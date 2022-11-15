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

    public void setPopulationNode(PopulationNode populationNode) {
        this.populationNode = populationNode;
    }

    public Boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    @Override
    public String toString() {
        return "MutationResponse{" +
                "populationNode=" + populationNode +
                ", isSuccessful=" + isSuccessful +
                '}';
    }
}
