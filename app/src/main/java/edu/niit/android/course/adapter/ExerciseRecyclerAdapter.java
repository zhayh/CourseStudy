package edu.niit.android.course.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.entity.Exercise;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ViewHolder> {
    private List<Exercise> exercises;


    public ExerciseRecyclerAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Exercise exercise = exercises.get(position);
        holder.tvOrder.setText(String.valueOf(exercise.getId()));
        holder.tvTitle.setText(exercise.getTitle());
        holder.tvSubTitle.setText(exercise.getSubTitle());

        // 设置圆角背景的颜色
        GradientDrawable drawable = (GradientDrawable) holder.tvOrder.getBackground();
        drawable.setColor(Color.parseColor(exercise.getBgColor()));

        // 设置监听器
        if(itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    // 回调事件的接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrder, tvTitle, tvSubTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrder = itemView.findViewById(R.id.tv_order);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubTitle = itemView.findViewById(R.id.tv_sub_title);
        }
    }
}
