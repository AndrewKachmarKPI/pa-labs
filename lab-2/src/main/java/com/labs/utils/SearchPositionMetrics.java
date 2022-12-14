package com.labs.utils;

public class SearchPositionMetrics {
    private Long time;
    private Integer iterations;
    private Integer fails;
    private Integer states;
    private Integer memStates = 0;

    public SearchPositionMetrics(Long time, Integer iterations, Integer fails, Integer states, Integer memStates) {
        if (time < 0 || iterations < 0 || fails < 0 || states < 0 || memStates < 0) {
            throw new RuntimeException("Invalid metrics");
        }
        this.time = time;
        this.iterations = iterations;
        this.fails = fails;
        this.states = states;
        this.memStates = memStates;
    }

    public SearchPositionMetrics(Long time, Integer iterations, Integer fails, Integer states) {
        if (time < 0 || iterations < 0 || fails < 0 || states < 0) {
            throw new RuntimeException("Invalid metrics");
        }
        this.time = time;
        this.iterations = iterations;
        this.fails = fails;
        this.states = states;
    }

    public Long getTime() {
        return time;
    }

    public Integer getIterations() {
        return iterations;
    }

    public Integer getFails() {
        return fails;
    }

    public Integer getStates() {
        return states;
    }

    public Integer getMemStates() {
        return memStates;
    }
}
