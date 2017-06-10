package com.accelity.wow21.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accelity.wow21.R;
import com.accelity.wow21.models.Promotions;

import java.util.ArrayList;

/**
 * Created by Rahul on 16-08-2016.
 */
public class PromotionListAdapter extends RecyclerView.Adapter<PromotionListAdapter.PromotionViewHolder> {
    ArrayList<Promotions> promotions = new ArrayList<Promotions>();
    Context ctx;
    RecyclerView resourceIdd;
    public PromotionListAdapter(ArrayList<Promotions> promotions, Context ctx, RecyclerView resourceIdd){
        this.promotions = promotions;
        this.ctx = ctx;
        this.resourceIdd = resourceIdd;
    }
    @Override
    public PromotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_promotion, parent, false);
        PromotionViewHolder contactViewHolder = new PromotionViewHolder(view, ctx, promotions, resourceIdd);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(PromotionViewHolder holder, int position) {
        Promotions objpromotion = promotions.get(position);
        holder.tv_promotionType.setText(objpromotion.getPromotionType());
        holder.tv_promotionCode.setText(objpromotion.getPromotionCode());
        holder.tv_promotionDesc.setText(objpromotion.getPromotionDesc());
        holder.tv_promotionFrom.setText(objpromotion.getPromotionFrom());
        holder.tv_promotionTo.setText(objpromotion.getPromotionTo());
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }
    public static class PromotionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_promotionType, tv_promotionCode, tv_promotionDesc, tv_promotionFrom, tv_promotionTo;
        public PromotionViewHolder(View view, Context ctx, ArrayList<Promotions> promotions, RecyclerView resourceIdd){
            super(view);
            tv_promotionType = (TextView) view.findViewById(R.id.tv_promotionType);
            tv_promotionCode = (TextView) view.findViewById(R.id.tv_promotionCode);
            tv_promotionDesc = (TextView) view.findViewById(R.id.tv_promotionDesc);
            tv_promotionFrom = (TextView) view.findViewById(R.id.tv_promotionFrom);
            tv_promotionTo = (TextView) view.findViewById(R.id.tv_promotionTo);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
