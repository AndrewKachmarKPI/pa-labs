package com.labs.domain;

import com.labs.service.GameConstants;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class BoxBorderPosition implements GameConstants {
    private int xPos;
    private int yPos;
    private boolean isHorizontal;

    public BoxBorderPosition() {
        xPos = -1;
        yPos = -1;
        isHorizontal = false;
    }

    public String getBorderPosition() {
        String posPrefix = isHorizontal ? HORIZONTAL : VERTICAL;
        return posPrefix + yPos + "" + xPos;
    }
}
