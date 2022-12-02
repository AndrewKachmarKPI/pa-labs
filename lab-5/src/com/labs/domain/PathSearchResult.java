package com.labs.domain;

import java.util.ArrayList;
import java.util.List;

public class PathSearchResult {
    private List<Integer> pathIndexes = new ArrayList<>();
    private StringBuilder path = new StringBuilder();
    private Integer pathCost = 0;
    private String antId;

    public PathSearchResult(int startPosition, String antId) {
        addCityIndex(startPosition, false);
        this.antId = antId;
    }

    public void addCityIndex(int cityIndex, boolean isLast) {
        pathIndexes.add(cityIndex);
        path.append(cityIndex);
        if (!isLast) {
            path.append("-");
        }
    }


    public void printPath() {
        System.out.println(path.toString());
    }

    public String getPath() {
        return path.toString();
    }

    public void countPathCost(int[][] distances) {
        for (int i = 0; i < pathIndexes.size() - 1; i++) {
            this.pathCost += distances[pathIndexes.get(i)][pathIndexes.get(i + 1)];
        }
    }

    public Integer getPathCost() {
        return pathCost;
    }

    @Override
    public String toString() {
        return "PathSearchResult{" +
                "path=" + path +
                ", pathCost=" + pathCost +
                '}';
    }
}
