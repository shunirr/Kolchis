package jp.s5r.kolchis.mediaplayer;

import android.view.Surface;

import jp.s5r.kolchis.KolchisPlayer;
import jp.s5r.kolchis.KolchisPlayerListener;

public class KolchisMediaPlayer implements KolchisPlayer {
    @Override
    public void setUserAgent(String userAgent) {

    }

    @Override
    public void setKolchisPlayerListener(KolchisPlayerListener listener) {

    }

    @Override
    public void setSurface(Surface surface) {

    }

    @Override
    public void clearSurface() {

    }

    @Override
    public void prepare(String uri) {

    }

    @Override
    public void start() {

    }

    @Override
    public void seekTo(long msec) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void release() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public int getBufferedPercentage() {
        return 0;
    }
}
