package com.nitinguru.app.nitinguru.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nitinguru.app.nitinguru.models.DBNotes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 28-05-2017.
 */

public class NotesDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns = { MySQLiteHelper.COLUMN_NOTES_ID,
            MySQLiteHelper.COLUMN_NOTES_TITLE, MySQLiteHelper.COLUMN_NOTES_TOPICID, MySQLiteHelper.COLUMN_NOTES_DESC };

    public NotesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }

    public DBNotes createNotes(DBNotes notes) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NOTES_TITLE, notes.getTitle());
        values.put(MySQLiteHelper.COLUMN_NOTES_TOPICID, notes.getTopicid());
        values.put(MySQLiteHelper.COLUMN_NOTES_DESC, notes.getDesc());
        long insertId = database.insert(MySQLiteHelper.TABLE_NOTES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,
                allColumns, MySQLiteHelper.COLUMN_NOTES_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBNotes newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;
    }
    private DBNotes cursorToNote(Cursor cursor) {
        DBNotes note = new DBNotes();
        note.setId(cursor.getLong(0));
        note.setTitle(cursor.getString(1));
        note.setTopicid(cursor.getInt(2));
        note.setDesc(cursor.getString(3));
        return note;
    }
    public List<DBNotes> getAllNotes() {
        List<DBNotes> notes = new ArrayList<DBNotes>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TOPICS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DBNotes note = new DBNotes();
            note = cursorToNote(cursor);
            notes.add(note);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return notes;
    }

    public List<DBNotes> getAllNotesById(long topicid) {
        List<DBNotes> notes = new ArrayList<DBNotes>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,
                allColumns, MySQLiteHelper.COLUMN_NOTES_TOPICID + " = " + topicid, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DBNotes note = new DBNotes();
            note = cursorToNote(cursor);
            notes.add(note);
            cursor.moveToNext();
        }

        cursor.close();
        return notes;
    }


}
