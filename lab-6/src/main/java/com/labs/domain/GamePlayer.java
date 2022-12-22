package com.labs.domain;

import com.labs.enums.PlayerType;
import com.labs.solvers.GameSolver;
import javafx.scene.paint.Color;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayer {
    private Integer score = 0;
    private PlayerType type;
    private Color color;
    private String title;
    private GameSolver gameSolver;

    public void updateScore() {
        this.score += 10;
    }
}
