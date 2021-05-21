package com.example.maesilnamu;

public class Mypage {
    private String nickName;
    private int rank, token;
    public Mypage(String nickName, int rank, int token) {
        this.nickName = nickName;
        this.rank = rank;
        this.token = token;
    }
    public String getNickName() {
        return nickName;
    }
    public int getRank() { return rank; }
    public int getToken() { return token; }
}
