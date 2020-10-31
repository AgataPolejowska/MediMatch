package com.i.medimatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String MED_TABLE = "MED_TABLE";
    public static final String MED_ID = "ID";
    public static final String MED_NAME = "MED_NAME";
    public static final String MED_FUNCTION = "MED_FUNCTION";


    public DataBaseHelper(@Nullable Context context) {
        super(context, "Medications.db", null, 1);
    }

    // Called the first time a database is accessed
    // Code to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + MED_TABLE + " (" + MED_ID + " INTERGER PRIMARY KEY AUTOINCREMENT, " + MED_NAME + " TEXT, " + MED_FUNCTION + " TEXT)";

        db.execSQL(createTableStatement);
    }


    // Called if the database version number changes.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(MedicationCard medicationCard) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MED_NAME, medicationCard.getName());
        cv.put(MED_FUNCTION, "function");

        long insert = db.insert(MED_TABLE, null, cv);

        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }

    }
}
