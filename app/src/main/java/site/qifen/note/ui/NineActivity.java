package site.qifen.note.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.utils.TimeUtils;
import com.github.leondevlifelog.gesturelockview.GestureLockView;

import butterknife.BindView;
import butterknife.ButterKnife;
import site.qifen.note.MainActivity;
import site.qifen.note.R;
import site.qifen.note.util.App;
import site.qifen.note.util.NoteUtil;
import site.qifen.note.util.SpUtil;

public class NineActivity extends AppCompatActivity {

    @BindView(R.id.customGestureLockView)
    GestureLockView customGestureLockView;
    @BindView(R.id.BigTextInfo)
    TextView bigTextInfo;
    @BindView(R.id.gestureNumInfo)
    TextView gestureNumInfo;

    private int status = 3;  //0设置密码 1验证密码 3 被锁定
    private int verifyNum = 5;  //解锁次数默认5次,5次不通过就锁定一小时
    private String prePassword = "";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine);
        ButterKnife.bind(this);

//        byte[] key = PassUtil.getKey();
//        String text = PassUtil.en(key, "11111");
//        System.out.println("aes  " + Arrays.toString(key));
//        System.out.println("aes  " + text);
//        System.out.println("aes  " + PassUtil.de(key, text));

        System.out.println("lock" + SpUtil.readLong("lockTime"));

        if (SpUtil.readLong("lockTime") > System.currentTimeMillis()) {
            String lockTime = TimeUtils.milliseconds2String(SpUtil.readLong("lockTime"));
            bigTextInfo.setText("你已被锁定");
            gestureNumInfo.setText("你没机会了,已被锁定至" + lockTime);
        } else {
            if (SpUtil.read("password").equals("")) {
                bigTextInfo.setText("设置手势密码");
                gestureNumInfo.setText("请记住手势密码");
                status = 0;
            } else {
                bigTextInfo.setText("验证手势密码");
                status = 1;
            }
        }


        customGestureLockView.setOnCheckPasswordListener(new GestureLockView.OnCheckPasswordListener() {
            @Override
            public boolean onCheckPassword(String password) {
                if (status == 0) { //设置密码
                    if (SpUtil.read("password").equals("")) {
                        prePassword = password;
                        if (!prePassword.equals("")) SpUtil.write("password", prePassword);
                        gestureNumInfo.setText("再次验证手势密码");
                        return true;
                    } else {
                        if (password.equals(SpUtil.read("password"))) {
                            NoteUtil.toast("设置手势密码成功");
                            startActivity(new Intent(App.getContext(), MainActivity.class));
                            finish();
                            return true;
                        } else {
                            gestureNumInfo.setText("再次验证手势密码");
                            return false;
                        }
                    }


                } else if (status == 1) { //验证密码
                    if (verifyNum >= 0 && SpUtil.read("password").equals(password)) {
//                        NoteUtil.toast("手势验证成功");
                        startActivity(new Intent(App.getContext(), MainActivity.class));
                        finish();
                        return true;
                    } else {
                        verifyNum--;
                        gestureNumInfo.setText("你还有" + verifyNum + "次机会");
                        if (verifyNum <= 0) {
                            long time = (System.currentTimeMillis() + (60 * 60 * 1000));
                            gestureNumInfo.setText("你没机会了,已被锁定至" + TimeUtils.milliseconds2String(time));
                            SpUtil.writeLong("lockTime", time);
                        }
                        NoteUtil.toast("手势验证失败");

                    }
                }
                return false;
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if (status == 0) SpUtil.write("password", "");
    }
}
