package jp.s5r.kolchis;

public interface KolchisPlayerListener {

    void onPreparing(KolchisPlayer player);

    void onPrepared(KolchisPlayer player);

    void onBuffering(KolchisPlayer player);

    void onPlaying(KolchisPlayer player);

    void onPaused(KolchisPlayer player);

    void onCompletion(KolchisPlayer player);

    void onError(KolchisPlayer player, Throwable t);

    void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio);

}
