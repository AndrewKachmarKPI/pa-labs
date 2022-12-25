package com.labs.domain;

import javafx.scene.layout.VBox;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

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

    public List<BoxBorder> getAllMoves() {
        List<BoxBorder> boxBorders = new ArrayList<>();
        List<GameBox> emptyBoxes = gameBoxList.stream().filter(GameBox::isNotOccupied).collect(Collectors.toList());
        for (GameBox emptyBox : emptyBoxes) {
            List<BoxBorder> emptyBoxBorders = emptyBox.getEmptyBoxBorders().stream()
                    .filter(boxBorder -> !hasBoxBorderWithId(boxBorders, boxBorder.getId()))
                    .collect(Collectors.toList());
            boxBorders.addAll(emptyBoxBorders);
        }
        return boxBorders;
    }

    private boolean hasBoxBorderWithId(List<BoxBorder> boxBorders, String boxBorderId) {
        return boxBorders.stream().anyMatch(boxBorder -> Objects.equals(boxBorder.getId(), boxBorderId));
    }
}