package com.labs.domain;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import lombok.*;


import java.util.ArrayList;
import java.util.List;


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
        return boxBorders.stream().filter(boxBorder -> boxBorder.getButton().getId().equals(id)).findFirst()
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


    public void closeGameBox(GamePlayer closedByPlayer) {
        Rectangle rectangle = (Rectangle) box.getCenter();
        rectangle.setFill(closedByPlayer.getColor());
        this.isOccupied = true;
        this.occupiedBy = closedByPlayer.getTitle();
    }
}
