package site.qifen.note.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.note.R;
import site.qifen.note.db.NoteDao;
import site.qifen.note.db.NoteDatabase;
import site.qifen.note.model.Note;
import site.qifen.note.util.App;
import site.qifen.note.util.NoteUtil;
import site.qifen.note.util.SpUtil;

public class NoteActivity extends AppCompatActivity {

    @BindView(R.id.note_write_title_text)
    TextView noteWriteTitleText;
    @BindView(R.id.note_write_title_edit)
    EditText noteWriteTitleEdit;
    @BindView(R.id.note_write_tag_text)
    TextView noteWriteTagText;
    @BindView(R.id.note_write_tag_edit)
    TextView noteWriteTagEdit;
    @BindView(R.id.note_write_content_text)
    EditText noteWriteContentText;
    @BindView(R.id.note_write_back_btn)
    Button noteWriteBackBtn;
    @BindView(R.id.note_write_save_btn)
    Button noteWriteSaveBtn;
    @BindView(R.id.note_text_info)
    TextView noteTextInfo;
    @BindView(R.id.note_write_backup_btn)
    Button noteWriteBackupBtn;
    private NoteDao noteDao = NoteDatabase.getInstance().noteDao();
    private String[] list = {};//要填充的数据
    private ListPopupWindow listPopupWindow = null;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);
        list = SpUtil.read("tag").split(",");
        noteWriteTagEdit.setText(list[0]);
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(noteWriteTagEdit);//以哪个控件为基准，在该处以mEditText为基准

        if (getIntent().getBooleanExtra("isEdit", false)) {
            note = noteDao.getNoteById(getIntent().getIntExtra("noteId", -1));
            if (note != null) {
                noteTextInfo.setText("改/查笔记");
                noteWriteTitleEdit.setText(note.getTitle());
                noteWriteTagEdit.setText(note.getTag());
                noteWriteContentText.setText(note.getContent());
            }
        }
    }

    @OnClick({R.id.note_write_back_btn, R.id.note_write_backup_btn, R.id.note_write_save_btn, R.id.note_write_tag_edit})
    public void onViewClicked(View view) {
        String titleText = noteWriteTitleEdit.getText().toString();
        String tagText = noteWriteTagEdit.getText().toString();
        String contentText = noteWriteContentText.getText().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        switch (view.getId()) {
            case R.id.note_write_back_btn:
                finish();
                break;
            case R.id.note_write_backup_btn:
                if (note == null) {
                    noteDao.insertNote(new Note(tagText, "[备份]"+titleText, contentText, date, true));
                    NoteUtil.toast("备份成功");
                    finish();
                }
                break;
            case R.id.note_write_tag_edit:
                listPopupWindow.setAdapter(new ArrayAdapter<String>(App.getContext(), android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
                if (!listPopupWindow.isShowing()) showListPopWindow();
                break;
            case R.id.note_write_save_btn:
                if (!titleText.equals("") && !tagText.equals("") && !contentText.equals("")) {
                    if (note == null) { //添加逻辑
                        noteDao.insertNote(new Note(tagText, titleText, contentText, date, false));
                        NoteUtil.toast("保存成功");
                        finish();
                    } else { //修改逻辑
                        note.setTitle(titleText);
                        note.setContent(contentText);
                        note.setDate(date);
                        note.setTag(tagText);
                        noteDao.updateNote(note);
                        NoteUtil.toast("修改成功");
                        finish();
                    }
                } else {
                    NoteUtil.toast("标题/标签/内容不能为空");
                }
                break;
        }
    }

    private void showListPopWindow() {
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                noteWriteTagEdit.setText(list[i]);//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
