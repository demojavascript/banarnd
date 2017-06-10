package com.accelity.wow21.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accelity.wow21.R;
import com.accelity.wow21.models.Passbook;

import java.util.ArrayList;

/**
 * Created by Rahul on 16-08-2016.
 */
public class PassbookListAdapter extends RecyclerView.Adapter<PassbookListAdapter.PassbookViewHolder> {
    ArrayList<Passbook> passbook = new ArrayList<Passbook>();
    Context ctx;
    RecyclerView resourceIdd;
    public PassbookListAdapter(ArrayList<Passbook> passbook, Context ctx, RecyclerView resourceIdd){
        this.passbook = passbook;
        this.ctx = ctx;
        this.resourceIdd = resourceIdd;
    }
    @Override
    public PassbookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_passbook, parent, false);
        PassbookViewHolder contactViewHolder = new PassbookViewHolder(view, ctx, passbook, resourceIdd);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(PassbookViewHolder holder, int position) {
        Passbook objpassbook = passbook.get(position);
        holder.tv_wdate.setText(objpassbook.getWhendate());
        if(objpassbook.getCredit() > 0){
            holder.tv_orderno.setText(objpassbook.getRemark());
            holder.img_paid_rec.setImageResource(R.drawable.ic_rupeereceived);
            holder.tv_source.setText("Cashback Received");
            holder.tv_debitorcredit.setText("+");
            holder.tv_amount.setText(objpassbook.getCredit()+"");
        }else{
            holder.img_paid_rec.setImageResource(R.drawable.ic_rupeepaid);
            holder.tv_orderno.setText(objpassbook.getRemark());
            holder.tv_source.setText("Paid for Order");
            holder.tv_debitorcredit.setText("-");
            holder.tv_amount.setText(objpassbook.getDebit()+"");
        }
    }

    @Override
    public int getItemCount() {
        return passbook.size();
    }
    public static class PassbookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_source, tv_orderno, tv_wdate, tv_debitorcredit, tv_amount;
        ImageView img_paid_rec;
        public PassbookViewHolder(View view, Context ctx, ArrayList<Passbook> passbook, RecyclerView resourceIdd){
            super(view);
            img_paid_rec = (ImageView) view.findViewById(R.id.img_paid_rec) ;
            tv_source = (TextView) view.findViewById(R.id.tv_source);
            tv_orderno = (TextView) view.findViewById(R.id.tv_orderno);
            tv_wdate = (TextView) view.findViewById(R.id.tv_wdate);
            tv_debitorcredit = (TextView) view.findViewById(R.id.tv_debitorcredit);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
