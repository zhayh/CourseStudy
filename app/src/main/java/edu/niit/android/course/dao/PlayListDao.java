package edu.niit.android.course.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.niit.android.course.entity.Video;
import edu.niit.android.course.utils.DBHelper;

public class PlayListDao {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private static PlayListDao dao;

    private PlayListDao(Context context) {
        this.dbHelper = DBHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public static PlayListDao getInstance(Context context) {
        if (dao == null) {
            dao = new PlayListDao(context);
        }
        return dao;
    }

    public void save(Video video, String username) {
        if (hasVideoPlay(video.getChapterId(), video.getVideoId(), username)) {
            if (!deleteVideoPlay(video.getChapterId(), video.getVideoId(), username)) {
                return;
            }
        }
        ContentValues values = new ContentValues();
        values.put("user_name", username);
        values.put("chapter_id", video.getChapterId());
        values.put("video_id", video.getVideoId());
        values.put("video_path", video.getVideoPath());
        values.put("video_title", video.getVideoTitle());
        values.put("title", video.getTitle());

        db.insert(DBHelper.TBL_NAME_PLAY_LIST, null, values);
    }

    private boolean deleteVideoPlay(int chapterId, int videoId, String username) {
        int row = db.delete(DBHelper.TBL_NAME_PLAY_LIST,
                "chapter_id=? and video_id=? and user_name=?",
                new String[]{
                        String.valueOf(chapterId),
                        String.valueOf(videoId),
                        username
                });
        return row > 0;
    }

    private boolean hasVideoPlay(int chapterId, int videoId, String username) {
        boolean isExist = false;
        String sql = "select * from " + DBHelper.TBL_NAME_PLAY_LIST +
                " where chapter_id=? and video_id=? and user_name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{
                String.valueOf(chapterId),
                String.valueOf(videoId),
                username
        });
        if (cursor.moveToFirst()) {
            isExist = true;
        }
        cursor.close();
        return isExist;
    }

    public List<Video> getVideoHistory(String username) {
        List<Video> videos = new ArrayList<>();
        String sql = "select * from " + DBHelper.TBL_NAME_PLAY_LIST + " where user_name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        while (cursor.moveToNext()) {
            Video video = new Video();
            video.setChapterId(cursor.getInt(cursor.getColumnIndex("chapter_id")));
            video.setVideoId(cursor.getInt(cursor.getColumnIndex("video_id")));
            video.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            video.setVideoPath(cursor.getString(cursor.getColumnIndex("video_path")));
            video.setVideoTitle(cursor.getString(cursor.getColumnIndex("video_title")));

            videos.add(video);
        }
        cursor.close();
        return videos;
    }
}
