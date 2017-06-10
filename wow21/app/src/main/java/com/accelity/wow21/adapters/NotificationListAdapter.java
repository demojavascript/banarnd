package com.accelity.wow21.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accelity.wow21.R;
import com.accelity.wow21.models.Notification;
import com.accelity.wow21.utilities.CustomUtil;
import com.accelity.wow21.utilities.NetConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rahul on 16-08-2016.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder> {
    ArrayList<Notification> notifications = new ArrayList<Notification>();
    Context ctx;
    RecyclerView resourceIdd;

    private NetConnection netConnection;
    public NotificationListAdapter(ArrayList<Notification> notifications, Context ctx, RecyclerView resourceIdd){
        this.notifications = notifications;
        this.ctx = ctx;
        this.resourceIdd = resourceIdd;
    }
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notifications, parent, false);
        NotificationViewHolder contactViewHolder = new NotificationViewHolder(view, ctx, notifications, resourceIdd);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification objNotification = notifications.get(position);
        holder.tv_title.setText(objNotification.getTitle());
        holder.tv_desc.setText(objNotification.getDesc());

        //Log.d("bsnner", objNotification.getSmallIMGURL());

        if(objNotification.getLargeIMGURL().length() > 0 && objNotification.getLargeIMGURL() != null){
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(ctx);
            if(!networkDetails.isEmpty()) {
                //if(getBitmapFromURL(objNotification.getSmallIMGURL()) != null) {

                //}
                if(objNotification.getScreenId().equals("99")){
                    holder.lout_upper.setVisibility(View.GONE);
                    holder.imfLIMG.setVisibility(View.GONE);
                    holder.imfLIMGBanner.setVisibility(View.VISIBLE);
                    //if(getBitmapFromURL(objNotification.getLargeIMGURL()) != null) {
                        holder.imfLIMGBanner.setImageBitmap(getBitmapFromURL(objNotification.getLargeIMGURL()));
                    //}
                }else{
                    //if(getBitmapFromURL(objNotification.getLargeIMGURL()) != null) {
                        holder.imfLIMG.setImageBitmap(getBitmapFromURL(objNotification.getLargeIMGURL()));
                    //}
                }
            }
        }else{
            holder.imfLIMG.setVisibility(View.GONE);
        }

        Log.d("SBanner", objNotification.getSmallIMGURL()+"");
        if(objNotification.getSmallIMGURL().length() > 0 && objNotification.getSmallIMGURL() != null){
            netConnection = new NetConnection();
            Map<String, String> networkDetails = netConnection.getConnectionDetails(ctx);
            if(!networkDetails.isEmpty()) {
                holder.img_smallImg.setImageBitmap(getBitmapFromURL(objNotification.getSmallIMGURL()));
            }
        }

        try {
            long msDiff = CustomUtil.totalTimeDiff2(objNotification.getDate());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(msDiff);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(msDiff);
            long hours = TimeUnit.MILLISECONDS.toHours(msDiff);
            long days = TimeUnit.MILLISECONDS.toDays(msDiff);
            String finalduration = "";
            if(days == 1){
                finalduration = days+" day ago";
            }else if(days > 1){
                finalduration = days+" days ago";
            }else if(hours == 1){
                finalduration = hours+" hour ago";
            }else if(hours > 1){
                finalduration = hours+" hours ago";
            }else if(minutes == 1){
                finalduration = minutes+" minute ago";
            }else if(minutes > 1){
                finalduration = minutes+" minutes ago";
            }else if(seconds > 0){
                finalduration = " few seconds ago";
            }
            holder.tv_wdate.setText(finalduration+"");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public int getItemCount() {
        return notifications.size();
    }
    public static class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_title, tv_desc, tv_wdate ;
        ImageView img_smallImg, imfLIMG, imfLIMGBanner;
        LinearLayout lout_upper;
        public NotificationViewHolder(View view, Context ctx, ArrayList<Notification> notifications, RecyclerView resourceIdd){
            super(view);
            lout_upper = (LinearLayout) view.findViewById(R.id.lout_upper);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_wdate = (TextView) view.findViewById(R.id.tv_wdate);
            img_smallImg = (ImageView)view.findViewById(R.id.img_smallImg);
            imfLIMG = (ImageView)view.findViewById(R.id.imfLIMG);
            imfLIMGBanner = (ImageView)view.findViewById(R.id.imfLIMGBanner);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
