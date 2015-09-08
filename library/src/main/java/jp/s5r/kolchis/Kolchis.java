package jp.s5r.kolchis;

import android.content.Context;

import jp.s5r.kolchis.exoplayer.KolchisExoPlayer;

public class Kolchis {
    private Kolchis() {
    }

    public static KolchisPlayer getOptimalPlayer(Context context) {
        return new KolchisExoPlayer(context);
    }
}
