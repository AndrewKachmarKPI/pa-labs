package com.labs.domain;

public class Item {
    private Integer price;
    private Integer weight;

    public Item(Integer price, Integer weight) {
        this.price = price;
        this.weight = weight;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Item{" +
                "price=" + price +
                ", weight=" + weight +
                '}';
    }
}
