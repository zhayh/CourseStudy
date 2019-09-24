package edu.niit.android.course.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.entity.Exercise;

public class ExerciseAdapter extends BaseAdapter {
    private List<Exercise> exercises;
    private Context context;

    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    // list的总记录数，若为0的话，即使有数据也不会显示
    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public Exercise getItem(int position) {
        return exercises.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 根据数据的记录数，根据item_exercise.xml加载每个数据
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // 1. 加载item_exercise.xml布局，只需要加载一次
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exercise, null);

            // 2. 获取控件对象
            holder.tvOrder = convertView.findViewById(R.id.tv_order);
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.tvSubTitle = convertView.findViewById(R.id.tv_sub_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 3. 加载数据
        final Exercise exercise = getItem(position);
        if (exercise != null) {
            holder.tvOrder.setText(String.valueOf(position + 1));
            holder.tvTitle.setText(exercise.getTitle());
            holder.tvSubTitle.setText(exercise.getSubTitle());

            // 设置圆角背景的颜色
            GradientDrawable drawable = (GradientDrawable) holder.tvOrder.getBackground();
            drawable.setColor(Color.parseColor(exercise.getBgColor()));
        }
        return convertView;
    }

    // 存放item_exercise.xml布局中的所有控件
    static class ViewHolder {
        TextView tvOrder, tvTitle, tvSubTitle;
    }
}
