import com.labs.domain.PopulationNode;
import com.labs.services.KnapsackProblemService;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeneticAlgorithmTest {
    @Test
    void Check_PackKnapsack_Succeeds() {
        //Arrange
        int capacity = 150;
        int iterations = 1000;
        KnapsackProblemService service = new KnapsackProblemService(capacity);
        //Act
        PopulationNode knapsack = service.getPackedKnapsack(iterations);
        //Assert
        assertThat(knapsack.getTotalWeight()).isLessThanOrEqualTo(capacity);
        assertThat(knapsack.getTotalPrice()).isNotZero();
    }

    @Test
    void Check_PackKnapsack_Succeeds_When_BigCapacity() {
        //Arrange
        int capacity = 1000;
        int iterations = 1000;
        KnapsackProblemService service = new KnapsackProblemService(capacity);
        //Act
        PopulationNode knapsack = service.getPackedKnapsack(iterations);
        //Assert
        assertThat(knapsack.getTotalWeight()).isLessThanOrEqualTo(capacity);
        assertThat(knapsack.getTotalPrice()).isNotZero();
    }

    @Test
    void Check_PackKnapsack_Fails_When_ZeroCapacity() {
        //Arrange
        int capacity = 0;
        int iterations = 1000;
        KnapsackProblemService service = new KnapsackProblemService(capacity);
        //Act
        Exception exception = assertThrows(RuntimeException.class, () -> service.getPackedKnapsack(iterations));
        //Assert
        assertThat(exception.getMessage()).isEqualTo("Capacity should not be 0 or less");
    }

    @Test
    void Check_PackKnapsack_Fails_When_NegativeCapacity() {
        //Arrange
        int capacity = -100;
        int iterations = 1000;
        KnapsackProblemService service = new KnapsackProblemService(capacity);
        //Act
        Exception exception = assertThrows(RuntimeException.class, () -> service.getPackedKnapsack(iterations));
        //Assert
        assertThat(exception.getMessage()).isEqualTo("Capacity should not be 0 or less");
    }
}
