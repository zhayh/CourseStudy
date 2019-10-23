package edu.niit.android.course.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.niit.android.course.R;
import edu.niit.android.course.entity.Video;

public class PlayHistoryAdapter extends RecyclerView.Adapter<PlayHistoryAdapter.ViewHolder> {
    private List<Video> videos;
    private Context context;

    public PlayHistoryAdapter(List<Video> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_play_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Video video = videos.get(position);
        setData(holder, video);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(video == null) {
                    return;
                }
//                Intent intent = new Intent(context, VideoPlayActivity.class);
//                intent.putExtra("videoPath", video.getVideoPath());
//                context.startActivity(intent);
            }
        });
    }
    private void setData(ViewHolder holder, Video video) {
        if(video != null) {
            holder.tvTitle.setText(video.getTitle());
            holder.tvVideoTitle.setText(video.getVideoTitle());
            switch (video.getChapterId()) {
                case 1:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                case 2:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                case 3:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                case 4:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                case 5:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                case 6:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                case 7:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                case 8:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                case 9:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                case 10:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
                default:
                    holder.ivIcon.setImageResource(R.drawable.video_play_img1);
                    break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvVideoTitle;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvVideoTitle = itemView.findViewById(R.id.tv_video_title);
            ivIcon = itemView.findViewById(R.id.iv_video_icon);
        }
    }
}
