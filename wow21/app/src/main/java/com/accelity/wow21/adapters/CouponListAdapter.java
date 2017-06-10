package com.accelity.wow21.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.accelity.wow21.R;
import com.accelity.wow21.activities.CheckoutActivity;
import com.accelity.wow21.models.Coupon;
import com.accelity.wow21.utilities.CustMessages;

import java.util.ArrayList;

/**
 * Created by Rahul on 06-09-2016.
 */
public class CouponListAdapter extends ArrayAdapter<Coupon> {
    Context context;
    int resource;
    ArrayList<Coupon> objects;
    ListView lv_couponlist;
    public CouponListAdapter(Context context, int resource, ArrayList<Coupon> objects, ListView lv_couponlist) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.lv_couponlist = lv_couponlist;
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_coupon_list, parent, false);
        if(!objects.get(position).getCanapplicable()){
            row = inflater.inflate(R.layout.row_coupon_list_no, parent, false);
        }
        RadioButton btn_RD = (RadioButton) row.findViewById(R.id.rd_btn);
        btn_RD.setEnabled(false);
        LinearLayout lout_topp = (LinearLayout) row.findViewById(R.id.lout_topp);

        //if(objects.get(position).getCanapplicable()) {
            btn_RD.setEnabled(true);
            btn_RD.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(objects.get(position).getCanapplicable()) {
                        try {
                            for (Integer k = 0; k < objects.size(); k++) {
                                RadioButton rbBtn = (RadioButton) lv_couponlist.getChildAt(k).findViewById(R.id.rd_btn);
                                rbBtn.setChecked(false);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        RadioButton rbBtn = (RadioButton) v;
                        rbBtn.setChecked(true);
                        CheckoutActivity inst1 = CheckoutActivity.instance();
                        inst1.updateKey("" + objects.get(position).getCouponCode());
                    }else{
                        RadioButton rbBtn = (RadioButton) v;
                        rbBtn.setChecked(false);
                        Toast.makeText(context, context.getResources().getString(R.string.paymentoption_minto_avail_coupon2)+" "+objects.get(position).getMinOrderAmount(), Toast.LENGTH_LONG).show();

                        //Toast.makeText(context, CustMessages.getOutOfStockText(context)+objects.get(position).getMinOrderAmount(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        //}


        TextView tv_couponcode = (TextView) row.findViewById(R.id.tv_couponcode);
        tv_couponcode.setText(objects.get(position).getCouponCode());
        TextView tv_couponAmnt = (TextView) row.findViewById(R.id.tv_couponAmnt);
        tv_couponAmnt.setText(objects.get(position).getMaxDiscount()+"");
        TextView tv_couponAmntPer = (TextView) row.findViewById(R.id.tv_couponAmntPer);
        tv_couponAmntPer.setText(objects.get(position).getDesc());

        if(!objects.get(position).getCanapplicable()){

        }

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(objects.get(position).getCanapplicable()) {
                    try {
                        for (Integer k = 0; k < objects.size(); k++) {
                            RadioButton rbBtn = (RadioButton) lv_couponlist.getChildAt(k).findViewById(R.id.rd_btn);
                            rbBtn.setChecked(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    RadioButton rbBtn = (RadioButton) v.findViewById(R.id.rd_btn);
                    rbBtn.setChecked(true);
                    CheckoutActivity inst1 = CheckoutActivity.instance();
                    inst1.updateKey("" + objects.get(position).getCouponCode());
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.paymentoption_minto_avail_coupon2)+" "+objects.get(position).getMinOrderAmount(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return row;
    }
}