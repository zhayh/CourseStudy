package edu.niit.android.course.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    // 1. 定义界面上的控件对象
    private TextView tvNickname, tvSignature, tvUsername, tvSex;
    private RelativeLayout nicknameLayout, signatureLayout, sexLayout, headLayout;
    private ImageView ivHead;

    // 2. 定义所需的变量
    private String spUsername;
    private UserInfo userInfo;
    private UserInfoService service;

    // 3. 文件存储相关的变量



    private static final int REQUEST_READ_PHOTO = 13;

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
        userInfo = service.get(spUsername);     // 从数据库读取
        userInfo = readFromInternal();          // 从内部存储读取
        userInfo = readPrivateExStorage();      // 从外部的私有存储读取
        userInfo = readPublicExternalStorage(); // 从外部的公有存储读取
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUsername(spUsername);
            userInfo.setNickname("课程助手");
            userInfo.setSignature("课程助手");
            userInfo.setSex("男");
            service.save(userInfo);
            saveToInternal(userInfo);
            savePrivateExStorage(userInfo);
            savePublicExternalStorage(userInfo);
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
                                saveToInternal(userInfo);
                                dialog.dismiss();
                            }
                        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 1. 对空数据、返回异常做判断
        if (data == null || resultCode != RESULT_OK) {
            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
            return;
        }
        // 2. 根据requestCode进行对应的保存
        // 2.1 获取data数据
        switch (requestCode) {
            case 1:
                // 2.2 设置userInfo对应的属性值，更新界面对应的控件内容
                String newNick = data.getStringExtra("nickname");
                if (!TextUtils.isEmpty(newNick)) {
                    tvNickname.setText(newNick);

                    // 2.3 保存到数据库
                    userInfo.setNickname(newNick);
                    service.modify(userInfo);

                    // 2.3 保存到内部存储
                    saveToInternal(userInfo);
                }
                break;
            case 2:
                String newSignature = data.getStringExtra("signature");
                if (!TextUtils.isEmpty(newSignature)) {
                    tvSignature.setText(newSignature);
                    userInfo.setSignature(newSignature);
                    service.modify(userInfo);
                    saveToInternal(userInfo);
                }
                break;
            case PICK_IMAGE:
                try {
                    Uri uri = data.getData();
                    Bitmap header = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    //
                    ivHead.setImageBitmap(header);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /*
        文件读写的流程
        1. 打开文件输入或输出流
        2. 创建BufferedReader或BufferedWriter对象读写文本文件
        3. 读写数据
        4. 关闭输入输出流
     */
    private static final String FILE_NAME = "userinfo.txt";
    // 2. 内部存储
    // 保存
    private void saveToInternal(UserInfo userInfo) {
        // 内部存储目录：data/data/包名/files/
        try {
            // 1. 打开文件输出流
            FileOutputStream out = this.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            // 2. 创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            // 3. 写入数据
            writer.write(JSON.toJSONString(userInfo));
            // 4. 关闭输出流s
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取
    private UserInfo readFromInternal() {
        UserInfo userInfo = null;
        try {
            FileInputStream in = this.openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String data = reader.readLine();
            userInfo = JSON.parseObject(data, UserInfo.class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    // 3. 外部私有存储
    // 保存
    private void savePrivateExStorage(UserInfo userInfo) {
        // 外部私有存储目录：/storage/emulated/0/Android/data/包名/files/
        try {
            // 1. 打开文件输出流
            File file = new File(getExternalFilesDir(""), FILE_NAME);
            FileOutputStream out = new FileOutputStream(file);
            // 2. 创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            // 3. 写入数据
            writer.write(JSON.toJSONString(userInfo));
            writer.flush();
            // 4. 关闭输出流
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取
    private UserInfo readPrivateExStorage() {
        // 外部私有存储目录：/storage/emulated/0/Android/data/包名/files/
        UserInfo userInfo = null;
        try {
            File file = new File(getExternalFilesDir(""), FILE_NAME);
            if(!file.exists()) {
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String data = reader.readLine();
            userInfo = JSON.parseObject(data, UserInfo.class);
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
    // 4. 外部公有存储
    // Android 6.0的权限申请回调标识
    private static final int REQUEST_READ_USERINFO = 101;
    private static final int REQUEST_WRITE_USERINFO = 102;
    private void savePublicExternalStorage(UserInfo userInfo) {
        // 外部私有存储目录：/storage/emulated/0/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_USERINFO);
                return;
            }
        }
        saveUserInfo(userInfo);
    }
    // 读取
    private UserInfo readPublicExternalStorage() {
        // 外部私有存储目录：/storage/emulated/0/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_USERINFO);
                return userInfo;
            }
        }
        return readUserInfo();
    }

    // 读写SD卡权限的请求回调
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 1. 判断申请的结果
        if (grantResults.length == 0 ||
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "申请权限被拒绝，无法执行操作",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // 2. 如果被允许，根据requestCode进行相应的处理
        if (requestCode == REQUEST_READ_USERINFO) {
            userInfo = readUserInfo();
        } else if (requestCode == REQUEST_WRITE_USERINFO) {
            saveUserInfo(userInfo);
        } else if (requestCode == REQUEST_READ_PHOTO) {
            choosePhoto();
        }
    }

    private void saveUserInfo(UserInfo userInfo) {
        // 检查SD卡的挂载情况
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(this, "无法读取SD卡", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 1. 打开文件输出流
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
            FileOutputStream out = new FileOutputStream(file);
            // 2. 创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            // 3. 写入数据
            writer.write(JSON.toJSONString(userInfo));
            writer.flush();
            // 4. 关闭输出流
            writer.close();
            out.close();
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    private UserInfo readUserInfo() {
        UserInfo userInfo = null;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
        try {
            FileInputStream in = new FileInputStream(file);
            int length = in.available();
            byte[] data = new byte[length];
            int len = in.read(data);
            userInfo = JSON.parseObject(data, UserInfo.class);
            Toast.makeText(this, "读取成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "读取失败", Toast.LENGTH_SHORT).show();
        }
        return userInfo;
    }

    private void modifyHeadIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_PHOTO);
                return;
            }
        }
        choosePhoto();
    }

    void choosePhoto() {
        // Intent.ACTION_PICK：隐性的Intent
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, PICK_IMAGE);
    }

}
