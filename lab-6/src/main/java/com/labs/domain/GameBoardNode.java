package com.labs.domain;

import com.labs.enums.PlayerType;
import lombok.*;

import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameBoardNode {
    private String parentId;
    private String boardId;
    private List<GameBox> currentState;
    private List<GameBoardNode> successors;
    private Integer depth;
    private Integer functionCost;
    private PlayerType moveBy;
    private Integer humanScore;
    private Integer computerScore;


    public GameBoardNode(String parentId, String boardId, Integer depth, Integer functionCost, String moveBy, Integer humanScore, Integer computerScore) {
        this.parentId = parentId;
        this.boardId = boardId;
        this.successors = new ArrayList<>();
        this.depth = depth;
        this.functionCost = functionCost;
        this.moveBy = PlayerType.valueOf(moveBy);
        this.humanScore = humanScore;
        this.computerScore = computerScore;
    }

    public GameBoardNode(GameBoardNode gameBoardNode) {
        this.parentId = gameBoardNode.parentId;
        this.boardId = gameBoardNode.boardId;
        this.currentState = gameBoardNode.currentState;
        this.successors = gameBoardNode.successors;
        this.depth = gameBoardNode.depth;
        this.functionCost = gameBoardNode.functionCost;
        this.moveBy = gameBoardNode.moveBy;
        this.humanScore = gameBoardNode.humanScore;
        this.computerScore = gameBoardNode.computerScore;
    }

    public List<BoxBorder> getDistinctBoxBorders() {
        List<BoxBorder> boxBorders = new ArrayList<>();
        for (GameBox gameBox : currentState) {
            boxBorders.addAll(gameBox.getEmptyBoxBorders());
        }
        return boxBorders.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(BoxBorder::getId))), ArrayList::new));
    }

    public List<BoxBorder> getDistinctAllBoxBorders() {
        List<BoxBorder> boxBorders = new ArrayList<>();
        for (GameBox gameBox : currentState) {
            boxBorders.addAll(gameBox.getBoxBorders());
        }
        return boxBorders.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(BoxBorder::getId))), ArrayList::new));
    }

    public boolean isLeaf() {
        return successors.isEmpty();
    }

    public void updateScore(String selectedBy) {
        if (Objects.equals(selectedBy, PlayerType.COMPUTER.toString())) {
            computerScore += 1;
        } else if (Objects.equals(selectedBy, PlayerType.HUMAN.toString())) {
            humanScore += 1;
        }
    }

    @Override
    public String toString() {
        return "GameBoardNode{" +
                "depth=" + depth +
                ", functionCost=" + functionCost +
                ", moveBy=" + moveBy +
                ", humanScore=" + humanScore +
                ", computerScore=" + computerScore +
                '}';
    }
}
