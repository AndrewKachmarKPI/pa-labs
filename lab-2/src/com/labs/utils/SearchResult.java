package com.labs.utils;

public class SearchResult {
    private GameNode solution;
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

    public boolean isSuccess() {
        return isSuccess;
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
