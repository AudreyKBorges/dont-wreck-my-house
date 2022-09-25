package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.models.Reservation;

import java.util.ArrayList;

public class Result <T> {
    private final ArrayList<String> messages = new ArrayList<>();
    private T payload;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void addMessage(String message){
        messages.add(message);
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }
}