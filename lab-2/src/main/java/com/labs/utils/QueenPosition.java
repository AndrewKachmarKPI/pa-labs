package com.labs.utils;

import java.util.Objects;

public class QueenPosition {
    private Integer xPos;
    private Integer yPos;

    public QueenPosition(Integer xPos, Integer yPos) {
        if (!isPositionValid(xPos, yPos)) {
            throw new RuntimeException("Invalid Queen position");
        }
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public Integer getxPos() {
        return xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    private boolean isPositionValid(Integer xPos, Integer yPos) {
        return (xPos >= 0 && xPos <= 7) && (yPos >= 0 && yPos <= 7);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueenPosition that = (QueenPosition) o;
        return Objects.equals(xPos, that.xPos) && Objects.equals(yPos, that.yPos);
    }

    public int hashCode() {
        return Objects.hash(xPos, yPos);
    }

    public String toString() {
        return "QueenPosition{" +
                "xPos=" + xPos +
                ", yPos=" + yPos +
                '}';
    }
}
