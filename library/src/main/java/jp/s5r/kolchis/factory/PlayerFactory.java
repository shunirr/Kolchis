package jp.s5r.kolchis.factory;

import android.content.Context;

import jp.s5r.kolchis.KolchisPlayer;

public interface PlayerFactory {

    float scorePlayer(Context context);

    KolchisPlayer createPlayer(Context context);
}
