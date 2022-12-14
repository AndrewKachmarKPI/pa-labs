package com.labs.domain;


import com.labs.enums.AntPlacementType;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AntAlgorithmParams {
    private double A;
    private double B;
    private int L_MIN;
    private double R;
    private int numberOfOrdinaryAnts;
    private int numberOfEliteAnts = 0;
    private int numberOfWildAnts = 0;
    private int colonyLife = 1;
    private AntPlacementType antPlacementType;

    public void checkParamsValidity() {
        if (this.A < 0.0 || this.B < 0.0 || this.L_MIN < 0 || this.R < 0 || this.numberOfOrdinaryAnts < 0 ||
                this.numberOfEliteAnts < 0 || this.numberOfWildAnts < 0 || this.colonyLife <= 0 || this.antPlacementType == null) {
            throw new RuntimeException("AntAlgorithmParams invalid");
        }
    }
}
