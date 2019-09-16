package edu.niit.android.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.niit.android.course.R;
import edu.niit.android.course.utils.MD5Utils;
import edu.niit.android.course.utils.SharedUtils;
import edu.niit.android.course.utils.StatusUtils;

public class ModifyPwdActivity extends AppCompatActivity {
    private EditText etOldPwd, etNewPwd, etNewPwdAgain;
    private Button btnSave;

    private String oldPwd, newPwd, newPwdAgain;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);

        StatusUtils.initToolbar(ModifyPwdActivity.this, "修改密码", true, false);
        initView();
    }

    private void initView() {
        etOldPwd = findViewById(R.id.et_old_pwd);
        etNewPwd = findViewById(R.id.et_new_pwd);
        etNewPwdAgain = findViewById(R.id.et_new_pwd_again);

        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {
        oldPwd = etOldPwd.getText().toString();
        newPwd = etNewPwd.getText().toString();
        newPwdAgain = etNewPwdAgain.getText().toString();
        username = SharedUtils.readValue(this, "loginUser");

        if (TextUtils.isEmpty(oldPwd)) {
            Toast.makeText(ModifyPwdActivity.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(newPwdAgain)) {
            Toast.makeText(ModifyPwdActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (!newPwd.equals(newPwdAgain)) {
            Toast.makeText(ModifyPwdActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ModifyPwdActivity.this, "新密码设置成功", Toast.LENGTH_SHORT).show();
            SharedUtils.saveStrValue(this, username, MD5Utils.md5(newPwd));
            SharedUtils.clearLoginInfo(this);
            Intent intent = new Intent(ModifyPwdActivity.this, LoginActivity.class);
            startActivity(intent);
            ModifyPwdActivity.this.finish();
        }
    }

}
