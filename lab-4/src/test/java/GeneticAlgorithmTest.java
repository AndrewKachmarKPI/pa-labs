import com.labs.domain.PopulationNode;
import com.labs.services.KnapsackProblemService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class GeneticAlgorithmTest {
    private static final int iterations = 1000;

    @Test
    public void packKnapsackSucceeds() {
//      arrange
        int weight = 150;
        KnapsackProblemService service = new KnapsackProblemService(weight);
//      act
        PopulationNode knapsack = service.getPackedKnapsack(iterations);
//      assert
        assertThat(knapsack.getTotalWeight()).isLessThanOrEqualTo(weight);
        assertThat(knapsack.getTotalPrice()).isNotZero();
    }

    @Test
    public void packKnapsackSucceedsWhenBigCapacity() {
//      arrange
        int weight = 1000;
        KnapsackProblemService service = new KnapsackProblemService(weight);
//      act
        PopulationNode knapsack = service.getPackedKnapsack(iterations);
//      assert
        assertThat(knapsack.getTotalWeight()).isLessThanOrEqualTo(weight);
        assertThat(knapsack.getTotalPrice()).isNotZero();
    }

    @Test
    public void packKnapsackFailsWhenZeroCapacity() {
        //      arrange
        KnapsackProblemService service = new KnapsackProblemService(0);
        //      act
        assertThatThrownBy(() -> service.getPackedKnapsack(iterations))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Capacity should not be 0 or less");
    }

    @Test
    public void packKnapsackFailsWhenNegativeCapacity() {
        //      arrange
        KnapsackProblemService service = new KnapsackProblemService(-100);
        //      act
        assertThatThrownBy(() -> service.getPackedKnapsack(iterations))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Capacity should not be 0 or less");
    }
}
