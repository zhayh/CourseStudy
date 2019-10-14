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
    private VideoView videoView;
    private ImageView ivVideo;
    private TextView tvIntro;
    private RecyclerView rvVideo;

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

        initData();
        initView();
        loadFirstFrame();
    }

    // 加载视频的首帧图像
    private void  loadFirstFrame() {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video101));
        bitmap = retriever.getFrameAtTime();
        ivVideo.setImageBitmap(bitmap);
    }

    private void initView() {
        videoView = findViewById(R.id.video_view);
        videoView.setMediaController(new MediaController(this));
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
                adapter.notifyDataSetChanged();

                Video video = videos.get(position);
                if(videoView.isPlaying()) {
                    videoView.stopPlayback();
                }

                if(TextUtils.isEmpty(video.getVideoPath())) {
                    Toast.makeText(CourseVideoActivity.this, "本地没有此视频，暂时无法播放", Toast.LENGTH_SHORT).show();
                    return;
                }
                videoView.setVisibility(View.VISIBLE);
                ivVideo.setVisibility(View.GONE);
                play();

                if(SharedUtils.isLogin(CourseVideoActivity.this, "isLogin")) {
                    String username = SharedUtils.readValue(CourseVideoActivity.this, "loginUser");
                    PlayListDao.getInstance(CourseVideoActivity.this).save(video, username);
                }
            }
        });
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

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            course = (Course) bundle.getSerializable("course");
        }

        videos = new ArrayList<>();
        try {
            InputStream is = getResources().getAssets().open("course.json");
            String json = IOUtils.convert(is, StandardCharsets.UTF_8);
            videos = IOUtils.convert(json, Video.class);

            Iterator<Video> it = videos.iterator();
            while(it.hasNext()) {
                Video video = it.next();
                if(video.getChapterId() != course.getId()) {
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
