package com.nitinguru.app.nitinguru.adapters;

/**
 * Created by Rahul on 10-01-2016.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitinguru.app.nitinguru.R;
import com.nitinguru.app.nitinguru.models.DBChapter;

import java.util.List;

public class ChapterGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<DBChapter> chapters;

    @Override
    public int getCount() {
        return chapters.size();
    }

    public ChapterGridAdapter(Context c, List<DBChapter> chapters) {
        mContext = c;
        this.chapters = chapters;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_chapter_column, null);
            TextView tvdealname = (TextView) grid.findViewById(R.id.grid_deal_name);
            TextView tv_progress = (TextView) grid.findViewById(R.id.tv_progress);
            ImageView imgdealimg = (ImageView) grid.findViewById(R.id.grid_deal_image);

            DBChapter chapter = chapters.get(position);
            tvdealname.setText(chapter.getTitle());
            tv_progress.setText(chapter.getVisits()+"/"+chapter.getTopics());
            //new ImageDownloaderTask(imgdealimg).execute(currdeal.getDealIcon());
        } else {
            grid = (View) convertView;
        }
        Log.d("grid", position+"");
        return grid;
    }
}