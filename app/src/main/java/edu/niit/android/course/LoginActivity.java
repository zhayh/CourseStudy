package edu.niit.android.course;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.niit.android.course.utils.MD5Utils;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initToolbar();  // 初始化toolbar
        initView();  // 初始化界面控件
        initData();  // 初始化传入的数据
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.title_toolbar);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // 设置返回键
//            actionBar.setHomeButtonEnabled(true);    // 设置是否是首页
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        if (!TextUtils.isEmpty(username)) {
            etUsername.setText(username);
        }
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        TextView tvRegister = findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
    }

    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        password = MD5Utils.md5(password);
        String spPwd = readPwd(username);

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(spPwd)) {
            Toast.makeText(LoginActivity.this, "请先注册", Toast.LENGTH_SHORT).show();
        } else if (!spPwd.equals(MD5Utils.md5(spPwd))) {
            Toast.makeText(LoginActivity.this, "输入的密码不正确", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            saveLoginStatus(username, true);

            // 跳转到我的界面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("isLogin", true);
            LoginActivity.this.finish();

        }
    }

    private void saveLoginStatus(String username, boolean isLogin) {
        getSharedPreferences("userInfo", MODE_PRIVATE)
                .edit()
                .putString("loginUser", username)
                .putBoolean("isLogin", isLogin)
                .apply();
    }

    private String readPwd(String username) {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        return sp.getString(username, "");
    }
}
