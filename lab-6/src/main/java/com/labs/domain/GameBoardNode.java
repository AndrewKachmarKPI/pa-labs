package com.labs.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GameBoardNode {
    private String parentId;
    private String boardId;
    private List<GameBox> currentState;
    private List<GameBoardNode> successors;
    private Integer depth;
    private Integer functionCost;

    public List<BoxBorder> getDistinctBoxBorders() {
        List<BoxBorder> boxBorders = new ArrayList<>();
        for (GameBox gameBox : currentState) {
            boxBorders.addAll(gameBox.getEmptyBoxBorders());
        }
        return boxBorders.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(BoxBorder::getId))), ArrayList::new));
    }

    public boolean isLeaf() {
        return successors.isEmpty();
    }

    public GameBoardNode copyState() {
        return this;
    }
}
