package site.qifen.note.util;

import android.widget.Toast;

public class NoteUtil {

    public static void toast(String text) {
        Toast.makeText(App.getContext(), text, Toast.LENGTH_LONG).show();
    }
}
