package jp.s5r.kolchis;

import android.content.Context;
import android.os.Build;

import java.lang.reflect.Method;

import jp.s5r.kolchis.exoplayer.KolchisExoPlayer;
import jp.s5r.kolchis.mediaplayer.KolchisMediaPlayer;

public class KolchisPlayerFactory {

    private static final String[] WORKAROUND58_CHIPSETS = new String[]{
            "APQ8064", "MSM8960", "MSM8930"
    };

    private KolchisPlayerFactory() {
    }

    public static KolchisPlayer getOptimalPlayer(Context context) {
        // ExoPlayer support Android 4.1 or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (!shouldUseWorkaround58(context)) {
                return new KolchisExoPlayer(context);
            }
        }
        return new KolchisMediaPlayer(context);
    }

    // https://github.com/google/ExoPlayer/issues/58
    private static boolean shouldUseWorkaround58(Context context) {
        final String board = Build.BOARD;
        final String platform = getSystemPropertyString(context, "ro.board.platform", Build.UNKNOWN);

        for (String chipsetName : WORKAROUND58_CHIPSETS) {
            if (board != null && chipsetName.equals(board.toUpperCase())) {
                return true;
            }
            if (platform != null && chipsetName.equals(platform.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private static String getSystemPropertyString(Context context, String key, String def) {
        String value;
        try {
            ClassLoader loader = context.getClassLoader();
            Class targetClass = loader.loadClass("android.os.SystemProperties");
            @SuppressWarnings("unchecked")
            Method targetMethod = targetClass.getMethod("get", String.class, String.class);
            value = (String) targetMethod.invoke(targetClass, key, def);
        } catch (Exception e) {
            value = def;
        }
        return value;
    }
}
