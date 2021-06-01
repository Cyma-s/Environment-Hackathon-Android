package com.example.maesilnamu;

public class MyQuest {
    String questName;
    boolean isComplete;

    public MyQuest(String questName, boolean isComplete) {
        this.questName = questName;
        this.isComplete = isComplete;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
