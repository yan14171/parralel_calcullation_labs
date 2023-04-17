import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelStripeForkMatrixMultiplication extends RecursiveAction {
    private static final int STRIPE_SIZE = 150;
    private final int[][] A;
    private final int[][] B;
    private final int[][] C;
    private final int startRow;
    private final int endRow;

    public ParallelStripeForkMatrixMultiplication(int[][] A, int[][] B, int[][] C, int startRow, int endRow) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    protected void compute() {
        if (endRow - startRow <= STRIPE_SIZE) {
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < B[0].length; j++) {
                    for (int k = 0; k < A[0].length; k++) {
                        C[i][j] += A[i][k] * B[k][j];
                    }
                }
            }
        } else {
            int mid = (startRow + endRow) / 2;
            invokeAll(new ParallelStripeForkMatrixMultiplication(A, B, C, startRow, mid),
                    new ParallelStripeForkMatrixMultiplication(A, B, C, mid, endRow));
        }
    }

    public static void multiply(int[][] A, int[][] B, int[][] C) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ParallelStripeForkMatrixMultiplication(A, B, C, 0, A.length));
        pool.shutdown();
    }
}