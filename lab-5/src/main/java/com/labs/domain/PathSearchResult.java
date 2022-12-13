package com.labs.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class PathSearchResult {
    private List<Integer> pathIndexes = new ArrayList<>();
    private StringBuilder path = new StringBuilder();
    private Integer pathCost = Integer.MAX_VALUE;
    private String antId;

    public PathSearchResult(int startPosition, String antId) {
        if (antId.isEmpty() || startPosition < 0) {
            throw new RuntimeException("Invalid path search result params");
        }
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

    public void countPathCost(int[][] distances) {
        this.pathCost = 0;
        for (int i = 0; i < pathIndexes.size() - 1; i++) {
            this.pathCost += distances[pathIndexes.get(i)][pathIndexes.get(i + 1)];
        }
    }
}
