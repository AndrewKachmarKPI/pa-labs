package com.labs.utils;

public class SearchPositionMetrics {
    private Long time;
    private Integer iterations;
    private Integer fails;
    private Integer states;
    private Integer memStates = 0;

    public SearchPositionMetrics(Long time, Integer iterations, Integer fails, Integer states, Integer memStates) {
        this.time = time;
        this.iterations = iterations;
        this.fails = fails;
        this.states = states;
        this.memStates = memStates;
    }

    public SearchPositionMetrics(Long time, Integer iterations, Integer fails, Integer states) {
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
