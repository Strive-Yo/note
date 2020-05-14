package site.qifen.note.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import site.qifen.note.model.Note;

@Dao
public interface NoteDao {

    @Query("select * from note")
    List<Note> getAllNote();

    @Insert
    void insertNote(Note note);

    @Query("select * from note where id = :id")
    Note getNoteById(int id);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("select * from note where `title` like '%' || :title || '%'")
    List<Note> getNoteLikeTitle(String title);

    @Query("select * from note where `tag` like '%' || :tag || '%'")
    List<Note> getNoteLikeTag(String tag);

    @Query("select * from note where `content` like '%' || :content || '%'")
    List<Note> getNoteLikeContent(String content);

    @Query("select * from note where `date` like '%' || :date || '%'")
    List<Note> getNoteLikeDate(String date);

    @Query("select * from note where backup = 1")
    List<Note> getBackUpNote();

    @Query("delete from note")
    void deleteAllNote();

    @Query("delete from note where backup = 1")
    void deleteAllBackupNote();
}
