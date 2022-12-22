package com.labs.domain;

import javafx.scene.layout.BorderPane;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Box {
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
}
