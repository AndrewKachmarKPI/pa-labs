package com.labs.domain;

import com.labs.enums.PlayerType;
import javafx.scene.paint.Color;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayer {
    private Integer score = 0;
    private PlayerType type;
    private Color color;
    private String title;
}
