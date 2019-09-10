package edu.niit.android.course;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class MyInfoFragment extends Fragment {
    private Context mContext;
    private TextView tvUsername;
    private LinearLayout headLayout;
    private RelativeLayout historyLayout, settingLayout;
    private boolean isLogin;  // 是否登录的标志位

    public MyInfoFragment() {
        // Required empty public constructor
    }

    public static MyInfoFragment newInstance() {
        return new MyInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1. 获取fragment的父Activity，以及目前的登录状态
        this.mContext = getContext();
        this.isLogin = checkLoginStatus();

        // 2. 获取fragment界面上需要处理的控件对象
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        tvUsername = view.findViewById(R.id.tv_username);
        setUsername(isLogin);  // 根据登录状态修改用户文本框的内容

        headLayout = view.findViewById(R.id.ll_head);
        historyLayout = view.findViewById(R.id.rl_history);
        settingLayout = view.findViewById(R.id.rl_setting);

        // 3. 设置事件监听器
        headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {

                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        // 播放历史布局的点击事件监听
        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {

                } else {
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 设置布局的点击事件监听
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    Intent intent = new Intent(mContext, SettingActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    /**
     * 登录返回的回调处理
     *
     * @param requestCode：请求码
     * @param resultCode：登录页面返回的结果码
     * @param data：登录页面返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            isLogin = data.getBooleanExtra("isLogin", false);
            setUsername(isLogin);
        }
    }

    /**
     * 根据登录状态设置用户名
     *
     * @param isLogin 是否登录的布尔值
     */
    private void setUsername(boolean isLogin) {
        if (isLogin) {
            tvUsername.setText(readLoginInfo());
        } else {
            tvUsername.setText("点击登录");
        }
    }

    /**
     * 获取登录状态
     */
    private boolean checkLoginStatus() {
        SharedPreferences sp = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }

    /**
     * 读取登录用户名
     *
     * @return
     */
    private String readLoginInfo() {
        SharedPreferences sp = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return sp.getString("loginUser", "");
    }


}
