package com.labs.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoxBorderNode implements Comparable<BoxBorderNode> {
    private BoxBorderPosition boxBorderPosition;
    private int utility;

    @Override
    public int compareTo(BoxBorderNode boxBorderNode) {
        return this.utility - boxBorderNode.utility;
    }
}
