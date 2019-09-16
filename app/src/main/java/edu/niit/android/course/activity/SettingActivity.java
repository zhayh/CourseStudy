package edu.niit.android.course.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import edu.niit.android.course.R;
import edu.niit.android.course.utils.SharedUtils;
import edu.niit.android.course.utils.StatusUtils;

public class SettingActivity extends AppCompatActivity {
    private RelativeLayout modifyLayout, securityLayout, exitLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setImmersionMode(this);
        setContentView(R.layout.activity_setting);

        StatusUtils.initToolbar(this, "设置", true, false);

        initView();
    }

    private void initView() {
        modifyLayout = findViewById(R.id.rl_modify_pwd);
        securityLayout = findViewById(R.id.rl_security_setting);
        exitLayout = findViewById(R.id.rl_exit_login);

        // 修改密码
        modifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ModifyPwdActivity.class);
                startActivity(intent);
            }
        });

        // 设置密保
        securityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, FindPwdActivity.class);
                intent.putExtra("from", "security");
                startActivity(intent);
            }
        });

        // 退出登录
        exitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "退出登录", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("退出")
                        .setMessage("确认退出登录？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedUtils.clearLoginInfo(SettingActivity.this);

                                // 返回我的界面
                                Intent intent = new Intent();
                                intent.putExtra("isLogin", false);
                                setResult(RESULT_OK, intent);
                                SettingActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
    }
}
