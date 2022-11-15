package com.labs.domain;

import java.util.ArrayList;
import java.util.List;

public class Knapsack {
    private List<Item> items;
    private Integer capacity;
    private Integer currentPrice;
    private Integer currentCapacity;


    public Knapsack(Integer capacity) {
        this.items = new ArrayList<>();
        this.capacity = capacity;
        this.currentPrice = 0;
        this.currentCapacity = 0;
    }

    public void addItem(Item item) {
        items.add(item);
        capacity += item.getWeight();
        currentPrice += item.getPrice();
    }

    public List<Item> getItems() {
        return items;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getCurrentPrice() {
        return currentPrice;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }

    @Override
    public String toString() {
        return "Knapsack{" +
                "items=" + items +
                ", capacity=" + capacity +
                ", currentPrice=" + currentPrice +
                ", currentCapacity=" + currentCapacity +
                '}';
    }
}
