package site.qifen.note.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import site.qifen.note.R;
import site.qifen.note.db.NoteDao;
import site.qifen.note.db.NoteDatabase;
import site.qifen.note.model.Note;
import site.qifen.note.util.App;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> noteList;
    private Context context;
    private TextView textView;
    private NoteDao noteDao = NoteDatabase.getInstance().noteDao();

    public NoteAdapter(List<Note> noteList, TextView textView, Context context) {
        this.noteList = noteList;
        this.context = context;
        this.textView = textView;
        if (noteList.size() > 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView noteTitle;
        private TextView noteContent;
        private TextView noteDate;
        private TextView noteTag;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_item_title);
            noteContent = itemView.findViewById(R.id.note_item_content);
            noteTag = itemView.findViewById(R.id.note_item_tag);
            noteDate = itemView.findViewById(R.id.note_item_time);
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.noteTitle.setText(note.getTitle());
        holder.noteContent.setText(note.getContent());
        holder.noteTag.setText(note.getTag());
        holder.noteDate.setText(note.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("noteId", note.getId());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog1 = new AlertDialog.Builder(v.getContext())
                        .setTitle("你确定删除这个笔记吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteDao.deleteNote(note);
                                noteList.remove(note);
                                if (noteList.size() == 0) {
                                    textView.setVisibility(View.VISIBLE);
                                }
                                noteList = noteDao.getAllNote();
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                alertDialog1.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return noteList.size();
    }


}
