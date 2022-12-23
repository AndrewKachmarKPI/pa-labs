package com.labs.domain;

import com.labs.enums.FieldSize;
import com.labs.enums.GameDifficulty;
import lombok.*;


@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameProperties {
    private GamePlayer firstPlayer;
    private GamePlayer secondPlayer;
    private GameDifficulty gameDifficulty;
    private FieldSize gameFieldSize;
}
