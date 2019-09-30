package edu.niit.android.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.entity.ExerciseDetail;

public class ExerciseDetailAdapter extends RecyclerView.Adapter<ExerciseDetailAdapter.ViewHolder> {
    private List<ExerciseDetail> details;
    private List<String> selectedPos;  // 记录点击的位置
    private OnSelectListener onSelectListener; // 监听选项的选择事件，用于修改相应的图标

    // 创建自定义ViewHolder类
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject, tvA, tvB, tvC, tvD;
        ImageView ivA, ivB, ivC, ivD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.tv_subject);
            tvA = itemView.findViewById(R.id.tv_a);
            tvB = itemView.findViewById(R.id.tv_b);
            tvC = itemView.findViewById(R.id.tv_c);
            tvD = itemView.findViewById(R.id.tv_d);
            ivA = itemView.findViewById(R.id.iv_a);
            ivB = itemView.findViewById(R.id.iv_b);
            ivC = itemView.findViewById(R.id.iv_c);
            ivD = itemView.findViewById(R.id.iv_d);
        }
    }
    // 回调接口，监听A、B、C 和D 选项的选择，更换图标
    public interface OnSelectListener {
        void onSelectA(int position, ImageView ivA, ImageView ivB, ImageView ivC, ImageView ivD);
        void onSelectB(int position, ImageView ivA, ImageView ivB, ImageView ivC, ImageView ivD);
        void onSelectC(int position, ImageView ivA, ImageView ivB, ImageView ivC, ImageView ivD);
        void onSelectD(int position, ImageView ivA, ImageView ivB, ImageView ivC, ImageView ivD);
    }

    // 参数：习题的集合，监听器（由Activity实现）
    public ExerciseDetailAdapter(List<ExerciseDetail> details, OnSelectListener onSelectListener) {
        this.details = details;
        selectedPos = new ArrayList<>();
        this.onSelectListener = onSelectListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercises_detail, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // 1. 获取数据
        ExerciseDetail detail = details.get(position);
        // 2. 给控件赋值
        holder.subject.setText(detail.getSubject());
        holder.tvA.setText(detail.getA());
        holder.tvB.setText(detail.getB());
        holder.tvC.setText(detail.getC());
        holder.tvD.setText(detail.getD());

        holder.ivA.setImageResource(R.mipmap.ic_exercise_a);
        holder.ivB.setImageResource(R.mipmap.ic_exercise_b);
        holder.ivC.setImageResource(R.mipmap.ic_exercise_c);
        holder.ivD.setImageResource(R.mipmap.ic_exercise_d);

        // 3. 设置每个图标的监听，并处理事件（根据选项判断答案是否正确，显示相应的图标）
        holder.ivA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos = String.valueOf(position);
                if (selectedPos.contains(pos)) {
                    selectedPos.remove(pos);
                } else {
                    selectedPos.add(pos);
                }
                // 修改图标的显示
                onSelectListener.onSelectA(position, holder.ivA, holder.ivB, holder.ivC, holder.ivD);
            }
        });
        holder.ivB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos = String.valueOf(position);
                if (selectedPos.contains(pos)) {
                    selectedPos.remove(pos);
                } else {
                    selectedPos.add(pos);
                }
                onSelectListener.onSelectB(position, holder.ivA, holder.ivB, holder.ivC, holder.ivD);
            }
        });
        holder.ivC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos = String.valueOf(position);
                if (selectedPos.contains(pos)) {
                    selectedPos.remove(pos);
                } else {
                    selectedPos.add(pos);
                }
                onSelectListener.onSelectC(position, holder.ivA, holder.ivB, holder.ivC, holder.ivD);
            }
        });
        holder.ivD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos = String.valueOf(position);
                if (selectedPos.contains(pos)) {
                    selectedPos.remove(pos);
                } else {
                    selectedPos.add(pos);
                }
                onSelectListener.onSelectD(position, holder.ivA, holder.ivB, holder.ivC, holder.ivD);
            }
        });
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    private void setSelectedDrawable(ViewHolder holder, int position) {
        holder.ivA.setImageResource(R.mipmap.ic_exercise_a);
        holder.ivB.setImageResource(R.mipmap.ic_exercise_b);
        holder.ivC.setImageResource(R.mipmap.ic_exercise_c);
        holder.ivD.setImageResource(R.mipmap.ic_exercise_d);
        setABCDEnable(true, holder.ivA, holder.ivB, holder.ivC, holder.ivD);
    }

    // 设置习题选项是否可点击
    public static void setABCDEnable(boolean value, ImageView ivA, ImageView ivB, ImageView ivC, ImageView ivD) {
        ivA.setEnabled(value);
        ivB.setEnabled(value);
        ivC.setEnabled(value);
        ivD.setEnabled(value);
    }
}
