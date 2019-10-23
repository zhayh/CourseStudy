package edu.niit.android.course.entity;

import java.io.Serializable;

/**
 * "chapterId": 1,
 * "videoId": "1",
 * "title": "第1章 Android 基础入门",
 * "videoTitle": "Android系统简介",
 * "videoPath": "video101.mp4"
 */
public class Video implements Serializable {
    private int chapterId;
    private int videoId;
    private String title;
    private String videoTitle;
    private String videoPath;

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
