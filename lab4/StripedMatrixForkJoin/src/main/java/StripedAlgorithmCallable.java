import java.util.concurrent.Callable;

public class StripedAlgorithmCallable implements Callable<int[]> {
    private final int[] row;
    private int[][] multiplied;

    public StripedAlgorithmCallable(int[] row, int rowIndex, int[][] multiplied) {
        this.row = row;
        this.multiplied = multiplied;
    }

    @Override
    public int[] call() {
        int[] result = new int[multiplied.length];

        for (int j = 0; j < multiplied.length; j++) {
            for (int i = 0; i < row.length; i++) {
                result[j] += row[i] * multiplied[i][j];
            }
        }
        return result;
    }
}
