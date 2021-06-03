package com.example.maesilnamu;

import java.io.Serializable;

public class MyQuest {
    String questName, point, explanation, condition, image;
    boolean isComplete;

    public MyQuest(String questName, String point, String explanation, String condition, String image, boolean isComplete) {
        this.questName = questName;
        this.point = point;
        this.explanation = explanation;
        this.condition = condition;
        this.image = image;
        this.isComplete = isComplete;
    }

    public MyQuest(String questName, String point, String explanation, String condition, String image) {
        this.questName = questName;
        this.point = point;
        this.explanation = explanation;
        this.condition = condition;
        this.image = image;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
