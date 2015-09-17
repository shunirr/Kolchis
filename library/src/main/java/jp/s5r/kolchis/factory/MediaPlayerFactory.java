package jp.s5r.kolchis.factory;

import android.content.Context;

import jp.s5r.kolchis.KolchisPlayer;
import jp.s5r.kolchis.mediaplayer.KolchisMediaPlayer;

public class MediaPlayerFactory implements PlayerFactory {

    @Override
    public float scorePlayer(final Context context) {
        return 0.8f;
    }

    @Override
    public KolchisPlayer createPlayer(final Context context) {
        return new KolchisMediaPlayer(context);
    }
}
