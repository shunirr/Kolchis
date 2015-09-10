package jp.s5r.kolchis.exoplayer;

import android.content.Context;
import android.media.AudioFormat;
import android.net.Uri;
import android.view.Surface;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.demo.player.DashRendererBuilder;
import com.google.android.exoplayer.demo.player.DemoPlayer;
import com.google.android.exoplayer.demo.player.ExtractorRendererBuilder;
import com.google.android.exoplayer.demo.player.HlsRendererBuilder;
import com.google.android.exoplayer.demo.player.SmoothStreamingRendererBuilder;
import com.google.android.exoplayer.util.Util;

import jp.s5r.kolchis.KolchisPlayer;
import jp.s5r.kolchis.KolchisPlayerListener;
import jp.s5r.kolchis.util.SuggestContentType;

public class KolchisExoPlayer implements KolchisPlayer, DemoPlayer.Listener {

    private static final AudioCapabilities DEFAULT_AUDIO_CAPABILITIES =
            new AudioCapabilities(new int[]{AudioFormat.ENCODING_PCM_16BIT}, 2);

    private final Context context;

    private KolchisPlayerListener listener;

    private DemoPlayer player;

    private String userAgent;

    public KolchisExoPlayer(Context context) {
        this.context = context;
        this.userAgent = Util.getUserAgent(this.context, "KolchisPlayer");
    }

    @Override
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public void setKolchisPlayerListener(KolchisPlayerListener listener) {
        this.listener = listener;
    }

    @Override
    public void setSurface(Surface surface) {
        if (player != null) {
            player.setSurface(surface);
        }
    }

    @Override
    public void clearSurface() {
        if (player != null) {
            player.blockingClearSurface();
        }
    }

    @Override
    public void prepare(String uri) {
        if (uri == null) {
            return;
        }
        if (player != null) {
            release();
        }
        player = new DemoPlayer(getRendererBuilder(Uri.parse(uri)));
        player.addListener(this);
        player.prepare();

        if (listener != null) {
            listener.onPreparing(this);
        }
    }

    @Override
    public void start() {
        if (player != null) {
            if (player.getPlaybackState() == DemoPlayer.STATE_IDLE) {
                return;
            }
            player.setPlayWhenReady(true);
            if (!player.getPlayWhenReady()) {
                player.setPlayWhenReady(true);
            }
            if (listener != null) {
                switch (player.getPlaybackState()) {
                    case DemoPlayer.STATE_PREPARING:
                        listener.onPreparing(this);
                        break;
                    case DemoPlayer.STATE_BUFFERING:
                        listener.onBuffering(this);
                        break;
                    case DemoPlayer.STATE_READY:
                        listener.onPlaying(this);
                        break;
                }
            }
        }
    }

    @Override
    public void seekTo(long msec) {
        if (player != null) {
            player.seekTo(msec);
        }
    }

    @Override
    public void pause() {
        if (player != null) {
            player.setPlayWhenReady(false);
            if (player.getPlayWhenReady()) {
                player.setPlayWhenReady(false);
            }
            if (listener != null) {
                listener.onPaused(this);
            }
        }
    }

    @Override
    public void release() {
        if (player != null) {
            player.removeListener(this);
            player.release();
            player = null;
        }
    }

    @Override
    public long getDuration() {
        if (player != null) {
            return player.getDuration();
        }
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        if (player != null) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getBufferedPercentage() {
        if (player != null) {
            return player.getBufferedPercentage();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return player != null
                && player.getPlaybackState() == DemoPlayer.STATE_READY
                && player.getPlayWhenReady();
    }

    // [DemoPlayer.Listener]

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case ExoPlayer.STATE_IDLE:
                break;
            case ExoPlayer.STATE_PREPARING:
                if (listener != null) {
                    listener.onPreparing(this);
                }
                break;
            case ExoPlayer.STATE_BUFFERING:
                if (listener != null) {
                    listener.onBuffering(this);
                    if (playWhenReady) {
                        listener.onPaused(this);
                    }
                }
                break;
            case ExoPlayer.STATE_READY:
                if (listener != null) {
                    listener.onPrepared(this);
                }
                break;
            case ExoPlayer.STATE_ENDED:
                if (listener != null) {
                    listener.onCompletion(this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(Exception e) {
        if (listener != null) {
            listener.onError(this, e);
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio) {
        if (listener != null) {
            listener.onVideoSizeChanged(width, height, pixelWidthHeightRatio);
        }
    }

    private DemoPlayer.RendererBuilder getRendererBuilder(Uri contentUri) {
        SuggestContentType.ContentType contentType = SuggestContentType.fromExtension(contentUri);
        switch (contentType) {
            case SMOOTH_STREAMING:
                return new SmoothStreamingRendererBuilder(context, userAgent, contentUri.toString(), null);

            case MPEG_DASH:
                return new DashRendererBuilder(context, userAgent, contentUri.toString(), null, DEFAULT_AUDIO_CAPABILITIES);

            case HLS:
                return new HlsRendererBuilder(context, userAgent, contentUri.toString(), DEFAULT_AUDIO_CAPABILITIES);

            default:
                return new ExtractorRendererBuilder(context, userAgent, contentUri);
        }
    }
}
