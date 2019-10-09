package edu.niit.android.course.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import edu.niit.android.course.R;

public class ViewPagerIndicator extends LinearLayout {
    private Context context;
    private int count;
    private int index;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setGravity(Gravity.CENTER);
    }

    /**
     * 设置滑动到当前小圆点时的颜色改变
     * @param currentIndex
     */
    public void setCurrentPosition(int currentIndex) {
        index = currentIndex;
        removeAllViews();
        int padding = 5;
        for(int i = 0; i < count; i++) {
            // 创建ImageView 放置小圆点
            ImageView imageView = new ImageView(context);
            if(index == i) {
                imageView.setImageResource(R.drawable.indicator_on);
            } else {
                imageView.setImageResource(R.drawable.indicator_off);
            }
            imageView.setPadding(padding, 0, padding, 0);
            addView(imageView);
        }
    }

    /**
     * 设置小圆点的数目
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }
}
