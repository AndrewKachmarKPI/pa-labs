package com.labs.domain;

import com.labs.enums.FieldSize;
import com.labs.enums.GameComplexity;
import com.labs.enums.PlayerType;
import javafx.scene.paint.Color;
import lombok.*;


@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameProperties {
    private PlayerType firstPlayerType;
    private PlayerType secondPlayerType;
    private Color firstPlayerColor;
    private Color secondPlayerColor;
    private GameComplexity gameComplexity;
    private FieldSize gameFieldSize;
}
