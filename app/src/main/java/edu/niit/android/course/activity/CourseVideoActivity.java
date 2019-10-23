package edu.niit.android.course.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.adapter.VideoAdapter;
import edu.niit.android.course.dao.PlayListDao;
import edu.niit.android.course.entity.Course;
import edu.niit.android.course.entity.Video;
import edu.niit.android.course.utils.IOUtils;
import edu.niit.android.course.utils.SharedUtils;
import edu.niit.android.course.utils.StatusUtils;

public class CourseVideoActivity extends AppCompatActivity {
    // 控件对象
    private VideoView videoView;    // 视频播放器
    private ImageView ivVideo;      // 视频的第1帧数据显示
    private TextView tvIntro;       // 课程介绍，来自上一个界面的Course对象的intro属性
    private RecyclerView rvVideo;   // 视频列表

    // 数据对象
    private Course course;
    private List<Video> videos;
    private VideoAdapter adapter;

    private MediaController controller;  //多媒体播放进度条控制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setImmersionMode(this);
        setContentView(R.layout.activity_course_video);

        StatusUtils.initToolbar(this, "视频资源", true, false);

        // 1. 获取初始化界面的数据
        initData();
        // 2. 初始化界面控件，并填充数据
        initView();
        // 3. 加载视频的第1帧图像（可选）
        loadFirstFrame();
    }

    private void initData() {
        // 1. 接收从上一个界面传递的Bundle对象
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            course = (Course) bundle.getSerializable("course");
        }

        // 2. 从json文件中获取视频的描述数据
        videos = new ArrayList<>();

        try {
            // 2.1 获取json文件中的所有数据集合
            InputStream is = getResources().getAssets().open("course.json");
            String json = IOUtils.convert(is, StandardCharsets.UTF_8);
            videos = JSON.parseArray(json, Video.class);

            // 2.2 筛选出course的id对应的视频集合
            Iterator<Video> it = videos.iterator();
            while(it.hasNext()) {
                Video video = it.next();
                if(video.getChapterId() != course.getId()) {
                    it.remove();
                }
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        videoView = findViewById(R.id.video_view);
        controller = new MediaController(this);
        videoView.setMediaController(controller);

        ivVideo = findViewById(R.id.iv_video);
        tvIntro = findViewById(R.id.tv_intro);
        rvVideo = findViewById(R.id.rv_video);

        tvIntro.setText(course.getIntro());

        adapter = new VideoAdapter(videos);
        rvVideo.setLayoutManager(new LinearLayoutManager(this));
        rvVideo.setAdapter(adapter);

        adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 设置选中项，并通过notifyDataSetChanged()更新UI
                adapter.setSelected(position);
                adapter.notifyDataSetChanged();  // 更新UI

                // 获取Video对象的数据，并初始化VideoView
                Video video = videos.get(position);
                if(videoView.isPlaying()) {
                    videoView.stopPlayback();
                }

                if(TextUtils.isEmpty(video.getVideoPath())) {
                    Toast.makeText(CourseVideoActivity.this,
                            "本地没有此视频，暂时无法播放", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 播放视频
                videoView.setVisibility(View.VISIBLE);
                ivVideo.setVisibility(View.GONE);
                String uri = "android.resource://" + getPackageName() + "/" + R.raw.video101;
                videoView.setVideoPath(uri);
                videoView.start();

                // 将播放历史存储到数据库
//                if(SharedUtils.isLogin(CourseVideoActivity.this, "isLogin")) {
//                    String username = SharedUtils.readValue(CourseVideoActivity.this, "loginUser");
//                    PlayListDao.getInstance(CourseVideoActivity.this).save(video, username);
//                }
            }
        });
    }
    // 加载视频的首帧图像
    private void  loadFirstFrame() {
        Bitmap bitmap = null;

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video101);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, uri);
        bitmap = retriever.getFrameAtTime();
        ivVideo.setImageBitmap(bitmap);
    }



    // 播放视频
    private void play() {
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.video101;
        videoView.setVideoPath(uri);
        videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView != null) {
            videoView.stopPlayback();
            videoView = null;
        }
    }


}
