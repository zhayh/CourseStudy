package edu.niit.android.course.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.adapter.AdBannerAdapter;
import edu.niit.android.course.entity.AdImage;
import edu.niit.android.course.view.ViewPagerIndicator;

public class AnotherCourseFragment extends Fragment {
    // 广告相关
    public static final int MSG_AD_ID = 1;  // 广告自动滑动
    private ViewPager adPager;
    private View adLayout;
    private AdBannerAdapter adAdapter;
    private ViewPagerIndicator indicator;
    private TextView tvDesc;

    private List<AdImage> adImages;
    private AdHandler adHandler;

    /**
     * 处理广告栏消息的Handler类
     */
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
            if (msg.what == MSG_AD_ID ) {
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
                    adHandler.sendEmptyMessage(MSG_AD_ID);
                }
            }
        }
    }

    public AnotherCourseFragment() {
        // Required empty public constructor
    }

    public static AnotherCourseFragment newInstance() {
        AnotherCourseFragment fragment = new AnotherCourseFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_another_course, container, false);

        initAdData();
        initAdView(view);

        adHandler = new AdHandler(adPager);
        new AdSlideThread().start();
        return view;
    }

    private void initAdView(View view) {
        // 广告栏
        adPager = view.findViewById(R.id.vp_banner);
        adPager.setLongClickable(false);
        adAdapter = new AdBannerAdapter(getChildFragmentManager(), adImages);
        adPager.setAdapter(adAdapter);
        adPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (adAdapter.getSize() > 0) {
                    int pos = position % adAdapter.getSize();
                    indicator.setCurrentPosition(pos);
                    tvDesc.setText(adImages.get(pos).getDesc());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 设置广告栏的宽高
        adLayout = view.findViewById(R.id.rl_adBanner);
        resetSize();

        // 创建提示器，并显示
        tvDesc = view.findViewById(R.id.tv_desc);
        indicator = view.findViewById(R.id.vp_indicator);
        indicator.setCount(adAdapter.getSize());
        if (adImages != null && !adImages.isEmpty()) {
            indicator.setCount(adImages.size());
            indicator.setCurrentPosition(0);
            tvDesc.setText(adImages.get(0).getDesc());
        }

        // 监听触屏事件，按下后取消所有的消息，抬起则恢复
        adPager.setOnTouchListener(new View.OnTouchListener() {
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

    /**
     * 设置广告栏的宽高
     */
    private void resetSize() {
        if(getActivity() != null) {
            int screenWidth = getScreenWidth(getActivity());
            ViewGroup.LayoutParams params = adLayout.getLayoutParams();
            params.width = screenWidth;
            params.height = screenWidth / 2;
            adLayout.setLayoutParams(params);
        }
    }

    /**
     * 读取屏幕的宽度
     * @param activity
     * @return
     */
    private int getScreenWidth(@NonNull Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 初始化广告的数据
     */
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
}
