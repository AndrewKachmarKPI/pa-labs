package com.labs.domain;

import java.util.ArrayList;
import java.util.List;

public final class PathSearchResult {
    private List<Integer> pathIndexes = new ArrayList<>();
    private StringBuilder path = new StringBuilder();
    private Integer pathCost = Integer.MAX_VALUE;
    private String antId;

    public PathSearchResult(int startPosition, String antId) {
        addCityIndex(startPosition, false);
        this.antId = antId;
    }

    public PathSearchResult() {
    }

    public void addCityIndex(int cityIndex, boolean isLast) {
        pathIndexes.add(cityIndex);
        path.append(cityIndex);
        if (!isLast) {
            path.append("-");
        }
    }

    public void countPathCost(int[][] distances) {
        this.pathCost = 0;
        for (int i = 0; i < pathIndexes.size() - 1; i++) {
            this.pathCost += distances[pathIndexes.get(i)][pathIndexes.get(i + 1)];
        }
    }

    public Integer getPathCost() {
        return pathCost;
    }

    public String getPath() {
        return path.toString();
    }

    public String getAntId() {
        return antId;
    }

    @Override
    public String toString() {
        return "PathSearchResult{" +
                "path=" + path +
                ", pathCost=" + pathCost +
                '}';
    }
}
