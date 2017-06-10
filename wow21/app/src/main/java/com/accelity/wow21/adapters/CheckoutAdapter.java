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
import com.accelity.wow21.etc.ImageDownloaderTask;
import com.accelity.wow21.models.CartData;
import com.accelity.wow21.utilities.CustMessages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rahul on 08-07-2016.
 */
public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.DealListViewHolder> {
    ArrayList<CartData> deals = new ArrayList<CartData>();
    Context ctx;
    RecyclerView resourceIdd;
    TextView tv_tamount, tv_tQty, tv_tSavings;
    int objlayout;
    public CheckoutAdapter(ArrayList<CartData> deals, Context ctx, RecyclerView resourceIdd, TextView tv_tamount, TextView tv_tQty, TextView tv_tSavings, int objlayout){
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
        CartData hdeal = deals.get(position);
        holder.tv_dealdiscount.setText(hdeal.getDealDiscount());
        holder.tv_dealtitle.setText(hdeal.getVariantTitle());
        holder.tv_dealsell.setText(hdeal.getSellingPrice().toString());
        holder.tv_dealmrp.setText(hdeal.getMrp().toString());
        holder.tv_dealQty.setText(hdeal.getMineQty()+"");
        holder.tv_dealmrp.setPaintFlags(holder.tv_dealmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if(hdeal.getDealIcon() != null){
            Picasso.with(ctx).load(hdeal.getDealIcon()).into(holder.imgDeal);
        }
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }
    public static class DealListViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        CardView cardView;
        TextView tv_dealtitle, tv_dealdiscount, tv_dealmrp, tv_dealsell, tv_dealQty;
        ImageView imgDeal, btn_addition, btn_subtraction, btn_sealBtn;
        CartData objDeal;
        private static Toast tincr = null;
        int currPos = 0;
        int layoutt;
        ArrayList<CartData> Deals;
        TextView tv_tamount, tv_tQty, tv_tSavings, tv_rs1, tv_rs2, dealdiscountOff;
        Context ctx;
        RecyclerView resourceIdd;
        public DealListViewHolder(View view, Context ctx, ArrayList<CartData> Deals, RecyclerView resourceIdd, TextView tv_tamount, TextView tv_tQty, TextView tv_tSavings,int layoutt){
            super(view);
            this.resourceIdd = resourceIdd;
            this.layoutt = layoutt;
            this.Deals = Deals;
            this.ctx = ctx;

            this.tv_rs1 = (TextView)view.findViewById(R.id.tv_rs1);
            this.tv_rs2 = (TextView)view.findViewById(R.id.tv_rs2);
            this.dealdiscountOff = (TextView)view.findViewById(R.id.dealdiscountOff);

            this.tv_tamount = tv_tamount;
            this.tv_tQty = tv_tQty;
            this.tv_tSavings = tv_tSavings;
            this.tv_dealtitle = (TextView)view.findViewById(R.id.dealtitle);
            this.tv_dealdiscount = (TextView)view.findViewById(R.id.dealdiscount);
            this.dealdiscountOff = (TextView)view.findViewById(R.id.dealdiscountOff);
            this.tv_dealmrp = (TextView)view.findViewById(R.id.dealMRPprice);
            this.tv_dealsell = (TextView)view.findViewById(R.id.dealSellingprice);
            this.tv_dealQty = (TextView)view.findViewById(R.id.tv_dealQty);
            this.imgDeal = (ImageView) view.findViewById(R.id.dealimgDeal);
            this.btn_addition = (ImageView) view.findViewById(R.id.btn_addition);
            this.btn_subtraction = (ImageView) view.findViewById(R.id.btn_subtract);
            this.btn_sealBtn = (ImageView) view.findViewById(R.id.btn_sealBtn);
            if(layoutt == R.layout.row_checkoutdeallist){
                this.btn_addition.setOnClickListener(this);
                this.btn_subtraction.setOnClickListener(this);
            }
            this.btn_sealBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currPos = getAdapterPosition();
            objDeal = Deals.get(currPos);

            switch(v.getId()){
                case R.id.btn_sealBtn:
                    infoBox(getAdapterPosition());
                    break;
                case R.id.btn_addition:
                    if (tincr != null) {
                        tincr.cancel();
                    }
                    additionOperation();
                    break;
                case R.id.btn_subtract:
                    if (tincr != null) {
                        tincr.cancel();
                    }
                    subtractOperation();
                    break;
            }
        }
        public void infoBox(int pos){
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder
                    .setMessage("Minimum Order Amount to unlock this Deal is Rs "+objDeal.getSuperDealMinAmount())
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        public void subtractOperation(){
            int productQty = Integer.parseInt(tv_dealQty.getText().toString());
            if (productQty > 0) {
                Double disTot2 = 0.0;
                --productQty;
                if(productQty < 1){
                    tv_dealQty.setVisibility(View.GONE);
                    btn_subtraction.setVisibility(View.GONE);
                }
                tv_dealQty.setText(String.valueOf(productQty));
                Deals.get(currPos).setDealQty(productQty);
                Double tAmount = 0.0;
                Integer tQty = 0;
                Double tDAmount = 0.0;
                for (int k = 0; k < Deals.size(); k++) {
                    CartData objndeal = Deals.get(k);
                    if (objndeal.getDealQty() > 0) {
                        tAmount = tAmount + objndeal.getDealQty() * Double.parseDouble(objndeal.getSellingPrice().toString());
                        tQty = tQty + objndeal.getDealQty();
                        disTot2 = disTot2 + objndeal.getDealQty() * (objndeal.getMrp() - objndeal.getSellingPrice());
                        if(!objndeal.getIsSuper()){
                            tDAmount = tDAmount + objndeal.getDealQty() * Double.parseDouble(objndeal.getSellingPrice().toString());
                        }
                    }
                }

                for (int k = 0; k < Deals.size(); k++) {
                    if(Deals.get(k).getIsSuper()) {
                        ImageView bnh1 = (ImageView) resourceIdd.getChildAt(k).findViewById(R.id.btn_addition);
                        ImageView bnh2 = (ImageView) resourceIdd.getChildAt(k).findViewById(R.id.btn_subtract);
                        ImageView bnh4 = (ImageView) resourceIdd.getChildAt(k).findViewById(R.id.btn_sealBtn);
                        TextView bnh3 = (TextView) resourceIdd.getChildAt(k).findViewById(R.id.tv_dealQty);

                        TextView bnh55  = (TextView)resourceIdd.findViewById(R.id.tv_rs1);
                        TextView bnh56 = (TextView)resourceIdd.findViewById(R.id.tv_rs2);
                        TextView bnh57 = (TextView)resourceIdd.findViewById(R.id.dealdiscountOff);

                        TextView bnh58 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.tv_dealtxt);
                        TextView bnh59 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.dealtitle);
                        TextView bnh60 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.dealdiscount);
                        TextView bnh61 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.dealMRPprice);
                        TextView bnh62 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.dealSellingprice);
                        TextView bnh63 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.tv_dealQty);
                        ImageView bnh64 = (ImageView) resourceIdd.getChildAt(k).findViewById(R.id.dealimgDeal);

                        if(tDAmount < Deals.get(k).getSuperDealMinAmount()){
                            bnh1.setVisibility(View.GONE);
                            bnh2.setVisibility(View.GONE);
                            bnh4.setVisibility(View.VISIBLE);
                            bnh56.setAlpha(Float.parseFloat("0.4"));
                            bnh57.setAlpha(Float.parseFloat("0.4"));
                            bnh58.setAlpha(Float.parseFloat("0.4"));
                            bnh59.setAlpha(Float.parseFloat("0.4"));
                            bnh60.setAlpha(Float.parseFloat("0.4"));
                            bnh61.setAlpha(Float.parseFloat("0.4"));
                            bnh62.setAlpha(Float.parseFloat("0.4"));
                            bnh63.setAlpha(Float.parseFloat("0.4"));
                            Deals.get(k).setDealQty(0);
                            productQty = productQty - Integer.parseInt(bnh3.getText().toString());
                            tAmount = tAmount - (Integer.parseInt(bnh3.getText().toString()) * Deals.get(k).getSellingPrice());
                            disTot2 = disTot2 - (Integer.parseInt(bnh3.getText().toString()) * (Deals.get(k).getMrp() - Deals.get(k).getSellingPrice()));
                            tQty = tQty - Integer.parseInt(bnh3.getText().toString());
                            bnh3.setText("0");
                            bnh3.setVisibility(View.GONE);
                        }
                    }
                }

                String ght = String.format("%.2f", Double.parseDouble(tAmount.toString()));
                String ghtt = String.format("%.2f", Double.parseDouble(disTot2.toString()));
                tv_tamount.setText(String.valueOf(ght));
                tv_tQty.setText(tQty.toString());
                tv_tSavings.setText(String.valueOf(ghtt));
            }else{

            }
        }
        public void additionOperation() {
            int productQty = Integer.parseInt(tv_dealQty.getText().toString());
            if ((objDeal.getStockAvailable() != 0) && (productQty < objDeal.getStockAvailable())) {
                if (productQty < objDeal.getMaxPerCustomer()) {
                    Double disTotl = 0.0;
                    productQty++;
                    tv_dealQty.setVisibility(View.VISIBLE);
                    btn_subtraction.setVisibility(View.VISIBLE);
                    tv_dealQty.setText(String.valueOf(productQty));
                    Deals.get(currPos).setDealQty(productQty);
                    Double tAmount = 0.0;
                    Integer tQty = 0;
                    for (int k = 0; k < Deals.size(); k++) {
                        CartData objndeal = Deals.get(k);
                        if (objndeal.getDealQty() > 0) {
                            tAmount = tAmount + objndeal.getDealQty() * Double.parseDouble(objndeal.getSellingPrice().toString());
                            tQty = tQty + objndeal.getDealQty();
                            disTotl = disTotl + objndeal.getDealQty() * (objndeal.getMrp() - objndeal.getSellingPrice());
                        }
                    }
                    String ght = String.format("%.2f", Double.parseDouble(tAmount.toString()));
                    String ghtt = String.format("%.2f", Double.parseDouble(disTotl.toString()));
                    tv_tamount.setText(String.valueOf(ght));
                    tv_tQty.setText(tQty.toString());
                    tv_tSavings.setText(String.valueOf(ghtt));
                    for (int k = 0; k < Deals.size(); k++) {
                        if(Deals.get(k).getIsSuper()) {
                            ImageView bnh2 = (ImageView) resourceIdd.getChildAt(k).findViewById(R.id.btn_subtract);
                            TextView bnh3 = (TextView) resourceIdd.getChildAt(k).findViewById(R.id.tv_dealQty);
                            ImageView bnh22 = (ImageView) resourceIdd.getChildAt(k).findViewById(R.id.btn_sealBtn);
                            ImageView bnh1 = (ImageView) resourceIdd.getChildAt(k).findViewById(R.id.btn_addition);

                            TextView bnh55  = (TextView)resourceIdd.findViewById(R.id.tv_rs1);
                            TextView bnh56 = (TextView)resourceIdd.findViewById(R.id.tv_rs2);
                            TextView bnh57 = (TextView)resourceIdd.findViewById(R.id.dealdiscountOff);

                            TextView bnh58 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.tv_dealtxt);
                            TextView bnh59 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.dealtitle);
                            TextView bnh60 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.dealdiscount);
                            TextView bnh61 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.dealMRPprice);
                            TextView bnh62 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.dealSellingprice);
                            TextView bnh63 = (TextView)resourceIdd.getChildAt(k).findViewById(R.id.tv_dealQty);
                            ImageView bnh64 = (ImageView) resourceIdd.getChildAt(k).findViewById(R.id.dealimgDeal);

                            if(Double.parseDouble(tAmount.toString()) >= Deals.get(k).getSuperDealMinAmount()){
                                bnh1.setVisibility(View.VISIBLE);
                                bnh2.setVisibility(View.VISIBLE);
                                bnh3.setVisibility(View.VISIBLE);
                                bnh22.setVisibility(View.GONE);


                                bnh56.setAlpha(1);
                                bnh57.setAlpha(1);
                                bnh58.setAlpha(1);
                                bnh59.setAlpha(1);
                                bnh60.setAlpha(1);
                                bnh61.setAlpha(1);
                                bnh62.setAlpha(1);
                                bnh63.setAlpha(1);
                            }
                        }
                    }
                }else{
                    if (tincr != null) {
                        tincr.cancel();
                    }
                    tincr = Toast.makeText(ctx, CustMessages.getNoMoreDeal(ctx), Toast.LENGTH_LONG);
                    tincr.show();
                }
            }else{
                if (tincr != null) {
                    tincr.cancel();
                }
                tincr = Toast.makeText(ctx, CustMessages.getOutOfStockText(ctx), Toast.LENGTH_LONG);
                tincr.show();
            }
        }
    }
}
