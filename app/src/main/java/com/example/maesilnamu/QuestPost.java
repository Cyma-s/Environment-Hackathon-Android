package com.example.maesilnamu;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class QuestPost implements Serializable {
    public String questName, postTitle, postContent, date, userName, picture, type, postingId, writerCode;
    public int pictureNum, reviewNum, authNum;

    public QuestPost(String postingId, String questName, String postTitle, String postContent, String picture, String date, String userName, String writerCode, int authNum, int pictureNum, int reviewNum, String type) {
        this.questName = questName;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.date = date;
        this.userName = userName;
        this.picture = picture;
        this.type = type;
        this.postingId = postingId;
        this.writerCode = writerCode;
        this.pictureNum = pictureNum;
        this.reviewNum = reviewNum;
        this.authNum = authNum;
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

    public String getWriterCode() {
        return writerCode;
    }

    public void setWriterCode(String writerCode) {
        this.writerCode = writerCode;
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


    public String getPostingId() {
        return postingId;
    }

    public void setPostingId(String postingId) {
        this.postingId = postingId;
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
