package com.nitinguru.app.nitinguru.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.activities.NotesActivity;
import com.nitinguru.app.nitinguru.models.DBTopic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 16-08-2016.
 */
public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {
    List<DBTopic> topics = new ArrayList<DBTopic>();
    Context ctx;
    RecyclerView resourceIdd;
    public TopicListAdapter(List<DBTopic> topics, Context ctx, RecyclerView resourceIdd){
        this.topics = topics;
        this.ctx = ctx;
        this.resourceIdd = resourceIdd;
    }
    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topics_view, parent, false);
        TopicViewHolder contactViewHolder = new TopicViewHolder(view, ctx, topics, resourceIdd);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        DBTopic topic = topics.get(position);
        holder.tv_name.setText(topic.getTitle());
        holder.img_1.setVisibility(View.GONE);
        holder.img_2.setVisibility(View.GONE);
        if(topic.getView() == 1){
            holder.img_2.setVisibility(View.VISIBLE);
        }else{
            holder.img_1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }
    public static class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        Context ctx;
        CardView card_view_storelist;
        List<DBTopic> topics = new ArrayList<DBTopic>();
        ImageView img_1, img_2;
        public TopicViewHolder(View view, Context ctx, List<DBTopic> topics, RecyclerView resourceIdd){
            super(view);
            this.ctx = ctx;
            this.topics = topics;
            img_1 = (ImageView)view.findViewById(R.id.img_1);
            img_2 = (ImageView)view.findViewById(R.id.img_2);
            card_view_storelist = (CardView)view.findViewById(R.id.card_view_topiclist);
            tv_name = (TextView) view.findViewById(R.id.tv_topicname);
            card_view_storelist.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            img_1.setVisibility(View.GONE);
            img_2.setVisibility(View.VISIBLE);
            Intent intent = new Intent(ctx, NotesActivity.class);
            intent.putExtra("chapterid", this.topics.get(getAdapterPosition()).getChapterid());
            intent.putExtra("topicid", this.topics.get(getAdapterPosition()).getId());
            ctx.startActivity(intent);
        }
    }
}
