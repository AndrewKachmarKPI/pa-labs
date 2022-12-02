package com.labs;

import com.labs.domain.PopulationNode;
import com.labs.services.KnapsackProblemService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class GeneticAlgorithmTest {
    private static final int iterations = 1000;

    @Test
    public void knapsackProblemServicePositiveTestDefaultSize() {
        int weight = 150;
        KnapsackProblemService service = new KnapsackProblemService(weight);
        PopulationNode populationNode = service.searchLoop(iterations);
        assertThat(populationNode.getTotalWeight()).isEqualTo(weight);
        assertThat(populationNode.getTotalPrice()).isNotZero();
    }

    @Test
    public void knapsackProblemServicePositiveTestBigSize() {
        int weight = 1000;
        KnapsackProblemService service = new KnapsackProblemService(weight);
        PopulationNode populationNode = service.searchLoop(iterations);
        assertThat(populationNode.getTotalWeight()).isLessThanOrEqualTo(weight);
        assertThat(populationNode.getTotalPrice()).isNotZero();
        System.out.println(populationNode.getTotalWeight());
    }

    @Test(expected = RuntimeException.class)
    public void knapsackProblemServiceNegativeTestZeroCapacity() {
        KnapsackProblemService service = new KnapsackProblemService(0);
        service.searchLoop(iterations);
    }

    @Test(expected = RuntimeException.class)
    public void knapsackProblemServiceNegativeTestNegativeCapacity() {
        KnapsackProblemService service = new KnapsackProblemService(-100);
        service.searchLoop(iterations);
    }

}
