package com.labs.domain;


import com.labs.enums.AntPlacementType;

public final class SalesmanProblemDto {
    private int A;
    private int B;
    private int L_MIN;
    private double R;
    private int numberOfAnts;
    private int numberOfEliteAnts = 0;
    private AntPlacementType antPlacementType;


    public SalesmanProblemDto() {
    }

    public int getA() {
        return A;
    }

    public int getB() {
        return B;
    }

    public int getL_MIN() {
        return L_MIN;
    }

    public double getR() {
        return R;
    }

    public int getNumberOfAnts() {
        return numberOfAnts;
    }

    public int getNumberOfEliteAnts() {
        return numberOfEliteAnts;
    }

    public AntPlacementType getAntPlacementType() {
        return antPlacementType;
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
                ", numberOfAnts=" + numberOfAnts +
                ", antPlacementType=" + antPlacementType +
                '}';
    }


    public class Builder {
        public Builder() {
        }

        public Builder setA(int a) {
            SalesmanProblemDto.this.A = a;
            return this;
        }

        public Builder setB(int b) {
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

        public Builder setNumberOfAnts(int antsLength) {
            SalesmanProblemDto.this.numberOfAnts = antsLength;
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

        public SalesmanProblemDto build() {
            return SalesmanProblemDto.this;
        }
    }

}
