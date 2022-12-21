package com.labs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldSize {
    S(3), M(6), L(12), XL(24);
    private Integer size;

    public String getTitle() {
        return this.name() + "(" + size + "X" + size + ")";
    }
}
