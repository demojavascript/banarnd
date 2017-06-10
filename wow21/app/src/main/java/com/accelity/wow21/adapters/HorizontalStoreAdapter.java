package com.accelity.wow21.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accelity.wow21.R;
import com.accelity.wow21.activities.StoreDetailActivity;
import com.accelity.wow21.models.Store;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rahul on 16-08-2016.
 */
public class HorizontalStoreAdapter extends RecyclerView.Adapter<HorizontalStoreAdapter.StoreViewHolder> {
    ArrayList<Store> stores = new ArrayList<Store>();
    Context ctx;
    RecyclerView resourceIdd;
    public HorizontalStoreAdapter(ArrayList<Store> stores, Context ctx, RecyclerView resourceIdd){
        this.stores = stores;
        this.ctx = ctx;
        this.resourceIdd = resourceIdd;
    }
    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hv_stores_view, parent, false);
        StoreViewHolder contactViewHolder = new StoreViewHolder(view, ctx, stores, resourceIdd);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.tv_name.setText(store.getName());
        holder.tv_address.setText(store.getAddress());
        //holder.tv_closingDay.setText(store.getClosingDay());
        try{
            if(store.getClosingDay().length()<1){
                holder.loutclosedOn.setVisibility(View.GONE);
            }else{
                holder.tv_closingDay.setText(""+store.getClosingDay());
            }
        }catch(Exception e){
            e.printStackTrace();
            holder.loutclosedOn.setVisibility(View.GONE);
        }
        float fdis = Float.parseFloat(store.getDistance());
        String strDis = String.format("%.02f", fdis);
        Float distance = Float.parseFloat(strDis)/1000;
        String strDis2 = String.format("%.02f", distance);
        if(Float.parseFloat(strDis) < 1000.0) {
            holder.tv_distance.setText(" " + strDis + " mtr");
        }else{
            holder.tv_distance.setText(" " + strDis2.toString() + " km");
        }

        holder.tv_viewstore.setPaintFlags(holder.tv_viewstore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if(store.getIcon() != null){
            Picasso.with(ctx).load(store.getIcon()).into(holder.storeImg);
            //new ImageDownloaderTask(holder.storeImg).execute(store.getIcon());
        }
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }
    public static class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_address, tv_distance, tv_viewstore, tv_closingDay;
        ImageView storeImg;
        LinearLayout loutclosedOn;
        CardView cv_storehome;
        ArrayList<Store> stores = new ArrayList<Store>();
        Context ctx;
        public StoreViewHolder(View view, Context ctx, ArrayList<Store> stores, RecyclerView resourceIdd){
            super(view);
            this.stores = stores;
            this.ctx = ctx;
            loutclosedOn = (LinearLayout)view.findViewById(R.id.loutclosedOn);
            cv_storehome = (CardView)view.findViewById(R.id.cv_storehome);
            tv_name = (TextView) view.findViewById(R.id.tv_storename);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_distance = (TextView) view.findViewById(R.id.tv_distance);
            tv_viewstore = (TextView) view.findViewById(R.id.tv_store_view);
            tv_closingDay= (TextView) view.findViewById(R.id.tv_closingDay);
            storeImg = (ImageView) view.findViewById(R.id.img_store);
            cv_storehome.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ctx, StoreDetailActivity.class);
            intent.putExtra("storeId", stores.get(getAdapterPosition()).getID());
            ctx.startActivity(intent);
        }
    }
}
