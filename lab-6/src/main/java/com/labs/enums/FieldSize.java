package com.labs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum FieldSize {
    S(2), M(3), L(12);
    private Integer size;

    public static FieldSize getByTitle(String title) {
        return Arrays.stream(FieldSize.values()).filter(fieldSize -> fieldSize.getTitle().equals(title)).findAny()
                .orElseThrow(() -> new RuntimeException("Size not found"));
    }

    public String getTitle() {
        return this.name() + "(" + size + "X" + size + ")";
    }
}
