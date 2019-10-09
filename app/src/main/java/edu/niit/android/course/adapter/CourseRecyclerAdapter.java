package edu.niit.android.course.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.entity.Course;
import edu.niit.android.course.entity.Exercise;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {
    private List<Course> courses;
    private List<Integer> imgIds;

    private OnItemClickListener itemClickListener;

    public CourseRecyclerAdapter(List<Course> courses) {
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Course course = courses.get(position);
        holder.ivImg.setImageResource(imgIds.get(position));
        holder.tvTitle.setText(course.getTitle());

        if(itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
