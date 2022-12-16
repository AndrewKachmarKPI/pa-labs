package com.labs.domain;

import java.util.List;
import java.util.UUID;

public class PopulationNode {
    private List<Integer> itemsSelection;
    private Integer totalPrice;
    private Integer totalWeight;
    private String nodeId;

    public PopulationNode(List<Integer> itemsSelection, Integer totalPrice, Integer totalWeight) {
        if (totalPrice < 0 || totalWeight < 0 || itemsSelection.isEmpty()) {
            throw new RuntimeException("Invalid population node params");
        }
        this.itemsSelection = itemsSelection;
        this.totalPrice = totalPrice;
        this.totalWeight = totalWeight;
        this.nodeId = UUID.randomUUID().toString();
    }

    public PopulationNode(List<Integer> itemsSelection) {
        if (itemsSelection.isEmpty()) {
            throw new RuntimeException("Invalid population node params");
        }
        this.itemsSelection = itemsSelection;
        this.totalPrice = 0;
        this.totalWeight = 0;
        this.nodeId = UUID.randomUUID().toString();
    }

    public void selectItem(Integer index, Integer value) {
        if (index == null || value == null) {
            throw new RuntimeException("Genes not found");
        }
        this.itemsSelection.set(index, value);
    }

    public void countParameters(List<Item> items) {
        this.totalPrice = 0;
        this.totalWeight = 0;
        int counter = 0;
        for (Integer item : this.itemsSelection) {
            if (item == 1) {
                Item selectedItem = items.get(counter);
                totalWeight += selectedItem.getWeight();
                totalPrice += selectedItem.getPrice();
            }
            counter++;
        }
    }

    public List<Integer> getItemsSelection() {
        return itemsSelection;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getTotalWeight() {
        return totalWeight;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String toString() {
        return "PopulationNode{" +
                "totalPrice=" + totalPrice +
                ", totalWeight=" + totalWeight + '}' + "\n";
    }
}
