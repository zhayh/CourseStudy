package edu.niit.android.course.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.entity.AdImage;
import edu.niit.android.course.fragment.AdBannerFragment;
import edu.niit.android.course.fragment.CourseFragment;

// FragmentStatePagerAdapter：销毁不需要的fragment
// FragmentPagerAdapter：仅销毁fragment视图，不销毁fragment实例
public class AdBannerAdapter extends FragmentStatePagerAdapter{
    private List<AdImage> adImages;


    public AdBannerAdapter(FragmentManager fm) {
        this(fm, null);
        this.adImages = new ArrayList<>();
    }

    public AdBannerAdapter(FragmentManager fm, List<AdImage> adImages) {
        super(fm);
        this.adImages = adImages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        if (adImages.size() > 0) {
            args.putString("ad", adImages.get(position % adImages.size()).getImg());
        }
        return AdBannerFragment.newInstance(args);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    // 防止刷新时显示缓存数据
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    /**
     * 返回数据集的真实容量大小
     */
    public int getSize() {
        return adImages.size();
    }
}
