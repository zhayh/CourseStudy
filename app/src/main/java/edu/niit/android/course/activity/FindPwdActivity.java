package edu.niit.android.course.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.niit.android.course.R;
import edu.niit.android.course.utils.MD5Utils;
import edu.niit.android.course.utils.SharedUtils;
import edu.niit.android.course.utils.StatusUtils;

public class FindPwdActivity extends AppCompatActivity {

    private TextView tvResetPwd;
    private EditText etValidateName, etUsername;
    private Button btnValidate;
    private String from;
    private String validateName, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);

        from = getIntent().getStringExtra("from");
        String title = "设置密保";
        if(!"security".equals(from)) {
            title = "找回密码";

            tvResetPwd = findViewById(R.id.tv_reset_pwd);
            etUsername = findViewById(R.id.et_username);
            tvResetPwd.setVisibility(View.VISIBLE);
            etUsername.setVisibility(View.VISIBLE);
        }
        StatusUtils.initToolbar(FindPwdActivity.this, title, true, false);
        initView();
    }

    private void initView() {
        etValidateName = findViewById(R.id.et_validate_name);

        btnValidate = findViewById(R.id.btn_validate);
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateName = etValidateName.getText().toString();

                if("security".equals(from)) {
                    setSecurity();
                } else {
                    findPwd();
                }
            }
        });
    }

    private void findPwd() {
        username = etUsername.getText().toString();
        String security = SharedUtils.readValue(this, username + "_security");

        if(TextUtils.isEmpty(username)) {
            Toast.makeText(FindPwdActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else if(!SharedUtils.isExist(this, username)) {
            Toast.makeText(FindPwdActivity.this, "输入的用户名不存在", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(validateName)) {
            Toast.makeText(FindPwdActivity.this, "验证名不能为空", Toast.LENGTH_SHORT).show();
        } else if(!validateName.equals(security)) {
            Toast.makeText(FindPwdActivity.this, "输入的密保不正确", Toast.LENGTH_SHORT).show();
        } else {
            tvResetPwd.setVisibility(View.VISIBLE);
            tvResetPwd.setText("初始密码：123456，请尽快修改密码");
            SharedUtils.saveStrValue(this, username, MD5Utils.md5("123456"));
            finish();
        }
    }

    private void setSecurity() {
        if(TextUtils.isEmpty(validateName)) {
            Toast.makeText(FindPwdActivity.this, "验证名不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(FindPwdActivity.this, "密保设置成功", Toast.LENGTH_SHORT).show();
            String loginUser = SharedUtils.readValue(this, "loginUser");
            SharedUtils.saveStrValue(this, loginUser + "_security", validateName);
            finish();
        }
    }


}
