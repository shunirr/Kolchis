package jp.s5r.kolchis;

import android.view.Surface;

public interface KolchisPlayer {

    void setUserAgent(String userAgent);

    void setKolchisPlayerListener(KolchisPlayerListener listener);

    void setSurface(Surface surface);

    void clearSurface();

    void prepare(String uri);

    void start();

    void seekTo(long msec);

    void pause();

    void release();

    boolean isPlaying();

    long getDuration();

    long getCurrentPosition();

    int getBufferedPercentage();
}
