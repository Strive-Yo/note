package site.qifen.note.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import site.qifen.note.model.Note;
import site.qifen.note.util.App;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase noteDatabase;

    public abstract NoteDao noteDao();

    public synchronized static NoteDatabase getInstance() {
        if (noteDatabase == null) {
            noteDatabase = Room.databaseBuilder(App.getContext(),
                    NoteDatabase.class, "note.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return noteDatabase;
    }
}
