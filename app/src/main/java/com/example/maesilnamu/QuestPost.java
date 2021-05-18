package com.example.maesilnamu;

public class QuestPost {
    private String questName, postTitle, postContent, date, userName, authNum;
    private int commentNum;

    public QuestPost(String questName, String postTitle, String postContent, String date, String userName, String authNum, int commentNum) {
        this.questName = questName;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.date = date;
        this.userName = userName;
        this.authNum = authNum;
        this.commentNum = commentNum;
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

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthNum() {
        return authNum;
    }

    public void setAuthNum(String authNum) {
        this.authNum = authNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }
}
