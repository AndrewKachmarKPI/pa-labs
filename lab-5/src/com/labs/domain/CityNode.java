package com.labs.domain;

public class CityNode {
    private Integer index;
    private String city;
    private Double probability;

    public CityNode(Integer index, String city, Double probability) {
        this.index = index;
        this.city = city;
        this.probability = probability;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }
    @Override
    public String toString() {
        return "CityNode{" +
                "index=" + index +
                ", city='" + city + '\'' +
                ", probability=" + probability +
                '}';
    }
}
