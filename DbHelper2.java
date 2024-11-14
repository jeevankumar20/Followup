package com.example.newapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper2 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reminders.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_REMINDER = "REMINDER_TABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "NAME";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_REMINDER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL);";

    public DbHelper2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);

        onCreate(db);
    }

    public long insertReminderContact(String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.C_NAME, name);

        long id = db.insert(Constants.TABLE_NAME_TWO, null, contentValues);

        db.close();

        return id;
    }

    public ArrayList<ModelReminder> getAllReminder() {

        ArrayList<ModelReminder> arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME_TWO;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                ModelReminder modelReminder = new ModelReminder(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)));

                arrayList.add(modelReminder);

            }while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }

}
