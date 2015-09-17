package jp.s5r.kolchis;

import android.content.Context;

import java.util.ArrayList;

import jp.s5r.kolchis.factory.ExoPlayerFactory;
import jp.s5r.kolchis.factory.MediaPlayerFactory;
import jp.s5r.kolchis.factory.PlayerFactory;

public final class KolchisPlayerFactory {

    private final static ArrayList<PlayerFactory> factories = new ArrayList<>();

    static {
        factories.add(new ExoPlayerFactory());
        factories.add(new MediaPlayerFactory());
    }

    private KolchisPlayerFactory() {
    }

    public static KolchisPlayer getOptimalPlayer(final Context context) {
        float bestScore = 0.f;
        PlayerFactory bestScoreFactory = null;
        for (PlayerFactory factory : factories) {
            final float score = factory.scorePlayer(context);
            if (score > bestScore) {
                bestScore = score;
                bestScoreFactory = factory;
            }
        }
        if (bestScoreFactory != null) {
            return bestScoreFactory.createPlayer(context);
        }
        return null;
    }

    public static void addFactory(final PlayerFactory factory) {
        factories.add(factory);
    }
}
