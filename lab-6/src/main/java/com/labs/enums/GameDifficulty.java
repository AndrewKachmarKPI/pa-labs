package com.labs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameDifficulty {
    EASY(1),
    MEDIUM(10),
    HARD(20);
    private Integer difficulty;
}
