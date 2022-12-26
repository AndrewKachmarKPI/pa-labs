package com.labs.domain;

import com.labs.enums.PlayerType;
import com.labs.solvers.AlphaBettaSolver;
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
    private String playerId;
    private PlayerType type;
    private Color color;
    private Integer colorIndex;
    private Integer score;
    private GameSolver gameSolver;
}
