package site.qifen.note.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.note.R;
import site.qifen.note.db.NoteDao;
import site.qifen.note.db.NoteDatabase;
import site.qifen.note.util.NoteUtil;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.settingsInfoText)
    TextView settingsInfoText;
    @BindView(R.id.managerNoteTagBtn)
    Button managerNoteTagBtn;
    @BindView(R.id.clearBackupNote)
    Button clearBackupNote;
    @BindView(R.id.clearAllNote)
    Button clearAllNote;
    @BindView(R.id.SettingBackBtn)
    Button SettingBackBtn;

    private NoteDao noteDao = NoteDatabase.getInstance().noteDao();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.managerNoteTagBtn, R.id.clearBackupNote, R.id.clearAllNote, R.id.SettingBackBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.managerNoteTagBtn:
                startActivity(new Intent(this, EditTagActivity.class));
                break;
            case R.id.clearBackupNote:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("你确定删除所有备份的笔记吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteDao.deleteAllBackupNote();
                                NoteUtil.toast("删除所有成功!");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.show();
                break;
            case R.id.clearAllNote:
                AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                        .setTitle("你确定删除所有笔记吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteDao.deleteAllNote();
                                NoteUtil.toast("删除所有成功!");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog1.show();
                break;
            case R.id.SettingBackBtn:
                finish();
                break;
        }
    }
}
