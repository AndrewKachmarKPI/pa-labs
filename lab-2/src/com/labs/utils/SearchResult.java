package com.labs.utils;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private GameNode solution;
    private List<GameNode> solutionPath = new ArrayList<>();
    private String message;
    private boolean isSuccess;

    public SearchResult(GameNode solution, boolean isSuccess) {
        this.solution = solution;
        this.isSuccess = isSuccess;
    }

    public SearchResult(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public GameNode getSolution() {
        return solution;
    }

    public void setSolution(GameNode solution) {
        this.solution = solution;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public List<GameNode> getSolutionPath() {
        return solutionPath;
    }

    public void setSolutionPath(List<GameNode> solutionPath) {
        this.solutionPath = solutionPath;
    }


    @Override
    public String toString() {
        return "SearchResult{" +
                "positions=" + solution +
                ", message='" + message + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
