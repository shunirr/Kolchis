package jp.s5r.kolchis.sample;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.Toast;

import jp.s5r.kolchis.KolchisPlayer;
import jp.s5r.kolchis.KolchisPlayerFactory;
import jp.s5r.kolchis.KolchisPlayerListener;
import jp.s5r.kolchis.widget.ViewRatioSettable;

public class SampleActivity extends Activity implements KolchisPlayerListener {

    private static final String TAG = SampleActivity.class.getSimpleName();

    private ViewRatioSettable videoView;
    private KolchisPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        player = KolchisPlayerFactory.getOptimalPlayer(getApplicationContext());
        if (player == null) {
            final String message = "Failed to create KolchisPlayer instance.";
            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        player.setKolchisPlayerListener(this);
        player.prepare("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8");
        final VideoViewCallback callback = new VideoViewCallback();
        callback.setListener(new VideoViewCallback.SurfaceListener() {
            @Override
            public void onCreateSurface(Surface surface) {
                player.setSurface(surface);
            }

            @Override
            public void onDestroySurface() {
                player.clearSurface();
            }
        });

        videoView = (ViewRatioSettable) findViewById(R.id.video_view);
        if (videoView instanceof SurfaceView) {
            ((SurfaceView) videoView).getHolder().addCallback(callback);
        } else if (videoView instanceof TextureView) {
            ((TextureView) videoView).setSurfaceTextureListener(callback);
        }
    }

    @Override
    public void onPreparing(KolchisPlayer player) {
    }

    @Override
    public void onPrepared(KolchisPlayer player) {
        player.start();
    }

    @Override
    public void onBuffering(KolchisPlayer player) {
    }

    @Override
    public void onPlaying(KolchisPlayer player) {
    }

    @Override
    public void onPaused(KolchisPlayer player) {
    }

    @Override
    public void onCompletion(KolchisPlayer player) {
    }

    @Override
    public void onError(KolchisPlayer player, Throwable t) {
    }

    @Override
    public void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio) {
        videoView.setAspectRatio((height == 0) ? 1 : (width * pixelWidthHeightRatio) / height);
    }

    static class VideoViewCallback implements SurfaceHolder.Callback, TextureView.SurfaceTextureListener {

        interface SurfaceListener {

            void onCreateSurface(Surface surface);

            void onDestroySurface();
        }

        private Surface currentSurface;

        private SurfaceListener listener;

        public void setListener(SurfaceListener listener) {
            this.listener = listener;
        }

        private void setSurface(Surface surface) {
            if (currentSurface != surface) {
                currentSurface = surface;
                if (listener != null) {
                    if (currentSurface != null) {
                        listener.onCreateSurface(currentSurface);
                    } else {
                        listener.onDestroySurface();
                    }
                }
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            setSurface(holder.getSurface());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            setSurface(null);
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            setSurface(new Surface(surface));
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            setSurface(null);
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    }
}
