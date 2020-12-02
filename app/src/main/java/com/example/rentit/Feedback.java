package com.example.rentit;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Feedback {
    private int grade=100;
    private String feedback="";



    public Feedback() {
    }

    public Feedback(int grade) {
        this.grade = grade;
    }

    public Feedback(int grade, String feedback) {
        this.grade = grade;
        this.feedback = feedback;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
