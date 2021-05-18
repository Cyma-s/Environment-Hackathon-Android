package com.example.maesilnamu;

import androidx.annotation.NonNull;

public class QuestPost {
    private String questName, postTitle, postContent, date, userName, picture, type;
    private int pictureNum, reviewNum, authNum;

    public QuestPost(String questName, String postTitle, String postContent, String picture, String date, String userName,
                     int authNum, int pictureNum, int reviewNum, String type) {
        this.questName = questName;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.date = date;
        this.userName = userName;
        this.authNum = authNum;
        this.picture = picture;
        this.pictureNum = pictureNum;
        this.reviewNum = reviewNum;
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPictureNum() {
        return pictureNum;
    }

    public void setPictureNum(int pictureNum) {
        this.pictureNum = pictureNum;
    }

    public int getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getDate() {
        return date;
    }

    @NonNull
    @Override
    public String toString() {
        String total;
        total = questName + " " + postTitle;
        return total;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAuthNum() {
        return authNum;
    }

    public void setAuthNum(int authNum) {
        this.authNum = authNum;
    }
}
