package site.qifen.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.note.db.NoteDao;
import site.qifen.note.db.NoteDatabase;
import site.qifen.note.model.Note;
import site.qifen.note.ui.NoteActivity;
import site.qifen.note.ui.NoteAdapter;
import site.qifen.note.ui.SettingActivity;
import site.qifen.note.util.NoteUtil;
import site.qifen.note.util.SpUtil;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.view_note_edit)
    EditText viewNoteEdit;
    @BindView(R.id.start_view_btn)
    Button startViewBtn;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.float_add_button)
    FloatingActionButton floatAddUtton;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.not_note)
    TextView notNote;

    private List<Note> noteList = new ArrayList<>();
    private NoteDao noteDao = NoteDatabase.getInstance().noteDao();
    private RecyclerView.LayoutManager layoutManager;
    private String[] spinnerArr = {"标题", "内容", "标签", "时间"};
    private String spinnerString = "标题";
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (SpUtil.read("tag").equals("")) SpUtil.write("tag", "临时,工作,生活,其它");

        noteList = noteDao.getAllNote();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        noteAdapter = new NoteAdapter(noteList, notNote, this);
        recyclerView.setAdapter(noteAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerString = spinnerArr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @OnClick({R.id.start_view_btn, R.id.float_add_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_view_btn:
                String text = viewNoteEdit.getText().toString();
                if (!text.equals("")) {
                    List<Note> tempList = new ArrayList<>();
                    switch (spinnerString) {
                        case "标题":
                            tempList = noteDao.getNoteLikeTitle(text);
                            break;
                        case "内容":
                            tempList = noteDao.getNoteLikeContent(text);
                            break;
                        case "标签":
                            tempList = noteDao.getNoteLikeTag(text);

                            break;
                        case "时间":
                            tempList = noteDao.getNoteLikeDate(text);
                            break;
                    }
                    recyclerView.setAdapter(new NoteAdapter(tempList, notNote, this));
                    noteAdapter.notifyDataSetChanged();
                } else {
                    NoteUtil.toast("搜索条件不能为空");
                }
                break;
            case R.id.float_add_button:
                startActivity(new Intent(this, NoteActivity.class));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_menu:
                startActivity(new Intent(this, SettingActivity.class));
                break;
//            case R.id.de_text_menu:
//
//                break;
//            case R.id.en_text_menu:
//
//                break;
            case R.id.backup_text_menu:
                noteList = noteDao.getBackUpNote();
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                noteAdapter = new NoteAdapter(noteList, notNote, this);
                recyclerView.setAdapter(noteAdapter);
                break;
            case R.id.all_text_menu:
                noteList = noteDao.getAllNote();
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                noteAdapter = new NoteAdapter(noteList, notNote, this);
                recyclerView.setAdapter(noteAdapter);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        NoteAdapter noteAdapter = new NoteAdapter(noteDao.getAllNote(), notNote, this);
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
    }


}
