package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.models.Reservation;

import java.util.ArrayList;

public class Result {
    private final ArrayList<String> messages = new ArrayList<>();
    private Reservation reservation;

    public ArrayList<String> getMessages() {
        return messages;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void addMessage(String message){
        messages.add(message);
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }
}