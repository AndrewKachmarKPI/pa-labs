package com.labs.domain;

public class CityNode {
    private Integer index;
    private Double probability;

    public CityNode(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public boolean isNodeFound() {
        return index != null;
    }

    @Override
    public String toString() {
        return "CityNode{" +
                "index=" + index +
                ", probability=" + probability +
                '}';
    }
}
