package dimakar.delivery.price;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DeliveryCalculatorTest {


    private static Stream<Arguments> negativeOrZeroDistance() {
        return Stream.of(
                Arguments.of(-1, true, true, DeliveryCalculator.WorkloadOfDeliveryService.HIGHEST),
                Arguments.of(-1, false, false, DeliveryCalculator.WorkloadOfDeliveryService.HIGH),
                Arguments.of(0, true, true, DeliveryCalculator.WorkloadOfDeliveryService.MIDDLE),
                Arguments.of(0, false, false, DeliveryCalculator.WorkloadOfDeliveryService.USUAL)
        );
    }

    private static Stream<Arguments> bigDistanceAndFragile() {
        return Stream.of(
                Arguments.of(30.1, true, true, DeliveryCalculator.WorkloadOfDeliveryService.USUAL),
                Arguments.of(100, false, true, DeliveryCalculator.WorkloadOfDeliveryService.HIGHEST),
                Arguments.of(30.1, false, true, DeliveryCalculator.WorkloadOfDeliveryService.HIGH)
        );
    }

    private static Stream<Arguments> correctArguments() {
        return Stream.of(
                Arguments.of(0.001, false, true, DeliveryCalculator.WorkloadOfDeliveryService.USUAL, 450),
                Arguments.of(2, false, true, DeliveryCalculator.WorkloadOfDeliveryService.HIGHEST, 720),
                Arguments.of(0.001, true, true, DeliveryCalculator.WorkloadOfDeliveryService.HIGH, 770),
                Arguments.of(2, true, false, DeliveryCalculator.WorkloadOfDeliveryService.MIDDLE, 400),
                Arguments.of(2.1, true, true, DeliveryCalculator.WorkloadOfDeliveryService.HIGH, 840),
                Arguments.of(10, false, true, DeliveryCalculator.WorkloadOfDeliveryService.MIDDLE, 600),
                Arguments.of(2.1, true, false, DeliveryCalculator.WorkloadOfDeliveryService.USUAL, 400),
                Arguments.of(10, false, false, DeliveryCalculator.WorkloadOfDeliveryService.HIGHEST, 400),
                Arguments.of(10.1, true, true, DeliveryCalculator.WorkloadOfDeliveryService.HIGHEST, 1120),
                Arguments.of(30, true, false, DeliveryCalculator.WorkloadOfDeliveryService.HIGH, 560),
                Arguments.of(10.1, false, true, DeliveryCalculator.WorkloadOfDeliveryService.MIDDLE, 720),
                Arguments.of(30, true, true, DeliveryCalculator.WorkloadOfDeliveryService.USUAL, 700),
                Arguments.of(30.1, true, false, DeliveryCalculator.WorkloadOfDeliveryService.MIDDLE, 600),
                Arguments.of(100, true, false, DeliveryCalculator.WorkloadOfDeliveryService.HIGHEST, 800)
        );
    }

    @ParameterizedTest
    @MethodSource("negativeOrZeroDistance")
    public void getDeliveryPriceNegativeOrZeroDistanceIllegalArgumentTest(double distance,
                                                                          boolean isLargeSize,
                                                                          boolean isFragile,
                                                                          DeliveryCalculator.WorkloadOfDeliveryService workload) {
        assertThatThrownBy(() ->
                DeliveryCalculator.getDeliveryPrice(distance, isLargeSize, isFragile, workload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("distance must be positive");
    }

    @ParameterizedTest
    @MethodSource("bigDistanceAndFragile")
    public void getDeliveryPriceBigDistanceAndFragileIllegalArgumentTest(double distance,
                                                                         boolean isLargeSize,
                                                                         boolean isFragile,
                                                                         DeliveryCalculator.WorkloadOfDeliveryService workload) {
        assertThatThrownBy(() ->
                DeliveryCalculator.getDeliveryPrice(distance, isLargeSize, isFragile, workload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Fragile goods cannot be transported over a distance of more than 30 km");
    }

    @ParameterizedTest
    @MethodSource("correctArguments")
    public void getDeliveryPriceCorrectArgumentsTest(double distance,
                                                     boolean isLargeSize,
                                                     boolean isFragile,
                                                     DeliveryCalculator.WorkloadOfDeliveryService workload,
                                                     double expectedPrice) {
        assertThat(DeliveryCalculator.getDeliveryPrice(distance, isLargeSize, isFragile, workload))
                .as("Price must be equal " + expectedPrice)
                .isEqualTo(expectedPrice);
    }

}
