package com.labs.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class BoxBorderPosition {
    private int xPos, yPos;
    private boolean isHorizontal;

    public BoxBorderPosition() {
        xPos = yPos = -1;
        isHorizontal = false;
    }

    public String getBorderPosition() {
        return (isHorizontal ? "H" : "V") + yPos + "" + xPos;
    }
}
