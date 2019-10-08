package edu.niit.android.course.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.adapter.AdViewPagerAdapter;
import edu.niit.android.course.entity.AdImage;

public class CourseFragment extends Fragment implements ViewPager.OnPageChangeListener {
    // 广告轮播图相关
    public static final int MSG_AD_ID = 1;  // 广告自动滑动的消息ID

    private ViewPager viewPager;
    private TextView tvDesc;                // 图片的描述
    private LinearLayout llPoint;           // 指示器的布局

    private List<AdImage> adImages;         // 数据
    private List<ImageView> imageViews;     // 图片的集合
    private int lastPos;                    // 之前的位置

    public CourseFragment() {
        // Required empty public constructor
    }
    private static CourseFragment fragment;
    public static CourseFragment newInstance() {
        if(fragment == null) {
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
//        adHandler.sendEmptyMessageDelayed(MSG_AD_ID, 5000);
        new AdSlideThread().start();
        return view;
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
                sendEmptyMessageDelayed(MSG_AD_ID, 5000);
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

}

//adPager.setOnTouchListener(new View.OnTouchListener() {
//@Override
//public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//        case MotionEvent.ACTION_DOWN:
//        adHandler.removeCallbacksAndMessages(null);
//        break;
//        case MotionEvent.ACTION_MOVE:
//        break;
//        case MotionEvent.ACTION_UP:
//        // 删除CourseFragment
//        adHandler.removeCallbacksAndMessages(null);
//        adHandler.sendEmptyMessageDelayed(CourseFragment.MSG_AD_ID, 5000);
//        v.performClick();  // 解决onTouch和onClick事件的冲突
//        break;
//        }
//        return true;
//        }
//
//
//        });