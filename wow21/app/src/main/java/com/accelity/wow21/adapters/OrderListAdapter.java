package com.accelity.wow21.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.accelity.wow21.R;
import com.accelity.wow21.activities.OrderDetailActivity;
import com.accelity.wow21.models.Order;

import java.util.ArrayList;

/**
 * Created by Rahul on 16-08-2016.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {
    ArrayList<Order> orders = new ArrayList<Order>();
    Context ctx;
    RecyclerView resourceIdd;
    public OrderListAdapter(ArrayList<Order> orders, Context ctx, RecyclerView resourceIdd){
        this.orders = orders;
        this.ctx = ctx;
        this.resourceIdd = resourceIdd;
    }
    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_orderlist, parent, false);
        OrderListViewHolder contactViewHolder = new OrderListViewHolder(view, ctx, orders, resourceIdd);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(OrderListViewHolder holder, int position) {
        Order objOrder = orders.get(position);
        holder.tv_orderno.setText(objOrder.getOrderno());
        holder.tv_wdate.setText(objOrder.getDate());
        holder.tv_orderamount.setText(objOrder.getTotalamount()+"");
        holder.tv_storename.setText(objOrder.getStorname());
        holder.pop_ratingbar.setRating(objOrder.getOrderrating());
        if(objOrder.getOrderstatus() == 0){
            holder.view_orderstatus.setBackgroundColor(ctx.getResources().getColor(R.color.colorYellowBadge));
        }else if(objOrder.getOrderstatus() == 1){
            holder.view_orderstatus.setBackgroundColor(ctx.getResources().getColor(R.color.colorGreenBarDark));
        }else if(objOrder.getOrderstatus() == 2){
            holder.view_orderstatus.setBackgroundColor(ctx.getResources().getColor(R.color.colorRed));
        }
        if(objOrder.getTotaldeal() < 1){
            holder.tv_dealcount.setText(objOrder.getTotaldeal()+"");
        }else {
            holder.tv_dealcount.setText(objOrder.getTotaldeal()+"");
        }
    }
    @Override
    public int getItemCount() {
        return orders.size();
    }
    public static class OrderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_orderno, tv_orderamount, tv_wdate, tv_dealcount, tv_storename;
        CardView cardview_row_orderlist;
        Context ctx;
        RatingBar pop_ratingbar;
        View view_orderstatus;
        ArrayList<Order> orders;
        public OrderListViewHolder(View view, Context ctx, ArrayList<Order> orders, RecyclerView resourceIdd){
            super(view);
            this.orders = orders;
            this.ctx = ctx;
            view_orderstatus = (View)view.findViewById(R.id.view_orderstatus);
            pop_ratingbar = (RatingBar) view.findViewById(R.id.pop_ratingbar);
            tv_storename = (TextView) view.findViewById(R.id.tv_storename);
            tv_orderno = (TextView) view.findViewById(R.id.tv_orderno);
            tv_orderamount = (TextView) view.findViewById(R.id.tv_orderamount);
            tv_wdate = (TextView) view.findViewById(R.id.tv_wdate);
            tv_dealcount = (TextView) view.findViewById(R.id.tv_dealcount);
            cardview_row_orderlist = (CardView)view.findViewById(R.id.cardview_row_orderlist);
            cardview_row_orderlist.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ctx, OrderDetailActivity.class);
            intent.putExtra("order", orders.get(getAdapterPosition()));
            ctx.startActivity(intent);
        }
    }
}
