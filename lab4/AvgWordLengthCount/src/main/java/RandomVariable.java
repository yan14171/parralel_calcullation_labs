import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomVariable {

    public static void calculateAndPrint(List<Integer> list) {

        // Calculate PMF
        Map<Integer, Double> pmf = new HashMap<>();
        int n = list.size();
        for(int i = 0; i < n; i++) {
            int x = list.get(i);
            pmf.put(x, pmf.getOrDefault(x, 0.0) + 1.0 / n);
        }
        System.out.println("PMF: " + pmf);

        // Calculate expected value
        double expectedValue = 0.0;
        for(int x : pmf.keySet()) {
            expectedValue += x * pmf.get(x);
        }
        System.out.println("Expected value: " + expectedValue);

        // Calculate variance
        double variance = 0.0;
        for(int x : pmf.keySet()) {
            variance += Math.pow(x - expectedValue, 2) * pmf.get(x);
        }
        System.out.println("Variance: " + variance);

        // Calculate standard deviation
        double standardDeviation = Math.sqrt(variance);
        System.out.println("Standard deviation: " + standardDeviation);
    }

}
