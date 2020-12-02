package com.example.rentit;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;
@IgnoreExtraProperties
public class RegisterInformation {
    private String email;
    private String password;
    private int id=1;
    protected List<CardCar> cardsUser = new ArrayList<>();
    private List<CardCar> cardsAs = new ArrayList<>();
    private List<Feedback> feedbacks = new ArrayList<>();

    public RegisterInformation() {

    }

    public RegisterInformation(String email, String password, int id, List<CardCar> cardsUser, List<CardCar> cardsAs, List<Feedback> feedbacks) {
        this.email = email;
        this.password = password;
        this.id = id;
        this.cardsUser = cardsUser;
        this.cardsAs = cardsAs;
        this.feedbacks = feedbacks;
    }

    public RegisterInformation(String email, String password, int id) {
        this.email = email;
        this.password = password;
        this.id = id;
    }
    public void removeCardCar(int i){
        cardsUser.remove(i);

    }




    public void addCardcar(CardCar cardCar) {
        cardsUser.add(cardCar);
        cardsAs.add(new CardCar(cardCar));
    }

    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CardCar> getCardsUser() {
        return new ArrayList<>(cardsUser);
    }


    public List<CardCar> getCardsAs() {
        return new ArrayList<>(cardsAs);
    }

    public void setCardsAs(List<CardCar> cardsAs) {
        this.cardsAs = cardsAs;
    }

    public List<Feedback> getFeedbacks() {
        return new ArrayList<>(feedbacks);
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public void setCardsUser(List<CardCar> cardsUser) {
        this.cardsUser = cardsUser;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
