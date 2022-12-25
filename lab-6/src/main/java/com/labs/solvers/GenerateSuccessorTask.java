package com.labs.solvers;

import com.labs.domain.BoxBorder;
import com.labs.domain.GameBoardNode;
import com.labs.domain.GameBox;
import com.labs.enums.PlayerType;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class GenerateSuccessorTask implements Callable<GameBoardNode> {
    private String generateBy;
    private GameBoardNode currentState;
    private boolean isFirstLayer;
    private static int depth = 2;


    @Override
    public GameBoardNode call() {
        if (isFirstLayer) {
            buildFirstLayer(currentState, generateBy);
        } else {
            buildGameTree(currentState, generateBy);
        }
        return currentState;
    }

    public void buildFirstLayer(GameBoardNode currentState, String generateBy) {
        List<GameBoardNode> successors = generateSuccessors(currentState, generateBy);
        currentState.setSuccessors(successors);
    }

    public void buildGameTree(GameBoardNode currentState, String generateBy) {
        buildFirstLayer(currentState, generateBy);
        if (currentState.isLeaf() || depth == currentState.getDepth()) {
            return;
        }
        for (GameBoardNode successor : currentState.getSuccessors()) {
            buildGameTree(successor, getGenerateBy(generateBy));
        }
    }

    private List<GameBoardNode> generateSuccessors(GameBoardNode currentState, String generateBy) {
        List<GameBoardNode> successors = new ArrayList<>();
        List<BoxBorder> currentEmptyBorders = new ArrayList<>(currentState.getDistinctBoxBorders());

        for (BoxBorder currentEmptyBorder : currentEmptyBorders) {
            GameBoardNode boardNode = new GameBoardNode(currentState.getBoardId(), UUID.randomUUID().toString(), currentState.getDepth() + 1, currentState.getFunctionCost(),
                    generateBy, currentState.getHumanScore(), currentState.getComputerScore());
            List<GameBox> newBoardState = new ArrayList<>(getSuccessorGameBoxes(currentState, boardNode, currentEmptyBorder, generateBy));
            boardNode.setCurrentState(newBoardState);
            successors.add(boardNode);
        }
        return successors;
    }

    public List<GameBox> getSuccessorGameBoxes(GameBoardNode currentNode, GameBoardNode boardNode, BoxBorder borderForSelect, String selectBy) {
        List<GameBox> currentState = new ArrayList<>(currentNode.getCurrentState());

        List<GameBox> gameBoxes = new ArrayList<>();
        for (GameBox gameBox : currentState) {
            GameBox newBox = new GameBox(gameBox);
            if (newBox.hasBorderWithId(borderForSelect.getId())) {
                newBox = new GameBox(gameBox, getSelectedBorder(newBox.getBoxBorders(), borderForSelect.getId(), selectBy), selectBy);
            }
            if (newBox.isBoxFilled()) {
                boardNode.updateScore(selectBy);
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
                newBorder = new BoxBorder(boxBorder);
                newBorder.selectBorder(selectBy);
            } else {
                newBorder = new BoxBorder(boxBorder);
            }
            newBorders.add(newBorder);
        }
        return newBorders;
    }

    public String getGenerateBy(String generateBy) {
        return generateBy.equals(PlayerType.COMPUTER.toString()) ? PlayerType.HUMAN.toString() : PlayerType.COMPUTER.toString();
    }
}
