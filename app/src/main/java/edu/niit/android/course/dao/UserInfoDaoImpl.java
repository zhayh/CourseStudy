package edu.niit.android.course.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.entity.UserInfo;
import edu.niit.android.course.utils.DBHelper;

public class UserInfoDaoImpl implements UserInfoDao {
    private DBHelper helper;    // 用于创建数据库、获取数据库对象
    private SQLiteDatabase db;  // 用于执行SQL语句

    public UserInfoDaoImpl(Context context) {
        helper = DBHelper.getInstance(context);  // 创建数据库
    }

    @Override
    public List<UserInfo> select() {
        List<UserInfo> userInfos  = new ArrayList<>();
        db = helper.getReadableDatabase();    // 获取了数据库对象

        Cursor cursor = db.query(DBHelper.TBL_NAME_USER,
                null, null,null,
                null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(cursor.getString(cursor.getColumnIndex("user_name")));
                userInfo.setNickname(cursor.getString(cursor.getColumnIndex("nick_name")));
                userInfo.setSex(cursor.getString(cursor.getColumnIndex("sex")));
                userInfo.setSignature(cursor.getString(cursor.getColumnIndex("signature")));

                userInfos.add(userInfo);
            }while(cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return userInfos;
    }

    @Override
    public UserInfo select(String username) {
        String sql = "select * from " + DBHelper.TBL_NAME_USER + " where user_name=?";
        UserInfo userInfo = null;
        db = helper.getReadableDatabase();
//        Cursor cursor = db.query(DBHelper.TBL_NAME_USER, null,
//                "user_name=?", new String[]{username},
//                null, null, null);
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if(cursor != null && cursor.moveToFirst()) {
            userInfo = new UserInfo();
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex("user_name")));
            userInfo.setNickname(cursor.getString(cursor.getColumnIndex("nick_name")));
            userInfo.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            userInfo.setSignature(cursor.getString(cursor.getColumnIndex("signature")));
            cursor.close();
        }
        db.close();
        return userInfo;
    }

    @Override
    public void insert(UserInfo userInfo) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_name", userInfo.getUsername());
        values.put("nick_name", userInfo.getNickname());
        values.put("sex", userInfo.getSex());
        values.put("signature", userInfo.getSignature());
        db.insert(DBHelper.TBL_NAME_USER, null, values);

//        String sql = "insert into " + DBHelper.TBL_NAME_USER + " values(null, ?, ?, ?, ?)";
//        db.execSQL(sql, new String[]{
//                userInfo.getUsername(),
//                userInfo.getNickname(),
//                userInfo.getSex(),
//                userInfo.getSignature()});
        db.close();
    }

    @Override
    public void update(UserInfo userInfo) {
        ContentValues values = new ContentValues();
        values.put("user_name", userInfo.getUsername());
        values.put("nick_name", userInfo.getNickname());
        values.put("sex", userInfo.getSex());
        values.put("signature", userInfo.getSignature());

        db = helper.getWritableDatabase();
        db.update(DBHelper.TBL_NAME_USER, values,
                "user_name=?", new String[]{userInfo.getUsername()});
        db.close();
    }

    @Override
    public void delete(UserInfo userInfo) {
        db = helper.getWritableDatabase();
        db.delete(DBHelper.TBL_NAME_USER,
                "user_name=?", new String[]{userInfo.getUsername()});
        db.close();
    }
}
