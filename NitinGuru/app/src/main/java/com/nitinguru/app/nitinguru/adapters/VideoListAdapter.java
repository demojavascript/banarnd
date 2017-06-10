package com.nitinguru.app.nitinguru.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.models.Video;
import com.nitinguru.app.nitinguru.utils.Helper;

import java.util.ArrayList;

/**
 * Created by Rahul on 16-08-2016.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    ArrayList<Video> videos = new ArrayList<Video>();
    Context ctx;
    RecyclerView resourceIdd;
    public VideoListAdapter(ArrayList<Video> videos, Context ctx, RecyclerView resourceIdd){
        this.videos = videos;
        this.ctx = ctx;
        this.resourceIdd = resourceIdd;
    }
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video_view, parent, false);
        VideoViewHolder contactViewHolder = new VideoViewHolder(view, ctx, videos, resourceIdd);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.tv_videoname.setText(video.getTitle());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
    public static class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_videoname;
        Context ctx;
        CardView card_view_storelist;
        ArrayList<Video> videos = new ArrayList<Video>();
        public VideoViewHolder(View view, Context ctx, ArrayList<Video> videos, RecyclerView resourceIdd){
            super(view);
            this.ctx = ctx;
            this.videos = videos;
            card_view_storelist = (CardView)view.findViewById(R.id.card_view_videolist);
            tv_videoname = (TextView) view.findViewById(R.id.tv_videoname);
            card_view_storelist.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Helper.playVideo(videos.get(getAdapterPosition()).getKey(), ctx);
        }
    }
}
