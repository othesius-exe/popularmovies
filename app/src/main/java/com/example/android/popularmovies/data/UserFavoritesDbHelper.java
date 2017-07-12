package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.UserFavoritesContract.FavoritesEntry;

/**
 *
 */

public class UserFavoritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int VERSION = 1;

    UserFavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " ("
                + FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_RELEASE_INFO + " DATE NOT NULL, "
                + FavoritesEntry.COLUMN_RATING + " DOUBLE NOT NULL, "
                + FavoritesEntry.COLUMN_SUMMARY + " STRING NOT NULL, "
                + FavoritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL" + ");";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
