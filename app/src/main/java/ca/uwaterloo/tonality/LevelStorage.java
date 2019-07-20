package ca.uwaterloo.tonality;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LevelStorage {

    private static LevelStorage INSTANCE = null;
    private static SharedPreferences pref;

    public static void init(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LevelStorage(context);
        }
    }

    public static LevelStorage getInstance() {
        return INSTANCE;
    }

    private LevelStorage(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void storeLevel(String scale, String level, int stars) {
        SharedPreferences.Editor editor = pref.edit();
        System.out.println("stars:" + scale + level);
        editor.putInt("stars:" + scale + level, stars);
        editor.apply();
    }

    public long getStarsForLevel(String scale, String level) {

        if(pref != null){
            return pref.getInt("stars:" + scale + level, 0);
        }
        else{
            return 0;
        }
    }
}
