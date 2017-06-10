package com.accelity.wow21;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.accelity.wow21.activities.NotificationListActivity;
import com.accelity.wow21.etc.DatabaseHandler;
import com.accelity.wow21.models.Notification;
import com.accelity.wow21.models.UserPref;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Rahul on 01-07-2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private UserPref userPref;
    private static final String TAG = "MyFirebaseMsgService";
    private String [] allStrings;
    private DatabaseHandler db;
    private Notification objNotify = null;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        userPref = new UserPref(this);
        db = new DatabaseHandler(this);
        //Displaying data in log
        //It is optional
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Title: " + remoteMessage.getNotification().getTitle());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //allStrings = remoteMessage.getNotification().getBody().split("##");
        //L1og.d(TAG, "Notification Message Icon: " + remoteMessage.getNotification().getIcon());
        //Log.d(TAG, "Notification Message Color: " + remoteMessage.getNotification().getColor());
        //Log.d(TAG, "Notification Message Tag: " + remoteMessage.getNotification().getTag());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().get);
        //Calling method to generate notification
        //sendNotification(remoteMessage.getNotification().getTitle(), allStrings[0], allStrings[2], allStrings[3], allStrings[1]);
        //Log.d("NotificationMSG Title", remoteMessage.getData().get("title")+"");
        //Log.d("NotificationMSG Desc", remoteMessage.getData().get("body")+"");
        //Log.d("NotificationMSG SrcNo ", remoteMessage.getData().get("SrcNo")+"");
        //Log.d("NotificationMSG icon ", remoteMessage.getData().get("icon")+"");
        //Log.d("NotificationMSG image ", remoteMessage.getData().get("image")+"");
        if(userPref.getUser(this).getLoggedIn()) {
            sendNotification(remoteMessage.getData().get("title"),
                    remoteMessage.getData().get("body"), remoteMessage.getData().get("icon"),
                    remoteMessage.getData().get("image"), remoteMessage.getData().get("SrcNo"));
        }
    }

    private void sendNotification(String title, String body, String sicon, String bimage, String screeno) {
        objNotify = new Notification();
        objNotify.setTitle(title);
        objNotify.setScreenId("0");
        if(screeno != null){
            objNotify.setScreenId(screeno);
        }

        objNotify.setIsClickable(false);
        if(screeno != null) {
            if (Integer.parseInt(screeno) > 0) {
                objNotify.setIsClickable(true);
            }
        }
        objNotify.setDate(System.currentTimeMillis()+"");
        objNotify.setDesc(body);
        objNotify.setLargeIMGURL(bimage);
        objNotify.setSmallIMGURL(sicon);
        db.insertNotification(objNotify);
        db.deleteAfter10();
        if(db.getNotificationUnRead() > 0){
            ShortcutBadger.applyCount(this, db.getNotificationUnRead());

        }
        //Log.d("Notification Count: - ", db.getNotificationCount()+"    -");
        Intent intent = new Intent(this, NotificationListActivity.class);
        intent.putExtra("fromhome", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap obh = getBitmapFromURL(bimage);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bmp = getBitmapFromURL(sicon);
        if(bimage != null && bimage.length()>0){
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_statusbar)
                    .setLargeIcon(bmp)
                    .setWhen(System.currentTimeMillis())
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(obh).setSummaryText(body))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }else{
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setSmallIcon(R.drawable.ic_statusbar)
                    .setLargeIcon(bmp)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }

    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}