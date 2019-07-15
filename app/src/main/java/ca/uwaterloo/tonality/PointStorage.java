package ca.uwaterloo.tonality;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PointStorage {

    private final static String key = "points";
    private static PointStorage INSTANCE = null;
    private static SharedPreferences pref;

    public static void init(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PointStorage(context);
        }
    }

    public static PointStorage getInstance() {
        return INSTANCE;
    }

    private PointStorage(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void incrementScore(long score) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, getScore() + score);
        editor.apply();
    }

    public long getScore() {
        return pref.getLong(key, 0);
    }
}
