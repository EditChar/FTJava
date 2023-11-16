package com.ing.ftjava;

import java.io.Serializable;

public class FarmItem implements Serializable {
    private int imageResource;
    private int counter;
    private int quantity;
    private int total;
    private String dateTime;
    private String allTotal;


    public FarmItem(int imageResource) {
        this.imageResource = imageResource;
        this.counter = 0;
        this.quantity = 0;
        this.total = 0;

    }

    public FarmItem(String dateTime, String allTotal) {
        this.dateTime = dateTime;
        this.allTotal = allTotal;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void updateTotal() {
        this.total = counter * quantity;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getAllTotal() {
        return allTotal;
    }
    // FarmItem nesnesini String'e çevirme
    @Override
    public String toString() {
        return dateTime + "|" + allTotal;
    }

    // String'i FarmItem nesnesine çevirme
    public static FarmItem fromString(String input) {
        String[] parts = input.split("\\|");
        if (parts.length == 2) {
            return new FarmItem(parts[0], parts[1]);
        }
        return null;
    }
}