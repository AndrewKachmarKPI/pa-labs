package com.labs.domain;

import java.util.ArrayList;
import java.util.List;

public class PathSearchResult {
    private List<Integer> pathIndexes = new ArrayList<>();
    private StringBuilder path = new StringBuilder();
    private Integer pathCost = 0;

    public PathSearchResult(int startPosition) {
        addCityIndex(startPosition, false);
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


}
