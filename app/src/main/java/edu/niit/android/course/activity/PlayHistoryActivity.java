package edu.niit.android.course.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.adapter.PlayHistoryAdapter;
import edu.niit.android.course.adapter.VideoAdapter;
import edu.niit.android.course.dao.PlayListDao;
import edu.niit.android.course.entity.Video;
import edu.niit.android.course.utils.SharedUtils;
import edu.niit.android.course.utils.StatusUtils;

public class PlayHistoryActivity extends AppCompatActivity {
    private List<Video> videos;
    private RecyclerView rvHistory;
    private PlayHistoryAdapter adapter;
    private TextView tvNone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setImmersionMode(this);
        setContentView(R.layout.activity_play_history);

        StatusUtils.initToolbar(this, "播放历史", true, false);

        initData();
        initView();
    }

    private void initView() {
        tvNone = findViewById(R.id.tv_none);
        rvHistory = findViewById(R.id.rv_play_list);
        if(videos.isEmpty()) {
            tvNone.setVisibility(View.VISIBLE);
        }
        adapter = new PlayHistoryAdapter(videos);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);
    }

    private void initData() {
        String username = SharedUtils.readValue(this, "loginUser");
        videos = PlayListDao.getInstance(this).getVideoHistory(username);
    }
}
