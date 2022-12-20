package com.labs.domain;

public class MutationResponse {
    private PopulationNode populationNode;
    private Boolean isSuccessful;

    public MutationResponse(PopulationNode populationNode, Boolean isSuccessful) {
        this.populationNode = populationNode;
        this.isSuccessful = isSuccessful;
    }

    public PopulationNode getPopulationNode() {
        return populationNode;
    }

    public Boolean isSuccessful() {
        return isSuccessful;
    }
}
