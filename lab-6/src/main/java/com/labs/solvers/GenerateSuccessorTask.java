package com.labs.solvers;

import com.labs.domain.BoxBorder;
import com.labs.domain.GameBoardNode;
import com.labs.domain.GameBox;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class GenerateSuccessorTask implements Callable<GameBoardNode> {
    private String generateBy;
    private GameBoardNode currentState;

    @Override
    public GameBoardNode call() throws Exception {
        buildGameTree(currentState, generateBy);
        return currentState;
    }

    public boolean buildGameTree(GameBoardNode currentState, String generateBy) {
        List<GameBoardNode> successors = generateSuccessors(currentState, generateBy);
        currentState.setSuccessors(successors);
        if (currentState.isLeaf()) {
            return true;
        }
        for (GameBoardNode successor : currentState.getSuccessors()) {
            boolean isLeaf = buildGameTree(successor, getGenerateBy(generateBy));
            if (isLeaf) {
                break;
            }
        }
        return true;
    }

    private List<GameBoardNode> generateSuccessors(GameBoardNode currentState, String generateBy) {
        List<GameBoardNode> successors = new ArrayList<>();
        List<BoxBorder> currentEmptyBorders = new ArrayList<>(currentState.getDistinctBoxBorders());

        for (BoxBorder currentEmptyBorder : currentEmptyBorders) {
            List<GameBox> newBoardState = new ArrayList<>(getSuccessorGameBoxes(currentState, currentEmptyBorder, generateBy));
            GameBoardNode boardNode = new GameBoardNode(currentState.getBoardId(), UUID.randomUUID().toString(),
                    newBoardState, new ArrayList<>(), currentState.getDepth() + 1, 0);
            successors.add(boardNode);
        }
        return successors;
    }

    public List<GameBox> getSuccessorGameBoxes(GameBoardNode currentNode, BoxBorder borderForSelect, String selectBy) {
        List<GameBox> currentState = new ArrayList<>(currentNode.getCurrentState());

        List<GameBox> gameBoxes = new ArrayList<>();
        for (GameBox gameBox : currentState) {
            GameBox newBox = new GameBox(gameBox);
            if (newBox.hasBorderWithId(borderForSelect.getId())) {
                newBox = new GameBox(gameBox, getSelectedBorder(newBox.getBoxBorders(), borderForSelect.getId(), selectBy), selectBy);
            }
            gameBoxes.add(newBox);
        }
        return gameBoxes;
    }

    public List<BoxBorder> getSelectedBorder(List<BoxBorder> borders, String borderId, String selectBy) {
        List<BoxBorder> currentBorders = new ArrayList<>(borders);
        List<BoxBorder> newBorders = new ArrayList<>();
        for (BoxBorder boxBorder : currentBorders) {
            BoxBorder newBorder;
            if (boxBorder.getId().equals(borderId)) {
                newBorder = new BoxBorder(boxBorder, selectBy);
            } else {
                newBorder = new BoxBorder(boxBorder);
            }
            newBorders.add(newBorder);
        }
        return newBorders;
    }

    public String getGenerateBy(String generateBy) {
        return generateBy.equals("MAX") ? "MIN" : "MAX";
    }
}
