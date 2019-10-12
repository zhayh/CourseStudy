package edu.niit.android.course.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.activity.CourseVideoActivity;
import edu.niit.android.course.adapter.AdViewPagerAdapter;
import edu.niit.android.course.adapter.CourseAdapter;
import edu.niit.android.course.entity.AdImage;
import edu.niit.android.course.entity.Course;
import edu.niit.android.course.utils.HttpsUtil;
import edu.niit.android.course.utils.IOUtils;
import edu.niit.android.course.utils.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class CourseFragment extends Fragment implements ViewPager.OnPageChangeListener {
    // 广告轮播图相关
    public static final int MSG_AD_ID = 1;      // 广告自动滑动的消息ID
    public static final int MSG_COURSE_ID = 2;   // 从网络获取课程信息的消息ID

    private ViewPager viewPager;
    private TextView tvDesc;                // 图片的描述
    private LinearLayout llPoint;           // 指示器的布局

    private List<AdImage> adImages;         // 数据
    private List<ImageView> imageViews;     // 图片的集合
    private int lastPos;                    // 之前的位置

    // 课程章节相关
    private GridView gvCourse;
    private Handler courseHandler = new CourseHandler(this);

    public CourseFragment() {
        // Required empty public constructor
    }

    private static CourseFragment fragment;
    public static CourseFragment newInstance() {
        if (fragment == null) {
            fragment = new CourseFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        // 初始化数据、图片和控件对象
        initAdData();       // AdImage的集合
        initAdView(view);
        initIndicator(view);

        // 设置ViewPager的初始状态
        lastPos = 0;
        llPoint.getChildAt(0).setEnabled(true);
        tvDesc.setText(adImages.get(0).getDesc());
        viewPager.setAdapter(new AdViewPagerAdapter(imageViews));

        adHandler = new AdHandler(viewPager);
        new AdSlideThread().start();

        // 课程相关
        courseHandler = new CourseHandler(this);

        // 加载本地的课程视频的数据
        gvCourse = view.findViewById(R.id.gv_courses);
        loadCourseByFile();

        loadCourseByNet();
        loadCourseByOkHttp();
        return view;
    }

    /**
     * 从本地json文件获取数据，更新UI
     */
    private void loadCourseByFile() {
        try {
            InputStream is = getResources().getAssets().open("chapter_intro.json");
            String json = IOUtils.convert(is, StandardCharsets.UTF_8);
            List<Course> courses = JSON.parseArray(json, Course.class);
            if(courses != null) {
                updateCourse(courses);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCourse(List<Course> courses) {
        CourseAdapter adapter = new CourseAdapter(getContext(), courses);
        gvCourse.setAdapter(adapter);
        gvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course) parent.getItemAtPosition(position);
                // 跳转到课程详情界面
                Toast.makeText(getContext(), "点击了：" + course.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), CourseVideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course", course);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 利用HttpURLConnection从网络获取json数据，更新UI
     */
    private void loadCourseByNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = NetworkUtils.get("https://www.fastmock.site/mock/b46332ceba020b46458f016deac2c275/course/chapter");
                List<Course> courses = JSON.parseArray(json, Course.class);
                if(courses != null) {
                    Message msg = new Message();
                    msg.what = MSG_COURSE_ID;  // 整型常量，可以用整数直接代替
                    msg.obj = courses;
                    courseHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private static class CourseHandler extends Handler {
        private WeakReference<CourseFragment> ref;

        public CourseHandler(CourseFragment fragment) {
            this.ref = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            CourseFragment target = ref.get();

            if(msg.what == MSG_COURSE_ID) {
                List<Course> courses = (List<Course>) msg.obj;
                if(courses != null) {
                    target.updateCourse(courses);
                } else {
                    Toast.makeText(target.getContext(),"目前还没有课程数据", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 利用OkHttp从网络获取json数据，更新UI
     */
    private void loadCourseByOkHttp() {
        // 1. 创建一个Okhttp3的Request对象，装载url、header等request头
        Request request = new Request.Builder()
                .url("https://www.fastmock.site/mock/b46332ceba020b46458f016deac2c275/course/chapter")
                .addHeader("Accept", "application/json")
                .method("GET", null)
                .build();

        // 2. 采用OkHttp的enqueue异步方式发送请求，使用Callback回调处理响应
        HttpsUtil.handleSSLHandshakeByOkHttp().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // 处理请求失败的信息
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 2.1 提取Http body数据
                    if(response.body() != null) {
                        String json = response.body().string();
                        final List<Course> courses = JSON.parseArray(json, Course.class);

                        // 使用Handler的Message更新UI
//                    if(courses != null) {
//                        Message msg = new Message();
//                        msg.what = MSG_COURSE_ID;
//                        msg.obj = courses;
//                        courseHandler.sendMessage(msg);
//                    }
                        // 2.2 通过runOnUiThread回到主线程下更新UI
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateCourse(courses);
                            }
                        });
                    }
                }
            }
        });
    }

    // 初始化ViewPager需要的图片集合
    private void initAdData() {
        adImages = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AdImage adImage = new AdImage();
            adImage.setId(i + 1);
            switch (i) {
                case 0:
                    adImage.setImg("banner_1");
                    adImage.setDesc("新一代Apple Watch发布");
                    break;
                case 1:
                    adImage.setImg("banner_2");
                    adImage.setDesc("寒武纪发布AI芯片");
                    break;
                case 2:
                    adImage.setImg("banner_3");
                    adImage.setDesc("Google发布AI语音助手");
                    break;
                default:
                    break;
            }
            adImages.add(adImage);
        }
    }

    // 初始化控件，生成图片集合
    private void initAdView(View view) {
        tvDesc = view.findViewById(R.id.tv_desc);

        viewPager = view.findViewById(R.id.vp_banner);
        viewPager.addOnPageChangeListener(this);  // 设置ViewPager的监听

        // 生成图片集合
        imageViews = new ArrayList<>();
        for (int i = 0; i < adImages.size(); i++) {
            AdImage adImage = adImages.get(i);

            // 添加图片到集合中
            ImageView iv = new ImageView(getContext());
            if ("banner_1".equals(adImage.getImg())) {
                iv.setBackgroundResource(R.drawable.banner_1);
            } else if ("banner_2".equals(adImage.getImg())) {
                iv.setBackgroundResource(R.drawable.banner_2);
            } else if ("banner_3".equals(adImage.getImg())) {
                iv.setBackgroundResource(R.drawable.banner_3);
            }
            imageViews.add(iv);
        }

        // 监听触屏事件，按下后取消所有的消息，抬起则恢复
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        adHandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        // 删除CourseFragment
                        adHandler.removeCallbacksAndMessages(null);
                        adHandler.sendEmptyMessageDelayed(CourseFragment.MSG_AD_ID, 5000);
                        v.performClick();  // 解决onTouch和onClick事件的冲突
                        break;
                }
                return true;
            }
        });
    }

    // 添加指示器圆点
    private void initIndicator(View view) {
        llPoint = view.findViewById(R.id.ll_point);

        View pointView;
        for (int i = 0; i < adImages.size(); i++) {
            pointView = new View(getContext());
            pointView.setBackgroundResource(R.drawable.indicator_bg);
            pointView.setEnabled(false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            if (i != 0) {
                params.leftMargin = 10;
            }
            llPoint.addView(pointView, params);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int currentPos = position % adImages.size();
        tvDesc.setText(adImages.get(currentPos).getDesc());

        llPoint.getChildAt(lastPos).setEnabled(false);
        llPoint.getChildAt(currentPos).setEnabled(true);
        lastPos = currentPos;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 处理广告栏消息的Handler类
     */
    private AdHandler adHandler;
    private static class AdHandler extends Handler {
        private WeakReference<ViewPager> reference;

        public AdHandler(ViewPager viewPager) {
            reference = new WeakReference<>(viewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ViewPager viewPager = reference.get();
            if (viewPager == null) {
                return;
            }
            if (msg.what == MSG_AD_ID) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//                sendEmptyMessageDelayed(MSG_AD_ID, 5000);
            }
        }
    }

    /**
     * 使用多线程实现广告自动切换
     */
    private class AdSlideThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (adHandler != null) {
                    Message msg = adHandler.obtainMessage();
                    msg.what = MSG_AD_ID;
                    adHandler.sendMessage(msg);
//                    adHandler.sendEmptyMessage(MSG_AD_ID);
                }
            }
        }
    }
}

