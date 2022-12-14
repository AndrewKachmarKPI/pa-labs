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
        if(message.isEmpty() || message.isBlank()){
            throw new RuntimeException("Message should not be blank");
        }
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public GameNode getSolution() {
        return solution;
    }

    public String getMessage() {
        return message;
    }

    public void setSolution(GameNode solution) {
        this.solution = solution;
    }

    public String toString() {
        return "SearchResult{" +
                "solution=" + solution +
                ", message='" + message + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
