package com.labs.domain;

import com.labs.enums.AntPlacementType;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AntAlgorithmParams {
    private double alpha;
    private double betta;
    private int orderOfPrice;
    private double evaporation;
    private int numberOfOrdinaryAnts;
    private int numberOfEliteAnts = 0;
    private int numberOfWildAnts = 0;
    private int colonyLife = 1;
    private AntPlacementType antPlacementType;

    public void checkParamsValidity() {
        if (this.alpha < 0.0 || this.betta < 0.0 || this.orderOfPrice < 0 || this.evaporation < 0 || this.numberOfOrdinaryAnts < 0 ||
                this.numberOfEliteAnts < 0 || this.numberOfWildAnts < 0 || this.colonyLife <= 0 || this.antPlacementType == null) {
            throw new RuntimeException("AntAlgorithmParams invalid");
        }
    }
}
