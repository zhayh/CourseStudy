package edu.niit.android.course.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;

import edu.niit.android.course.R;
import edu.niit.android.course.adapter.ExerciseDetailAdapter;
import edu.niit.android.course.entity.ExerciseDetail;
import edu.niit.android.course.utils.IOUtils;
import edu.niit.android.course.utils.StatusUtils;

public class ExerciseDetailActivity extends AppCompatActivity
        implements ExerciseDetailAdapter.OnSelectListener {

    // 获取从ExerciseFragment传来的数据
    private int id;
    private String title;

    // 从xml文件中获得
    private List<ExerciseDetail> details;

    // 控件及Adapter
    private RecyclerView lvDetails;
    private ExerciseDetailAdapter adapter;

    // 保存得分
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setImmersionMode(this);  // 沉浸式状态栏（可选）
        setContentView(R.layout.activity_exercise_detail);

        initData();
        initView();

        // 自定义标题栏（可选）
        StatusUtils.initToolbar(this, title, true, false);
    }

    private void initData() {
        // 1. 获取从ExerciseFragment界面传递过来的章节id和章节标题
        id = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");

        // 2. 从xml文件获取习题内容
        details = new ArrayList<>();
        try {
            InputStream is = getResources().getAssets().open("chapter" + id + ".xml");
            // pull方式读取xml
            details = IOUtils.getXmlContents(is);

            // sax方式读取xml
//            details = IOUtils.getXmlBySAX(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        lvDetails = findViewById(R.id.lv_details);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lvDetails.setLayoutManager(manager);
        adapter = new ExerciseDetailAdapter(details, this);
        lvDetails.setAdapter(adapter);

        // 增加标题
//        TextView title = new TextView(this);
//        title.setTextColor(Color.parseColor("#000000"));
//        title.setText("一、选择题");
//        title.setPadding(10, 15, 0, 0);
//        lvExercises.addHeaderView(title);
    }

    @Override
    public void onSelectA(int position, ImageView ivA, ImageView ivB, ImageView ivC, ImageView ivD) {
        ExerciseDetail detail = details.get(position);
        // 判断如果答案不是1，则为A 选项
        if (detail.getAnswer() != 1) {
            detail.setSelect(1);
        } else {
            detail.setSelect(0);
            score += 1;
        }
        switch (detail.getAnswer()) {
            case 1:
                ivA.setImageResource(R.mipmap.ic_exercise_answer_right);
                break;
            case 2:
                ivB.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivA.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
            case 3:
                ivC.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivA.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
            case 4:
                ivD.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivA.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
        }
        // 选择后不允许再修改
        ExerciseDetailAdapter.setABCDEnable(false, ivA, ivB, ivC, ivD);
    }

    @Override
    public void onSelectB(int position, ImageView ivA, ImageView ivB, ImageView ivC, ImageView ivD) {
        ExerciseDetail detail = details.get(position);

        // 判断如果答案不是2，则为B 选项
        if (detail.getAnswer() != 2) {
            detail.setSelect(2);
        } else {
            detail.setSelect(0);
            score += 1;
        }

        switch (detail.getAnswer()) {
            case 1:
                ivA.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivB.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
            case 2:
                ivB.setImageResource(R.mipmap.ic_exercise_answer_right);
                break;
            case 3:
                ivC.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivB.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
            case 4:
                ivD.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivB.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
        }
        // 选择后不允许再修改
        ExerciseDetailAdapter.setABCDEnable(false, ivA, ivB, ivC, ivD);
    }

    @Override
    public void onSelectC(int position, ImageView ivA, ImageView ivB, ImageView ivC, ImageView ivD) {
        ExerciseDetail detail = details.get(position);

        // 判断如果答案不是3，则为C 选项
        if (detail.getAnswer() != 3) {
            detail.setSelect(3);
        } else {
            detail.setSelect(0);
            score += 1;
        }

        switch (detail.getAnswer()) {
            case 1:
                ivA.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivC.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
            case 2:
                ivB.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivC.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
            case 3:
                ivC.setImageResource(R.mipmap.ic_exercise_answer_right);
                break;
            case 4:
                ivD.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivC.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
        }
        // 选择后不允许再修改
        ExerciseDetailAdapter.setABCDEnable(false, ivA, ivB, ivC, ivD);
    }

    @Override
    public void onSelectD(int position, ImageView ivA, ImageView ivB, ImageView ivC, ImageView ivD) {
        ExerciseDetail detail = details.get(position);

        // 判断如果答案不是4，则为D 选项
        if (detail.getAnswer() != 4) {
            detail.setSelect(4);
        } else {
            detail.setSelect(0);
            score += 1;
        }

        switch (detail.getAnswer()) {
            case 1:
                ivA.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivD.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
            case 2:
                ivB.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivD.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
            case 3:
                ivC.setImageResource(R.mipmap.ic_exercise_answer_right);
                ivD.setImageResource(R.mipmap.ic_exercise_answer_error);
                break;
            case 4:
                ivD.setImageResource(R.mipmap.ic_exercise_answer_right);
                break;
        }
        // 选择后不允许再修改
        ExerciseDetailAdapter.setABCDEnable(false, ivA, ivB, ivC, ivD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercise_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = "本次练习得分：" + score * 20;
        switch(score) {
            case 0:
                msg += "，一题都没做对，加油";
                break;
            case 1:
            case 2:
                msg += "，没及格，继续努力";
                break;
            case 3:
                msg += "，刚及格，继续努力";
                break;
            case 4:
                msg += "，还不错，不要骄傲哦";
                break;
            case 5:
                msg += "，全做对，太棒了！";
                break;
        }
        new AlertDialog.Builder(this).setTitle("得分")
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .show();
        return true;
    }
}
