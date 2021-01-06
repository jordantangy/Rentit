package com.example.rentit;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Feedback {
    private int grade=10;
    private String feedback="";
    private String emailCar="";
    private String emailReporting="";
    private int id=-1;
    private String key="";




    public Feedback() {
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

    public String getEmailCar() {
        return emailCar;
    }

    public void setEmailCar(String emailCar) {
        this.emailCar = emailCar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailReporting() {
        return emailReporting;
    }

    public void setEmailReporting(String emailReporting) {
        this.emailReporting = emailReporting;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
