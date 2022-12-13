import com.labs.domain.PopulationNode;
import com.labs.services.KnapsackProblemService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class GeneticAlgorithmTest {
    @Test
    public void Check_PackKnapsack_Succeeds() {
        //Arrange
        int weight = 150;
        int iterations = 1000;
        KnapsackProblemService service = new KnapsackProblemService(weight);
        //Act
        PopulationNode knapsack = service.getPackedKnapsack(iterations);
        //Assert
        assertThat(knapsack.getTotalWeight()).isLessThanOrEqualTo(weight);
        assertThat(knapsack.getTotalPrice()).isNotZero();
    }

    @Test
    public void Check_PackKnapsack_Succeeds_When_BigCapacity() {
        //Arrange
        int weight = 1000;
        int iterations = 1000;
        KnapsackProblemService service = new KnapsackProblemService(weight);
        //Act
        PopulationNode knapsack = service.getPackedKnapsack(iterations);
        //Assert
        assertThat(knapsack.getTotalWeight()).isLessThanOrEqualTo(weight);
        assertThat(knapsack.getTotalPrice()).isNotZero();
    }

    @Test
    public void Check_PackKnapsack_Fails_When_ZeroCapacity() {
        //Arrange
        int iterations = 1000;
        KnapsackProblemService service = new KnapsackProblemService(0);
        //Act
        assertThatThrownBy(() -> service.getPackedKnapsack(iterations))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Capacity should not be 0 or less");
    }

    @Test
    public void Check_PackKnapsack_Fails_When_NegativeCapacity() {
        //Arrange
        int iterations = 1000;
        KnapsackProblemService service = new KnapsackProblemService(-100);
        //Act
        assertThatThrownBy(() -> service.getPackedKnapsack(iterations))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Capacity should not be 0 or less");
    }
}
