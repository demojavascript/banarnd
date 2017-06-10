package com.nitinguru.app.nitinguru.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nitinguru.app.nitinguru.models.DBChapter;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Rahul on 28-05-2017.
 */

public class ChapterDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns = { MySQLiteHelper.COLUMN_CHAPTER_ID,
            MySQLiteHelper.COLUMN_CHAPTER_TITLE, MySQLiteHelper.COLUMN_CHAPTER_TOPICCOUNT, MySQLiteHelper.COLUMN_CHAPTER_VISTTS };

    public ChapterDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public DBChapter createChapter(DBChapter chapter) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CHAPTER_TITLE, chapter.getTitle());
        values.put(MySQLiteHelper.COLUMN_CHAPTER_TOPICCOUNT, chapter.getTopics());
        values.put(MySQLiteHelper.COLUMN_CHAPTER_VISTTS, 0);
        long insertId = database.insert(MySQLiteHelper.TABLE_CHAPTER, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHAPTER,
                allColumns, MySQLiteHelper.COLUMN_CHAPTER_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBChapter newChapter = cursorToChapter(cursor);
        cursor.close();
        return newChapter;
    }

    private DBChapter cursorToChapter(Cursor cursor) {
        DBChapter chapter = new DBChapter();
        chapter.setId(cursor.getLong(0));
        chapter.setTitle(cursor.getString(1));
        chapter.setTopics(cursor.getInt(2));
        chapter.setVisits(cursor.getInt(3));
        return chapter;
    }
    public boolean updateChapter(int visits, long id) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CHAPTER_VISTTS, visits);
        database.update(MySQLiteHelper.TABLE_CHAPTER, values, MySQLiteHelper.COLUMN_CHAPTER_ID+" = ?",new String[] { id+"" });
        return true;
    }
    public void deleteChapters(DBChapter chapter) {
        long id = chapter.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_CHAPTER, MySQLiteHelper.COLUMN_CHAPTER_ID
                + " = " + id, null);
    }

    public List<DBChapter> getAllChapter() {
        List<DBChapter> chapters = new ArrayList<DBChapter>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHAPTER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DBChapter chapter = new DBChapter();
            chapter = cursorToChapter(cursor);
            chapters.add(chapter);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return chapters;
    }
    public DBChapter getChapter(long chapterid) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CHAPTER,
                allColumns, MySQLiteHelper.COLUMN_CHAPTER_ID + " = " + chapterid, null,
                null, null, null);

        cursor.moveToFirst();
        DBChapter newChapter = cursorToChapter(cursor);
        cursor.close();
        return newChapter;
    }
}
