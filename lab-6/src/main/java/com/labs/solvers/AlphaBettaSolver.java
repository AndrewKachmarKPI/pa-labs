package com.labs.solvers;

import com.labs.domain.BoxBorder;
import com.labs.domain.GameBoardNode;
import com.labs.domain.GameBox;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class AlphaBettaSolver implements GameSolver {
    private static Integer alpha = Integer.MIN_VALUE; // BEST FOR MAX player
    private static Integer beta = Integer.MAX_VALUE;  // BEST FOR MIN player
    private GameBoardNode rootState;

    public AlphaBettaSolver(GameBoardNode rootState) {
        this.rootState = rootState;
    }

    // If alpha >= beta then prune
    // If alpha < beta DON'T prune
//    private String getNextMove(GameBoard board, GameDifficulty difficulty) {
//
//    }
//
    public void alphaBettaMiniMax() {
        buildGameTree(rootState, "MAX");
        System.out.println("TEST");
    }

    public void buildGameTree(GameBoardNode currentState, String generateBy) {
        ExecutorService executorService = Executors.newFixedThreadPool(24);
        List<Callable<GameBoardNode>> callables = new ArrayList<>();
        List<GameBoardNode> successors = generateSuccessors(currentState, generateBy);

        for (GameBoardNode successor : successors) {
            callables.add(new GenerateSuccessorTask(generateBy, successor));
        }
        successors.clear();
        try {
            List<Future<GameBoardNode>> futures = executorService.invokeAll(callables);
            for (Future<GameBoardNode> future : futures) {
                successors.add(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentState.setSuccessors(successors);
    }

    //    public boolean buildGameTree(GameBoardNode currentState, String generateBy) {
//        List<GameBoardNode> successors = generateSuccessors(currentState, generateBy);
//        currentState.setSuccessors(successors);
//        if (currentState.isLeaf()) {
//            System.out.println("LEAF->"+currentState.getDepth());
//            return true;
//        }
//        for (GameBoardNode successor : currentState.getSuccessors()) {
//            boolean isLeaf = buildGameTree(successor, getGenerateBy(generateBy));
//            if (isLeaf) {
//                break;
//            }
//        }
//        return true;
//    }
//
//    public String getGenerateBy(String generateBy) {
//        return generateBy.equals("MAX") ? "MIN" : "MAX";
//    }
//
//    //SUCCESSORS
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
}
