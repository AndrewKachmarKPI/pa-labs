package com.labs.domain;

public class Item {
    private Integer price;
    private Integer weight;

    public Item(int price, int weight) {
        if (price <= 0 || weight <= 0) {
            throw new RuntimeException("Item params should not be 0");
        }
        this.price = price;
        this.weight = weight;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getWeight() {
        return weight;
    }
}
