package com.nitinguru.app.nitinguru.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nitinguru.app.nitinguru.models.DBTopic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 28-05-2017.
 */

public class TopicDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns = { MySQLiteHelper.COLUMN_TOPICS_ID,
            MySQLiteHelper.COLUMN_TOPICS_TITLE, MySQLiteHelper.COLUMN_TOPICS_VIEW, MySQLiteHelper.COLUMN_TOPICS_CHAPTERID };

    public TopicDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }

    public DBTopic createTopic(DBTopic topic) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TOPICS_TITLE, topic.getTitle());
        values.put(MySQLiteHelper.COLUMN_TOPICS_CHAPTERID, topic.getChapterid());
        values.put(MySQLiteHelper.COLUMN_TOPICS_VIEW, 0);
        long insertId = database.insert(MySQLiteHelper.TABLE_TOPICS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TOPICS,
                allColumns, MySQLiteHelper.COLUMN_TOPICS_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBTopic newTopic = cursorToTopic(cursor);
        cursor.close();
        return newTopic;
    }
    private DBTopic cursorToTopic(Cursor cursor) {
        DBTopic topic = new DBTopic();
        topic.setId(cursor.getLong(0));
        topic.setTitle(cursor.getString(1));
        topic.setView(cursor.getInt(2));
        topic.setChapterid(cursor.getInt(3));
        return topic;
    }
    public boolean updateTopic(long topicid) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TOPICS_VIEW, 1);
        database.update(MySQLiteHelper.TABLE_TOPICS, values, MySQLiteHelper.COLUMN_TOPICS_ID+" = ?",new String[] { topicid+"" });
        return true;
    }
    public void deleteTpic(DBTopic topic) {
        long id = topic.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_TOPICS, MySQLiteHelper.COLUMN_TOPICS_ID
                + " = " + id, null);
    }

    public List<DBTopic> getAllTopic() {
        List<DBTopic> topics = new ArrayList<DBTopic>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TOPICS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DBTopic chapter = new DBTopic();
            chapter = cursorToTopic(cursor);
            topics.add(chapter);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return topics;
    }

    public List<DBTopic> getAllTopicById(long chpterid) {
        List<DBTopic> topics = new ArrayList<DBTopic>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TOPICS,
                allColumns, MySQLiteHelper.COLUMN_TOPICS_CHAPTERID + " = " + chpterid, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DBTopic chapter = new DBTopic();
            chapter = cursorToTopic(cursor);
            topics.add(chapter);
            cursor.moveToNext();
        }

        cursor.close();
        return topics;
    }

    public DBTopic getTopicd(long topicid) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TOPICS,
                allColumns, MySQLiteHelper.COLUMN_TOPICS_ID + " = " + topicid, null,
                null, null, null);

        cursor.moveToFirst();
        DBTopic newTopic = cursorToTopic(cursor);
        cursor.close();
        return newTopic;
    }
}
