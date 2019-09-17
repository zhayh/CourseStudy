package edu.niit.android.course.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.entity.UserInfo;
import edu.niit.android.course.service.UserInfoService;
import edu.niit.android.course.service.UserInfoServiceImpl;
import edu.niit.android.course.utils.SharedUtils;
import edu.niit.android.course.utils.StatusUtils;

/**
 * 完成显示个人信息的功能
 * 就完成修改昵称、修改性别和签名的功能
 */
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MODIFY_NICKNAME = 1;
    private static final int MODIFY_SIGNATURE = 2;

    // 1. 定义界面上的控件对象
    private TextView tvNickname, tvSignature, tvUsername, tvSex;
    private RelativeLayout nicknameLayout, signatureLayout, sexLayout;

    // 2. 定义所需的变量
    private String spUsername;
    private UserInfo userInfo;
    private UserInfoService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setImmersionMode(this);
        setContentView(R.layout.activity_user_info);

        // 3. 设置标题栏（可选）
        StatusUtils.initToolbar(this, "个人信息", true, false);
        // 4. 从数据库、网络、intent或存储中获取数据，初始化界面控件（可选）
        initData();
        // 5. 获取所有控件对象，设置监听器（必须）
        initView();
    }
    private void initData() {
        spUsername = SharedUtils.readValue(this, "loginUser");

        service = new UserInfoServiceImpl(this);
        userInfo = service.get(spUsername);
        if(userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUsername(spUsername);
            userInfo.setNickname("课程助手");
            userInfo.setSignature("课程助手");
            userInfo.setSex("男");
            service.save(userInfo);
        }
    }
    private void initView() {
        // 1. 获取控件对象
        tvUsername = findViewById(R.id.tv_username);
        tvNickname = findViewById(R.id.tv_nickname);
        tvSex = findViewById(R.id.tv_sex);
        tvSignature = findViewById(R.id.tv_signature);

        nicknameLayout = findViewById(R.id.rl_nickname);
        sexLayout = findViewById(R.id.rl_sex);
        signatureLayout = findViewById(R.id.rl_signature);
        // 2. 设置数据库获取的数据
        tvUsername.setText(userInfo.getUsername());
        tvNickname.setText(userInfo.getNickname());
        tvSex.setText(userInfo.getSex());
        tvSignature.setText(userInfo.getNickname());
        // 3. 设置监听器
        nicknameLayout.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
        signatureLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 根据id分别进行事件处理
        switch (v.getId()) {
            case R.id.rl_nickname:
                // 将用户名、昵称等信息传给ChangeUserInfoActivity进行修改保存并返回
                modifyNickname();
                break;
            case R.id.rl_signature:
                // 将用户名、签名等信息传给ChangeUserInfoActivity进行修改保存并返回
                modifySignature();
                break;
            case R.id.rl_sex:
                // 通过对话框修改
                modifySex();
                break;
        }
    }

    private void modifyNickname() {
        // 1. 获取已有的内容
        String nickname = tvNickname.getText().toString();
        // 2. 根据需要传递数据到下一个Activity
        Intent intent = new Intent(UserInfoActivity.this, ChangeUserInfoActivity.class);
        // Bundle对象用于传递有明确类型的简单类型和复杂数据类型的数据（简单类型数据也可以用Intent传递）
        // Bundle需要加载到Intent中才能传递
        Bundle bundle = new Bundle();
        bundle.putString("title", "设置昵称"); // 标题栏的标题
        bundle.putString("value", nickname); // 内容
        bundle.putInt("flag", 1); // 用于区分修改昵称还是签名
        intent.putExtras(bundle);
        // 3. 启动下一个界面
        startActivityForResult(intent, 1);
    }

    private void modifySignature() {
        String signature = tvSignature.getText().toString();
        Intent intent = new Intent(UserInfoActivity.this, ChangeUserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", "设置签名");
        bundle.putString("value", signature);
        bundle.putInt("flag", 2);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
    }

    private void modifySex() {
        final String[] datas = {"男", "女"};
        String sex = tvSex.getText().toString();
        // 获取性别所在的索引
        final List<String> sexs = Arrays.asList(datas);
        int selected = sexs.indexOf(sex);
        new AlertDialog.Builder(this)
                .setTitle("性别")
                .setSingleChoiceItems(datas, selected,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sex = datas[which];
                        tvSex.setText(sex);
                        userInfo.setSex(sex);
                        service.modify(userInfo);
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null || resultCode != RESULT_OK) {
            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case MODIFY_NICKNAME:
                String newNick = data.getStringExtra("nickname");
                if(!TextUtils.isEmpty(newNick)) {
                    tvNickname.setText(newNick);
                    userInfo.setNickname(newNick);
                    service.modify(userInfo);
                }
                break;
            case MODIFY_SIGNATURE:
                String newSignature = data.getStringExtra("signature");
                if(!TextUtils.isEmpty(newSignature)) {
                    tvSignature.setText(newSignature);
                    userInfo.setSignature(newSignature);
                    service.modify(userInfo);
                }
                break;
        }
    }
}
