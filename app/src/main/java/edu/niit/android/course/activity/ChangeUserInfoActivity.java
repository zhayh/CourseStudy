package edu.niit.android.course.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.niit.android.course.R;
import edu.niit.android.course.utils.StatusUtils;

public class ChangeUserInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setImmersionMode(this);
        setContentView(R.layout.activity_change_user_info);

        initData();
        initView();
        StatusUtils.initToolbar(this, title, true, false);
    }

    private EditText etContent;
    private ImageView ivDelete;
    private void initView() {
        etContent = findViewById(R.id.et_content);
        etContent.setText(value);

        ivDelete = findViewById(R.id.iv_delete);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setText("");
            }
        });
    }
    private String title;
    private String value;
    private int flag;
    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            title = bundle.getString("title");
            value = bundle.getString("value");
            flag = bundle.getInt("flag");
        }
    }
    // 加载选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_info, menu);
        return true;
    }
    // 菜单项的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
                save();
                break;
            case R.id.item_cancel:
                ChangeUserInfoActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void save() {
        Intent intent = new Intent();
        // 1. 获取输入的内容
        String value = etContent.getText().toString();
        String msg = "";
        if(flag == 1) {
            intent.putExtra("nickname", value);
            msg = "昵称不能为空";
        } else {
            intent.putExtra("signature", value);
            msg = "签名不能为空";
        }
        if(TextUtils.isEmpty(value)) {
            Toast.makeText(ChangeUserInfoActivity.this,
                    msg, Toast.LENGTH_SHORT).show();
        } else {
            // 2. 将输入的内容返回给UserInfoActivity
            setResult(RESULT_OK, intent);
            Toast.makeText(ChangeUserInfoActivity.this,
                    "修改成功", Toast.LENGTH_SHORT).show();
            ChangeUserInfoActivity.this.finish();
        }
    }

    // 当menu的showAsAction="never"时显示Icon
//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//        if(menu != null) {
//            if(menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
//                try {
//                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//                    method.setAccessible(true);
//                    method.invoke(menu, true);
//                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return super.onMenuOpened(featureId, menu);
//    }
}
