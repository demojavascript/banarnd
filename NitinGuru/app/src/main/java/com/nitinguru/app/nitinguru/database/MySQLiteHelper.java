package com.nitinguru.app.nitinguru.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Rahul on 28-05-2017.
 */
 
class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_CHAPTER = "chapters";
    public static final String COLUMN_CHAPTER_ID = "_id";
    public static final String COLUMN_CHAPTER_TITLE = "_title";
    public static final String COLUMN_CHAPTER_TOPICCOUNT = "_topics";
    public static final String COLUMN_CHAPTER_VISTTS = "_visits";

    public static final String TABLE_TOPICS = "topics";
    public static final String COLUMN_TOPICS_ID = "_id";
    public static final String COLUMN_TOPICS_CHAPTERID = "_chapterid";
    public static final String COLUMN_TOPICS_TITLE = "_title";
    public static final String COLUMN_TOPICS_VIEW = "_view";

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_NOTES_ID = "_id";
    public static final String COLUMN_NOTES_TOPICID = "_topicid";
    public static final String COLUMN_NOTES_TITLE = "_title";
    public static final String COLUMN_NOTES_DESC = "_desc";

    private static final String DATABASE_NAME = "nitinguru.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CHAPTER + "( " + COLUMN_CHAPTER_ID
            + " integer primary key autoincrement, " + COLUMN_CHAPTER_TITLE
            + " text, " + COLUMN_CHAPTER_TOPICCOUNT
            + " integer, " + COLUMN_CHAPTER_VISTTS
            + " integer);";

    private static final String DATABASE_CREATE_TOPICS = "create table "
            + TABLE_TOPICS + "( " + COLUMN_TOPICS_ID
            + " integer primary key autoincrement, " + COLUMN_TOPICS_TITLE
            + " text, " + COLUMN_TOPICS_VIEW
            + " integer, " + COLUMN_TOPICS_CHAPTERID
            + " integer);";

    private static final String DATABASE_CREATE_NOTES = "create table "
            + TABLE_NOTES + "( " + COLUMN_NOTES_ID
            + " integer primary key autoincrement, " + COLUMN_NOTES_TITLE
            + " text, " + COLUMN_NOTES_TOPICID
            + " integer, " + COLUMN_NOTES_DESC
            + " text);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
        sqLiteDatabase.execSQL(DATABASE_CREATE_TOPICS);
        sqLiteDatabase.execSQL(DATABASE_CREATE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAPTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
}
