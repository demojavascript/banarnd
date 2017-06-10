package com.accelity.wow21.etc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.accelity.wow21.models.Notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Rahul on 22-05-2016.
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "notiDB";
    // Contacts table name
    private static final String TABLE_NOTIFY = "notifications";
    // Contacts Table Columns names
    private static final String KEY_NOTIFYID = "notifyid";
    private static final String KEY_NOTIFYTITLE = "notifytitle";
    private static final String KEY_NOTIFYDESC = "notifydesc";
    private static final String KEY_NOTIFYLIMGURL = "notifylimgurl";
    private static final String KEY_NOTIFYSIMGURL = "notifysimgurl";
    private static final String KEY_NOTIFYISCLICKABLE = "notifyisclickable";
    private static final String KEY_NOTIFYSCREENID = "screenid";
    private static final String KEY_NOTIFYWDATE = "notifywdate";
    private static final String KEY_NOTIFYSYNC = "notifysync";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTIFY_TABLE = "CREATE TABLE " + TABLE_NOTIFY + "("
                + KEY_NOTIFYID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOTIFYTITLE + " TEXT,"
                + KEY_NOTIFYDESC + " TEXT,"
                + KEY_NOTIFYLIMGURL + " TEXT,"
                + KEY_NOTIFYSIMGURL + " TEXT,"
                + KEY_NOTIFYISCLICKABLE + " TEXT,"
                + KEY_NOTIFYSCREENID + " TEXT,"
                + KEY_NOTIFYWDATE + " TEXT,"
                + KEY_NOTIFYSYNC + " TEXT" + ")";
        db.execSQL(CREATE_NOTIFY_TABLE);
    }

    public void insertNotification(Notification objNotify) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
            String formattedDate = df.format(c.getTime());

            values.put(KEY_NOTIFYTITLE, objNotify.getTitle());
            values.put(KEY_NOTIFYDESC, objNotify.getDesc());
            values.put(KEY_NOTIFYLIMGURL, objNotify.getLargeIMGURL());
            values.put(KEY_NOTIFYSIMGURL, objNotify.getSmallIMGURL());
            values.put(KEY_NOTIFYISCLICKABLE, "0");
            if(objNotify.getIsClickable()) {
                values.put(KEY_NOTIFYISCLICKABLE, "1");
            }
            values.put(KEY_NOTIFYSCREENID, objNotify.getScreenId());
            values.put(KEY_NOTIFYWDATE, formattedDate);
            values.put(KEY_NOTIFYSYNC, "1");
            db.insertWithOnConflict(TABLE_NOTIFY, null, values,  SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Notification> getAllNotification() {
        ArrayList<Notification> notificationList = new ArrayList<Notification>();
        String selectQuery = "SELECT  notifyid, notifytitle, notifydesc, notifylimgurl, notifysimgurl, notifyisclickable, screenid, notifywdate  FROM " + TABLE_NOTIFY +" ORDER BY "+KEY_NOTIFYID+"  DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Notification objNotify = new Notification();
                objNotify.setId(cursor.getString(0));
                objNotify.setTitle(cursor.getString(1));
                objNotify.setDesc(cursor.getString(2));
                objNotify.setLargeIMGURL(cursor.getString(3));
                objNotify.setSmallIMGURL(cursor.getString(4));
                if(Integer.parseInt(cursor.getString(5)) > 0) {
                    objNotify.setIsClickable(true);
                }else{
                    objNotify.setIsClickable(false);
                }
                objNotify.setScreenId(cursor.getString(6));
                objNotify.setDate(cursor.getString(7));
                notificationList.add(objNotify);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notificationList;
    }

    public int getNotificationCount() {
        int hhh = 0;
        try {
            String countQuery = "SELECT  * FROM " + TABLE_NOTIFY;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            hhh = cursor.getCount();
            cursor.close();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return hhh;
    }

    public int getNotificationUnRead() {
        int hhh = 0;
        if(getNotificationCount()>0) {
            String countQuery = "SELECT  * FROM " + TABLE_NOTIFY + " WHERE " + KEY_NOTIFYSYNC + " = 1";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            hhh = cursor.getCount();
            cursor.close();
            db.close();
        }
        return hhh;
    }
    public int syncNotification() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTIFYSYNC, "0");
        return db.update(TABLE_NOTIFY, values, KEY_NOTIFYSYNC + " = ?", new String[] { String.valueOf("1") });
    }

    public void deleteNotification() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFY, null, null);
        db.close();
    }

    public void deleteAfter10() {
        SQLiteDatabase db = this.getWritableDatabase();
        int totalrows = getAllNotification().size();
        try {
            if (totalrows > 10) {
                totalrows = totalrows - 10;
                String query = "DELETE FROM " + TABLE_NOTIFY + " WHERE " + KEY_NOTIFYID + " IN (SELECT " + KEY_NOTIFYID + " FROM " + TABLE_NOTIFY + " ORDER BY " + KEY_NOTIFYID + " ASC LIMIT " + totalrows + ")";
                db.execSQL(query); //delete all rows in a table
                //db.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERDETAILS);
        // Create tables again
        //onCreate(db);
    }
}
