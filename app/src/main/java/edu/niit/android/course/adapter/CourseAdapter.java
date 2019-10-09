package edu.niit.android.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.entity.Course;

public class CourseAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courses;
    private List<Integer> imgIds;

    public CourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
        setImgIds();
    }

    private void setImgIds() {
        imgIds = new ArrayList<>();
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_2);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
        imgIds.add(R.drawable.img_chapter_1);
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Course getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);

            holder = new ViewHolder();
            holder.ivImg = convertView.findViewById(R.id.iv_img);
            holder.tvImgTitle = convertView.findViewById(R.id.tv_img_title);
            holder.tvTitle = convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Course course = getItem(position);
        holder.ivImg.setImageResource(imgIds.get(position));
        holder.tvImgTitle.setText(course.getImgTitle());
        holder.tvTitle.setText(course.getTitle());
        return convertView;
    }

    static class ViewHolder {
        ImageView ivImg;
        TextView tvImgTitle;
        TextView tvTitle;
    }
}
