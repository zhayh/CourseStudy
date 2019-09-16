package edu.niit.android.course.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "course.db";
    public static final int DB_VERSION = 1;
    public static final String TBL_NAME_USER = "tb_user";
    public static final String TBL_USER = "create table if not exists " +
            TBL_NAME_USER +
            "(_id integer primary key autoincrement, " +
            "user_name varchar, " +
            "nick_name varchar, " +
            "sex varchar, " +
            "signature varchar)";
    private static DBHelper helper;
    private DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public static DBHelper getInstance(Context context) {
        if(helper == null) {
            helper = new DBHelper(context);
        }
        return helper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TBL_USER);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TBL_NAME_USER);
        onCreate(db);
    }
}
