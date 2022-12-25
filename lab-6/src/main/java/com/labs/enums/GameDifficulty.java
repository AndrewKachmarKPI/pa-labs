package com.labs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameDifficulty {
    EASY(4),
    MEDIUM(4),
    HARD(6);
    private Integer difficulty;
}
