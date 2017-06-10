package com.accelity.wow21.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accelity.wow21.R;
import com.accelity.wow21.activities.HomeActivity;
import com.accelity.wow21.models.HomeDeal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rahul on 16-08-2016.
 */
public class HorizontalDealAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<HomeDeal> deal;

    public HorizontalDealAdapter(Context ctx, ArrayList<HomeDeal> deal) {
        this.ctx = ctx;
        this.deal = deal;
    }

    @Override
    public int getCount() {
        return deal.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void clearAdapter(){
        deal.clear();
    }

    public void addNewValues(ArrayList<HomeDeal> deal){
        this.deal = deal;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(ctx);
            grid = inflater.inflate(R.layout.row_hv_deal_view, null);
            LinearLayout loutSuper = (LinearLayout) grid.findViewById(R.id.loutSuper);
            CardView cardView = (CardView) grid.findViewById(R.id.cardview_row_deal);

            if(position == (deal.size()-1)){
                grid = inflater.inflate(R.layout.row_hv_deal_view_blank, null);
                cardView = (CardView) grid.findViewById(R.id.cardview_row_deal);
            }else {

                HomeDeal hdeal = deal.get(position);
                TextView tv_dealdesc = (TextView) grid.findViewById(R.id.tv_dealdesc);
                TextView tv_dealoff = (TextView) grid.findViewById(R.id.tv_dealoff);
                TextView tv_soldtext = (TextView) grid.findViewById(R.id.tv_soldtext);

                TextView tv_mrp = (TextView) grid.findViewById(R.id.grid_dealmrp);
                TextView tv_mrp2 = (TextView) grid.findViewById(R.id.grid_dealmrp2);
                ImageView imgDeal = (ImageView) grid.findViewById(R.id.img_dealimg);

                View perView5 = (View) grid.findViewById(R.id.perView5);
                View perView4 = (View) grid.findViewById(R.id.perView4);
                View perView3 = (View) grid.findViewById(R.id.perView3);
                View perView2 = (View) grid.findViewById(R.id.perView2);
                View perView1 = (View) grid.findViewById(R.id.perView1);


                tv_dealoff.setText(hdeal.getDealDiscount());
                tv_dealdesc.setText(hdeal.getVariantTitle());

                tv_mrp.setText(hdeal.getSellingPrice() + "");
                tv_mrp2.setText(hdeal.getMrp() + "");

                tv_mrp2.setPaintFlags(tv_mrp2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tv_soldtext.setText(hdeal.getStockDetail().getText());
                loutSuper.setVisibility(View.INVISIBLE);


                if (Integer.parseInt(hdeal.getStockDetail().getPercentagesold()) <= 20) {
                    perView1.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView2.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView3.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView4.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView5.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                } else if (Integer.parseInt(hdeal.getStockDetail().getPercentagesold()) > 20 && Integer.parseInt(hdeal.getStockDetail().getPercentagesold()) <= 40) {
                    perView1.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView2.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView3.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView4.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView5.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                } else if (Integer.parseInt(hdeal.getStockDetail().getPercentagesold()) > 40 && Integer.parseInt(hdeal.getStockDetail().getPercentagesold()) <= 60) {
                    perView1.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView2.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView3.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView4.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView5.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                } else if (Integer.parseInt(hdeal.getStockDetail().getPercentagesold()) > 60 && Integer.parseInt(hdeal.getStockDetail().getPercentagesold()) <= 80) {
                    perView1.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView2.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView3.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView4.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                    perView5.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                } else if (Integer.parseInt(hdeal.getStockDetail().getPercentagesold()) > 80 && Integer.parseInt(hdeal.getStockDetail().getPercentagesold()) < 100) {
                    perView1.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView2.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView3.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView4.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView5.setBackground(ctx.getResources().getDrawable(R.drawable.deal_bbox));
                } else {
                    perView1.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView2.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView3.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView4.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                    perView5.setBackgroundColor(Color.parseColor(hdeal.getStockDetail().getColour()));
                }
                if (hdeal.getDealIcon() != null) {
                    Picasso.with(ctx).load(hdeal.getDealIcon()).into(imgDeal);
                }
                if (hdeal.getIsSuper()) {
                    loutSuper.setVisibility(View.VISIBLE);
                }
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent intent = new Intent(ctx, DealListActivity.class);
                    //ctx.startActivity(intent);
                    HomeActivity homeActivity = HomeActivity.instance();
                    homeActivity.shopNow();
                }
            });

        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}