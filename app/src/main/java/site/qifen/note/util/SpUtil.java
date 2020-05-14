package site.qifen.note.util;

import android.content.Context;
import android.content.SharedPreferences;


public class SpUtil {

    public static void write(String key, String value) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences("note", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String read(String key) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences("note", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static long readLong(String key) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences("note", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }


    public static void writeLong(String key, long value) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences("note", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }
}
