package jp.s5r.kolchis.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.s5r.kolchis.KolchisPlayer;
import jp.s5r.kolchis.KolchisPlayerListener;

public class KolchisMediaPlayer implements
        KolchisPlayer,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnInfoListener {

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private int currentState = STATE_IDLE;

    private static final int TYPE_UNKNOWN = -1;
    private static final int TYPE_LIVE = 0;
    private static final int TYPE_ON_DEMAND = 1;

    private int currentVideoType = TYPE_UNKNOWN;

    private final Context context;
    private KolchisPlayerListener listener;
    private MediaPlayer player;
    private Surface currentSurface;
    private int currentBufferedPercentage;
    private String currentUri;
    private Map<String, String> headers = new HashMap<>();
    private long beforePosition;

    public KolchisMediaPlayer(Context context) {
        this.context = context;
        setPlayingState(STATE_IDLE);
    }

    @Override
    public void setUserAgent(String userAgent) {
        headers.put("User-Agent", userAgent);
    }

    @Override
    public void setKolchisPlayerListener(KolchisPlayerListener listener) {
        this.listener = listener;
    }

    @Override
    public void setSurface(Surface surface) {
        if (currentSurface == null) {
            currentSurface = surface;
            if (currentUri != null) {
                prepare(currentUri);
            }
        } else {
            clearSurface();
            setSurface(surface);
        }
    }

    @Override
    public void clearSurface() {
        if (!isLiveStreaming()) {
            beforePosition = getCurrentPosition();
        }
        release();
    }

    @Override
    public void prepare(String uri) {
        if (player == null) {
            player = new MediaPlayer();
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            player.setOnBufferingUpdateListener(this);
            player.setOnVideoSizeChangedListener(this);
            player.setOnInfoListener(this);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        currentUri = uri;

        if (currentSurface != null) {
            currentVideoType = TYPE_UNKNOWN;
            currentBufferedPercentage = 0;

            player.setSurface(currentSurface);
            try {
                player.setDataSource(context, Uri.parse(uri), headers);
                player.prepareAsync();
                setPlayingState(STATE_PREPARING);
            } catch (IOException e) {
                setPlayingState(STATE_ERROR);
                if (listener != null) {
                    listener.onError(this, e);
                }
            }
        }
    }

    @Override
    public void start() {
        if (player != null) {
            try {
                if (isInPlaybackState()) {
                    player.start();
                    setPlayingState(STATE_PLAYING);
                }
            } catch (IllegalStateException e) {
                if (listener != null) {
                    listener.onError(this, e);
                }
            }
        }
    }

    @Override
    public void seekTo(long msec) {
        if (player != null) {
            try {
                player.seekTo((int) msec);
            } catch (IllegalStateException e) {
                if (listener != null) {
                    listener.onError(this, e);
                }
            }
        }
    }

    @Override
    public void pause() {
        if (player != null) {
            try {
                player.pause();
                setPlayingState(STATE_PAUSED);
            } catch (IllegalStateException e) {
                if (listener != null) {
                    listener.onError(this, e);
                }
            }
        }
    }

    @Override
    public void release() {
        currentSurface = null;

        if (player != null) {
            player.setSurface(null);

            try {
                player.reset();
                player.release();
                player = null;
                setPlayingState(STATE_IDLE);
            } catch (IllegalStateException e) {
                if (listener != null) {
                    listener.onError(this, e);
                }
            }
        }
    }

    @Override
    public long getDuration() {
        if (player != null && isInPlaybackState()) {
            try {
                return player.getDuration();
            } catch (IllegalStateException e) {
                if (listener != null) {
                    listener.onError(this, e);
                }
            }
        }
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        if (player != null && isInPlaybackState()) {
            try {
                return player.getCurrentPosition();
            } catch (IllegalStateException e) {
                if (listener != null) {
                    listener.onError(this, e);
                }
            }
        }
        return 0;
    }

    @Override
    public int getBufferedPercentage() {
        if (player != null && isInPlaybackState()) {
            return currentBufferedPercentage;
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        if (player != null) {
            try {
                return player.isPlaying();
            } catch (IllegalStateException e) {
                if (listener != null) {
                    listener.onError(this, e);
                }
            }
        }
        return false;
    }

    private boolean isLiveStreaming() {
        if (player != null && isInPlaybackState()) {
            if (currentVideoType == TYPE_UNKNOWN) {
                if (player.getDuration() <= 0) {
                    currentVideoType = TYPE_LIVE;
                } else {
                    currentVideoType = TYPE_ON_DEMAND;
                }
            }
        }
        return currentVideoType == TYPE_LIVE;
    }

    // [MediaPlayer]

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (beforePosition > 0) {
            seekTo(beforePosition);
            beforePosition = 0;
        }
        setPlayingState(STATE_PREPARED);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        setPlayingState(STATE_PLAYBACK_COMPLETED);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        String message;
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                message = "MediaPlayer.MEDIA_ERROR_SERVER_DIED (" + what + ")";
                break;
            case MediaPlayer.MEDIA_ERROR_IO:
                message = "MediaPlayer.MEDIA_ERROR_IO (" + what + ")";
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                message = "MediaPlayer.MEDIA_ERROR_MALFORMED (" + what + ")";
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                message = "MediaPlayer.MEDIA_ERROR_UNSUPPORTED (" + what + ")";
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                message = "MediaPlayer.MEDIA_ERROR_TIMED_OUT (" + what + ")";
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                message = "MediaPlayer.MEDIA_ERROR_UNKNOWN (" + what + ")";
                break;
            default:
                PvmfReturnCodes code = PvmfReturnCodes.valueOf(what);
                if (code != null) {
                    message = "PvmfError (PvmfReturnCodes: " + code.name() + ")";
                } else {
                    message = "Unknown Error (" + what + ")";
                }
                break;
        }

        setPlayingState(STATE_ERROR);
        if (listener != null) {
            listener.onError(this, new MediaPlayerException(what, message));
        }

        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        currentBufferedPercentage = percent;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (listener != null) {
            listener.onVideoSizeChanged(width, height, 1.f);
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (isInPlaybackState()) {
                    if (listener != null) {
                        listener.onBuffering(this);
                    }
                }
                break;

            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (isInPlaybackState()) {
                    if (listener != null) {
                        listener.onPlaying(this);
                    }
                }
                break;

            default:
                break;
        }
        return false;
    }

    private boolean isInPlaybackState() {
        return player != null
                && currentState != STATE_ERROR
                && currentState != STATE_IDLE
                && currentState != STATE_PREPARING;
    }

    private void setPlayingState(int newState) {
        if (currentState == newState) {
            return;
        }
        currentState = newState;

        switch (newState) {
            case STATE_ERROR:
                break;
            case STATE_IDLE:
                break;
            case STATE_PREPARING:
                if (listener != null) {
                    listener.onPreparing(this);
                }
                break;
            case STATE_PREPARED:
                if (listener != null) {
                    listener.onPrepared(this);
                }
                break;
            case STATE_PLAYING:
                if (listener != null) {
                    listener.onPlaying(this);
                }
                break;
            case STATE_PAUSED:
                if (listener != null) {
                    listener.onPaused(this);
                }
                break;
            case STATE_PLAYBACK_COMPLETED:
                if (listener != null) {
                    listener.onCompletion(this);
                }
                break;
        }
    }
}
