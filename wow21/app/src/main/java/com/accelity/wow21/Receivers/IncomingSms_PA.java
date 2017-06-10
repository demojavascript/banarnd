package com.accelity.wow21.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.accelity.wow21.activities.ProfileActivity;

;

/**
 * Created by Rahul on 31-08-2016.
 */
public class IncomingSms_PA extends BroadcastReceiver  {
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                if(address.indexOf("WOW") > -1){
                    smsMessageStr = smsBody.split(" ")[smsBody.split(" ").length - 1];
                }
            }
            try{
                ProfileActivity inst1 = ProfileActivity.instance();
                inst1.updateOTP(smsMessageStr);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}