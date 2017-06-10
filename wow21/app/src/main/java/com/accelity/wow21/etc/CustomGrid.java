package com.accelity.wow21.etc;

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

import com.accelity.wow21.R;
import com.accelity.wow21.models.HomeDeal;

import java.util.ArrayList;

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private ArrayList<HomeDeal> deal;

    public CustomGrid(Context c, ArrayList<HomeDeal> deal) {
        mContext = c;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_product_column, null);
            TextView tvdealname = (TextView) grid.findViewById(R.id.grid_deal_name);
            TextView tvdealmrp1 = (TextView) grid.findViewById(R.id.grid_dealmrp);
            TextView tvdealmrp2 = (TextView) grid.findViewById(R.id.grid_dealmrp2);
            TextView tvdealsold = (TextView) grid.findViewById(R.id.grid_sold);
            TextView tvdealla = (TextView) grid.findViewById(R.id.grid_leftall);
            //TextView tvdealoff = (TextView) grid.findViewById(R.id.grid_deal_off);
            ImageView imgdealimg = (ImageView) grid.findViewById(R.id.grid_deal_image);

            HomeDeal currdeal = deal.get(position);
            String totalaleft = currdeal.getStockAvailable()+"/"+currdeal.getDealTotalQty();
            tvdealname.setText(currdeal.getVariantTitle());
            tvdealmrp1.setText(currdeal.getMrp().toString());
            tvdealmrp2.setText(currdeal.getSellingPrice().toString());
            tvdealla.setText(totalaleft);
            tvdealsold.setText("Sold");
            if (currdeal.getStockAvailable() == 0) {
                tvdealsold.setText("Out of Stock");
            }
            new ImageDownloaderTask(imgdealimg).execute(currdeal.getDealIcon());


        } else {
            grid = (View) convertView;
        }
        Log.d("grid", position+"");
        return grid;
    }
}