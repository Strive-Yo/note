package site.qifen.note.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.note.R;
import site.qifen.note.util.NoteUtil;
import site.qifen.note.util.SpUtil;

public class EditTagActivity extends AppCompatActivity {

    @BindView(R.id.editInfoText)
    TextView editInfoText;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.saveTypeBtn)
    Button saveTypeBtn;
    @BindView(R.id.backBtn)
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);
        ButterKnife.bind(this);
        String tag = SpUtil.read("tag");
        editText.setText(tag);
    }

    @OnClick({R.id.saveTypeBtn, R.id.backBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.saveTypeBtn:
                if (editText.getText().toString().equals("")) {
                    NoteUtil.toast("标签不能为空");
                } else {
                    SpUtil.write("tag", editText.getText().toString());
                    NoteUtil.toast("保存成功");
                    finish();
                }
                break;
            case R.id.backBtn:
                finish();
                break;
        }
    }
}
