package com.labs.solvers;

import com.labs.domain.BoxBorder;
import com.labs.domain.GameBoardNode;
import com.labs.domain.GameBox;
import com.labs.enums.PlayerType;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class GenerateSuccessorTask implements Callable<GameBoardNode> {
    private GameBoardNode currentState;
    private PlayerType moveBy;
    private static int depth = 2;


    @Override
    public GameBoardNode call() {
        buildFirstLayer(currentState, moveBy.toString());
        return currentState;
    }

    public void buildFirstLayer(GameBoardNode currentState, String generateBy) {
        List<GameBoardNode> successors = generateSuccessors(currentState, generateBy);
        currentState.setSuccessors(successors);
    }

    private List<GameBoardNode> generateSuccessors(GameBoardNode currentState, String generateBy) {
        List<GameBoardNode> successors = new ArrayList<>();
        List<BoxBorder> currentEmptyBorders = new ArrayList<>(currentState.getDistinctBoxBorders());

        for (BoxBorder currentEmptyBorder : currentEmptyBorders) {
            GameBoardNode boardNode = new GameBoardNode(currentState);
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
                System.out.println("UPDATE SCORE");
                boardNode.updateScore(selectBy);
                System.out.println(boardNode);
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

    public String getMoveBy(String generateBy) {
        return generateBy.equals(PlayerType.COMPUTER.toString()) ? PlayerType.HUMAN.toString() : PlayerType.COMPUTER.toString();
    }

    public void buildGameTree(GameBoardNode currentState, String generateBy) {
        buildFirstLayer(currentState, generateBy);
        if (currentState.isLeaf() || depth == currentState.getDepth()) {
            return;
        }
        for (GameBoardNode successor : currentState.getSuccessors()) {
            buildGameTree(successor, getMoveBy(generateBy));
        }
    }
}
