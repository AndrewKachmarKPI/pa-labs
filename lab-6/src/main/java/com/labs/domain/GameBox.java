package com.labs.domain;

import javafx.animation.FillTransition;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GameBox {
    private boolean isOccupied;
    private String occupiedBy;
    private BorderPane box;
    private List<BoxBorder> boxBorders = new ArrayList<>();

    public boolean isAllBorderBoxSelected() {
        return boxBorders.stream().filter(BoxBorder::isSelected).count() == boxBorders.size();
    }

    public boolean isNotOccupied() {
        return !isOccupied;
    }

    public void closeGameBox(GamePlayer closedByPlayer) {
        Rectangle rectangle = (Rectangle) box.getCenter();
        fillBox(rectangle, closedByPlayer.getColor());
        this.isOccupied = true;
        this.occupiedBy = closedByPlayer.getPlayerId();
    }

    private void fillBox(Rectangle rectangle, Color color) {
        FillTransition fill = new FillTransition();
        fill.setDuration(Duration.millis(1000));
        fill.setFromValue(Color.WHITE);
        fill.setToValue(color);
        fill.setShape(rectangle);
        fill.play();
    }
}
