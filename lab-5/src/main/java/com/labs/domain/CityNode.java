package com.labs.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public final class CityNode {
    private Integer index;
    private Double probability;

    public CityNode(Integer index) {
        this.index = index;
    }

    public boolean isNodeFound() {
        return index != null;
    }
}
