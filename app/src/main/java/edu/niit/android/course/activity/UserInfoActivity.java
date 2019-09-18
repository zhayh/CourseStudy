package edu.niit.android.course.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
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
    private static final int PICK_IMAGE = 3;

    private static final int REQUEST_PERMISSION_READ = 11;

    // 1. 定义界面上的控件对象
    private TextView tvNickname, tvSignature, tvUsername, tvSex;
    private RelativeLayout nicknameLayout, signatureLayout, sexLayout, headLayout;
    private ImageView ivHead;

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
        ivHead = findViewById(R.id.iv_head_icon);

        nicknameLayout = findViewById(R.id.rl_nickname);
        sexLayout = findViewById(R.id.rl_sex);
        signatureLayout = findViewById(R.id.rl_signature);
        headLayout = findViewById(R.id.rl_head);

        // 2. 设置数据库获取的数据
        tvUsername.setText(userInfo.getUsername());
        tvNickname.setText(userInfo.getNickname());
        tvSex.setText(userInfo.getSex());
        tvSignature.setText(userInfo.getNickname());

        // 3. 设置监听器
        nicknameLayout.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
        signatureLayout.setOnClickListener(this);
        headLayout.setOnClickListener(this);
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
            case R.id.rl_head:
                modifyHeadIcon();
        }
    }

    private void modifyHeadIcon() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_READ);
            } else {
                choosePhoto();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_READ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            choosePhoto();
        } else {
            Toast.makeText(this, "申请读取SD卡的权限未允许", Toast.LENGTH_SHORT).show();
        }
    }

    void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, PICK_IMAGE);
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
        bundle.putString("value", nickname);  // 内容
        bundle.putInt("flag", MODIFY_NICKNAME); // 用于区分修改昵称还是签名
        intent.putExtras(bundle);
        // 3. 启动下一个界面
        startActivityForResult(intent, MODIFY_NICKNAME);
    }

    private void modifySignature() {
        String signature = tvSignature.getText().toString();
        Intent intent = new Intent(UserInfoActivity.this, ChangeUserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", "设置签名");
        bundle.putString("value", signature);
        bundle.putInt("flag", MODIFY_SIGNATURE);
        intent.putExtras(bundle);
        startActivityForResult(intent, MODIFY_SIGNATURE);
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
        // 1. 对空数据、返回异常做判断
        if(data == null || resultCode != RESULT_OK) {
            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
            return;
        }
        // 2. 根据requestCode进行对应的保存
        // 2.1 获取data数据
        if(requestCode == 1) {
            // 2.2 设置userInfo对应的属性值，更新界面对应的控件内容
            String value = data.getStringExtra("nickname");
            tvNickname.setText(value);
            userInfo.setNickname(value);
        } else if(requestCode == 2) {
            String value = data.getStringExtra("signature");
            tvSignature.setText(value);
            userInfo.setSignature(value);
        }
        // 2.3 保存到数据库
        service.modify(userInfo);
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(data == null || resultCode != RESULT_OK) {
//            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        switch (requestCode) {
//            case 1:
//                String newNick = data.getStringExtra("nickname");
//                if(!TextUtils.isEmpty(newNick)) {
//                    tvNickname.setText(newNick);
//                    userInfo.setNickname(newNick);
//                    service.modify(userInfo);
//                }
//                break;
//            case 2:
//                String newSignature = data.getStringExtra("signature");
//                if(!TextUtils.isEmpty(newSignature)) {
//                    tvSignature.setText(newSignature);
//                    userInfo.setSignature(newSignature);
//                    service.modify(userInfo);
//                }
//                break;
//            case PICK_IMAGE:
//                try {
//                    Uri uri = data.getData();
//                    Bitmap header = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                    ivHead.setImageBitmap(header);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                break;
//        }
//    }
}
