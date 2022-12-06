package com.labs.domain;


import com.labs.enums.AntPlacementType;

public final class SalesmanProblemDto {
    private double A;
    private double B;
    private int L_MIN;
    private double R;
    private int numberOfOrdinaryAnts;
    private int numberOfEliteAnts = 0;
    private int numberOfWildAnts = 0;
    private int colonyLife = 1;
    private AntPlacementType antPlacementType;


    public SalesmanProblemDto() {
    }

    public double getA() {
        return A;
    }

    public double getB() {
        return B;
    }

    public int getL_MIN() {
        return L_MIN;
    }

    public double getR() {
        return R;
    }

    public int getNumberOfOrdinaryAnts() {
        return numberOfOrdinaryAnts;
    }

    public int getNumberOfEliteAnts() {
        return numberOfEliteAnts;
    }

    public int getNumberOfWildAnts() {
        return numberOfWildAnts;
    }

    public AntPlacementType getAntPlacementType() {
        return antPlacementType;
    }

    public int getColonyLife() {
        return colonyLife;
    }

    public static Builder builder() {
        return new SalesmanProblemDto().new Builder();
    }

    @Override
    public String toString() {
        return "SalesmanProblemDto{" +
                "A=" + A +
                ", B=" + B +
                ", L_MIN=" + L_MIN +
                ", R=" + R +
                ", numberOfAnts=" + numberOfOrdinaryAnts +
                ", antPlacementType=" + antPlacementType +
                '}';
    }


    public class Builder {
        public Builder() {
        }

        public Builder setA(double a) {
            SalesmanProblemDto.this.A = a;
            return this;
        }

        public Builder setB(double b) {
            SalesmanProblemDto.this.B = b;
            return this;
        }

        public Builder setL_MIN(int l_MIN) {
            SalesmanProblemDto.this.L_MIN = l_MIN;
            return this;
        }

        public Builder setR(double r) {
            SalesmanProblemDto.this.R = r;
            return this;
        }

        public Builder setNumberOfOrdinaryAnts(int antsLength) {
            SalesmanProblemDto.this.numberOfOrdinaryAnts = antsLength;
            return this;
        }

        public Builder setAntPlacementType(AntPlacementType placementType) {
            SalesmanProblemDto.this.antPlacementType = placementType;
            return this;
        }

        public Builder setNumberOfEliteAnts(int eliteAnts) {
            SalesmanProblemDto.this.numberOfEliteAnts = eliteAnts;
            return this;
        }

        public Builder setNumberOfWildAnts(int wildAnts) {
            SalesmanProblemDto.this.numberOfWildAnts = wildAnts;
            return this;
        }


        public Builder setColonyLife(int colonyLife) {
            SalesmanProblemDto.this.colonyLife = colonyLife;
            return this;
        }

        public SalesmanProblemDto build() {
            return SalesmanProblemDto.this;
        }
    }

}
