package com.labs.domain;

import javafx.animation.FillTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.*;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

    public GameBox(GameBox gameBox) {
        this.isOccupied = gameBox.isOccupied;
        this.occupiedBy = gameBox.occupiedBy;
        this.box = gameBox.box;
        this.boxBorders = gameBox.boxBorders;
    }

    public GameBox(GameBox gameBox, List<BoxBorder> boxBorders, String selectBy) {
        this.isOccupied = gameBox.isOccupied;
        this.occupiedBy = gameBox.occupiedBy;
        this.box = gameBox.box;
        this.boxBorders = boxBorders;
        if (getAllNotSelectedBorders().isEmpty()) {
            this.isOccupied = true;
            this.occupiedBy = selectBy;
        }
    }

    @Override
    public String toString() {
        return "Box{" +
                "isOccupied=" + isOccupied +
                ", occupiedBy='" + occupiedBy + '\'' +
                ", box=" + box +
                ", boxBorders=" + boxBorders.size() +
                '}';
    }

    public Button getButtonById(String id) {
        return boxBorders.stream().filter(boxBorder -> boxBorder.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Btn not found")).getButton();
    }

    public boolean isLastBorderBox() {
        return boxBorders.stream().filter(BoxBorder::isSelected).count() == boxBorders.size() - 1;
    }

    public boolean isAllBorderBoxSelected() {
        return boxBorders.stream().filter(BoxBorder::isSelected).count() == boxBorders.size();
    }

    public boolean isNotOccupied() {
        return !isOccupied;
    }

    public List<BoxBorder> getAllNotSelectedBorders() {
        return boxBorders.stream().filter(BoxBorder::isNotSelected).collect(Collectors.toList());
    }

    public void closeGameBox(GamePlayer closedByPlayer) {
        Rectangle rectangle = (Rectangle) box.getCenter();
        fillBox(rectangle, closedByPlayer.getColor());
        this.isOccupied = true;
        this.occupiedBy = closedByPlayer.getTitle();
    }

    private void fillBox(Rectangle rectangle, Color color) {
        FillTransition fill = new FillTransition();
        fill.setDuration(Duration.millis(1000));
        fill.setFromValue(Color.WHITE);
        fill.setToValue(color);
        fill.setShape(rectangle);
        fill.play();
    }

    public List<BoxBorder> getEmptyBoxBorders() {
        return boxBorders.stream().filter(BoxBorder::isNotSelected).collect(Collectors.toList());
    }

    public boolean hasBorderWithId(String id) {
        return boxBorders.stream().anyMatch(boxBorder -> boxBorder.getId().equals(id));
    }

    public GameBox copyGameBoxWithoutBorders() {
        return GameBox.builder()
                .isOccupied(this.isOccupied)
                .occupiedBy(this.occupiedBy)
                .box(this.box)
                .boxBorders(new ArrayList<>()).build();
    }

    public GameBox copyGameBox() {
        return GameBox.builder()
                .isOccupied(this.isOccupied)
                .occupiedBy(this.occupiedBy)
                .box(this.box)
                .boxBorders(this.boxBorders).build();
    }

}
