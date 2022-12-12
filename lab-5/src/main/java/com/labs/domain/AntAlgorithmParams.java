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
}
