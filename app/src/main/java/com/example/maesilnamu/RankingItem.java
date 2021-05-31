package com.example.maesilnamu;

public class RankingItem {
    String userName, userRanking, userPoint;

    public RankingItem(String userName, String userRanking, String userPoint) {
        this.userName = userName;
        this.userRanking = userRanking;
        this.userPoint = userPoint;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRanking() {
        return userRanking;
    }

    public void setUserRanking(String userRanking) {
        this.userRanking = userRanking;
    }

    public String getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(String userPoint) {
        this.userPoint = userPoint;
    }
}
