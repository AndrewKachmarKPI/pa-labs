package com.labs.domain;

import javafx.scene.layout.VBox;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GameBoard {
    private VBox gameBoard;
    private int size;
    private List<GameBox> gameBoxList;

    public boolean isAllBoxesClosed() {
        return gameBoxList.stream().noneMatch(GameBox::isNotOccupied);
    }
}
