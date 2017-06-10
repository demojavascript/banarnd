package com.accelity.wow21.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.models.HomeDeal;
import com.accelity.wow21.utilities.CustMessages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rahul on 08-07-2016.
 */
public class DealListAdapter2 extends RecyclerView.Adapter<DealListAdapter2.DealListViewHolder> {
    ArrayList<HomeDeal> deals = new ArrayList<HomeDeal>();
    Context ctx;
    RecyclerView resourceIdd;
    TextView tv_tamount, tv_tQty, tv_tSavings;
    int objlayout;
    public DealListAdapter2(ArrayList<HomeDeal> deals, Context ctx, RecyclerView resourceIdd, TextView tv_tamount, TextView tv_tQty, TextView tv_tSavings, int objlayout){
        this.deals = deals;
        this.ctx = ctx;
        this.resourceIdd = resourceIdd;
        this.tv_tamount = tv_tamount;
        this.tv_tQty = tv_tQty;
        this.tv_tSavings = tv_tSavings;
        this.objlayout = objlayout;
    }
    @Override
    public DealListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(objlayout, parent, false);
        DealListViewHolder contactViewHolder = new DealListViewHolder(view, ctx, deals, resourceIdd, tv_tamount, tv_tQty, tv_tSavings, objlayout);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(DealListViewHolder holder, int position) {
        HomeDeal hdeal = deals.get(position);

        holder.tv_dealdiscount.setText(hdeal.getDealDiscount());
        holder.tv_dealtitle.setText(hdeal.getVariantTitle());
        holder.tv_dealsell.setText(hdeal.getSellingPrice().toString());
        holder.tv_dealmrp.setText(hdeal.getMrp().toString());
        holder.tv_dealQty.setText("0");
        holder.tv_dealmrp.setPaintFlags(holder.tv_dealmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //if(hdeal.getDealIcon() != null){
            //new ImageDownloaderTask(holder.imgDeal).execute(hdeal.getDealIcon());
        //}
        if(hdeal.getDealIcon() != null){
            Picasso.with(ctx).load(hdeal.getDealIcon()).into(holder.imgDeal);
        }
        if(hdeal.getIsSuper()){
            holder.btn_addition.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }
    public static class DealListViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tv_dealtitle, tv_dealdiscount, tv_dealmrp, tv_dealsell, tv_dealQty, tv_dealtxt;
        ImageView imgDeal, btn_addition, btn_subtraction, btn_sealBtn;
        HomeDeal objDeal;
        private static Toast tincr = null;
        int currPos = 0;
        int layoutt;
        ArrayList<HomeDeal> Deals;
        TextView tv_tamount, tv_tQty, tv_tSavings, tv_rs1, tv_rs2, dealdiscountOff;
        RecyclerView resourceIdd;
        Context ctx;
        public DealListViewHolder(View view, Context ctx, ArrayList<HomeDeal> Deals, RecyclerView resourceIdd, TextView tv_tamount, TextView tv_tQty, TextView tv_tSavings,int layoutt){
            super(view);

            this.resourceIdd = resourceIdd;
            this.layoutt = layoutt;
            this.Deals = Deals;
            this.ctx = ctx;
            this.tv_tamount = tv_tamount;
            this.tv_tQty = tv_tQty;
            this.tv_tSavings = tv_tSavings;

            this.tv_rs1 = (TextView)view.findViewById(R.id.tv_rs1);
            this.tv_rs2 = (TextView)view.findViewById(R.id.tv_rs2);
            this.dealdiscountOff = (TextView)view.findViewById(R.id.dealdiscountOff);

            this.tv_dealtxt = (TextView)view.findViewById(R.id.tv_dealtxt);
            this.tv_dealtitle = (TextView)view.findViewById(R.id.dealtitle);
            this.tv_dealdiscount = (TextView)view.findViewById(R.id.dealdiscount);
            this.tv_dealmrp = (TextView)view.findViewById(R.id.dealMRPprice);
            this.tv_dealsell = (TextView)view.findViewById(R.id.dealSellingprice);
            this.tv_dealQty = (TextView)view.findViewById(R.id.tv_dealQty);
            this.imgDeal = (ImageView) view.findViewById(R.id.dealimgDeal);
            this.btn_addition = (ImageView) view.findViewById(R.id.btn_addition);
            this.btn_subtraction = (ImageView) view.findViewById(R.id.btn_subtract);
            this.btn_sealBtn = (ImageView) view.findViewById(R.id.btn_sealBtn);
        }
    }
}
