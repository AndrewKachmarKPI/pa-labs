package com.labs.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CityNode {
    private Integer index;
    private Double probability;

    public CityNode(Integer index) {
        if (index < 0) {
            throw new RuntimeException("Invalid index");
        }
        this.index = index;
    }

    public CityNode() {
        this.index = null;
    }

    public boolean isNodeFound() {
        return index != null;
    }
}
