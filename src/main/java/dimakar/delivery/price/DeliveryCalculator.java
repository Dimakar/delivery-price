package dimakar.delivery.price;

public class DeliveryCalculator {

    public static double getDeliveryPrice(double distance,
                                          boolean isLargeSize,
                                          boolean isFragile,
                                          WorkloadOfDeliveryService workload) {
        double price;
        if (distance > 30) {
            if (isFragile) {
                throw new IllegalArgumentException("Fragile goods cannot be transported over a distance of more than 30 km");
            }
            price = 300;
        } else if (distance > 10) {
            price = 200;
        } else if (distance > 2) {
            price = 100;
        } else if (distance > 0) {
            price = 50;
        } else {
            throw new IllegalArgumentException("distance must be positive");
        }

        price = price + (isLargeSize ? 200 : 100);
        if (isFragile) price = price + 300;

        price = price * workload.getMultiplier();

        if (price < 400) {
            price = 400;
        }
        return price;
    }


    public enum WorkloadOfDeliveryService {
        HIGHEST(1.6, "Очень высокая загруженность"),
        HIGH(1.4, "Высокая загруженность"),
        MIDDLE(1.2, "Повышенная загруженность"),
        USUAL(1.0, "Нормальная загруженность");

        String displayName;
        double multiplier;

        WorkloadOfDeliveryService(double multiplier, String displayName) {
            this.multiplier = multiplier;
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public double getMultiplier() {
            return multiplier;
        }
    }
}
