package com.labs.utils;

import java.util.Objects;

public class QueenPosition {
    private Integer xPos;
    private Integer yPos;

    public QueenPosition(Integer xPos, Integer yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public Integer getxPos() {
        return xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueenPosition that = (QueenPosition) o;
        return Objects.equals(xPos, that.xPos) && Objects.equals(yPos, that.yPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPos, yPos);
    }

    @Override
    public String toString() {
        return "QueenPosition{" +
                "xPos=" + xPos +
                ", yPos=" + yPos +
                '}';
    }
}
