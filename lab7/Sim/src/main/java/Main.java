import com.google.common.base.Stopwatch;
import mpi.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Main {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        exp(rank, size);

        int n = 12;
        double[] A = generateRandomMatrix(n, n, 10, 20);
        double[] B = generateRandomMatrix(n, n, 10, 20);
        double[] C = new double[n*n];

//        CollectiveMultiply(rank, size, n, A, B, C);
//        if(rank == 0)
//        {
//            System.out.println("Matrix A:");
//            print(to2D(A, n, n));
//            System.out.println("Matrix B:");
//            print(to2D(B, n, n));
//            System.out.println("Result:");
//            print(to2D(C, n, n));
//        }
        MPI.Finalize();
    }
    private static void SyncMultiply(int rank, int size, int n, double[] A, double[] B, double[] C) {
        int rowsPerProcess = n / size;

        if (rank == 0) {
            for (int dest = 1; dest < size; dest++) {
                MPI.COMM_WORLD.Send(A, dest*rowsPerProcess* n, rowsPerProcess* n, MPI.DOUBLE, dest, 0);
            }
        } else {
            MPI.COMM_WORLD.Recv(A, rank *rowsPerProcess* n, rowsPerProcess* n, MPI.DOUBLE, 0, 0);
        }
        for (int source = 0; source < size; source++) {
            if (rank == source) {
                MPI.COMM_WORLD.Send(B, 0, n * n, MPI.DOUBLE, (rank +1)% size, 0);
            } else if (rank == (source+1)% size) {
                MPI.COMM_WORLD.Recv(B, 0, n * n, MPI.DOUBLE, source, 0);
            }
            MPI.COMM_WORLD.Barrier();
        }

        for (int i = rank *rowsPerProcess; i < (rank +1)*rowsPerProcess; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    C[i* n +j] += A[i* n +k] * B[k* n +j];
                }
            }
        }
        if (rank == 0) {
            for (int source = 1; source < size; source++) {
                MPI.COMM_WORLD.Recv(C, source*rowsPerProcess* n, rowsPerProcess* n, MPI.DOUBLE, source, 0);
            }
        } else {
            MPI.COMM_WORLD.Send(C, rank *rowsPerProcess* n, rowsPerProcess* n, MPI.DOUBLE, 0, 0);
        }
    }
    private static void AsyncMultiply(int rank, int size, int n, double[] A, double[] B, double[] C) {
        int rowsPerProcess = n / size;
        Request[] reqs = new Request[5];

        if (rank == 0) {
            for (int dest = 1; dest < size; dest++) {
                reqs[0] = MPI.COMM_WORLD.Isend(A, dest * rowsPerProcess * n, rowsPerProcess * n, MPI.DOUBLE, dest, 0);
            }
        } else {
            MPI.COMM_WORLD.Irecv(A, rank *rowsPerProcess* n, rowsPerProcess* n, MPI.DOUBLE, 0, 0).Wait();
        }
        for (int source = 0; source < size; source++) {
            if (rank == source) {
                MPI.COMM_WORLD.Isend(B, 0, n * n, MPI.DOUBLE, (rank +1)% size, 0);
            } else if (rank == (source+1)% size) {
                MPI.COMM_WORLD.Irecv(B, 0, n * n, MPI.DOUBLE, source, 0).Wait();
            }
        }
        for (int i = rank *rowsPerProcess; i < (rank +1)*rowsPerProcess; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    C[i* n +j] += A[i* n +k] * B[k* n +j];
                }
            }
        }
        if (rank == 0) {
            for (int source = 1; source < size; source++) {
                MPI.COMM_WORLD.Recv(C, source*rowsPerProcess* n, rowsPerProcess* n, MPI.DOUBLE, source, 0);
            }
        } else {
            MPI.COMM_WORLD.Isend(C, rank *rowsPerProcess* n, rowsPerProcess* n, MPI.DOUBLE, 0, 0);
        }
    }
    public static void CollectiveMultiply(int rank, int size, int n, double[] a, double[] b, double[] c) {
        int rowsPerProcess = n / size;

        double[] blockMultiplied = new double[rowsPerProcess * n];
        double[] result = new double[rowsPerProcess * n];

        MPI.COMM_WORLD.Scatter(a, 0, rowsPerProcess * n, MPI.DOUBLE, blockMultiplied, 0, rowsPerProcess * n, MPI.DOUBLE, 0);

        MPI.COMM_WORLD.Bcast(b, 0, n * n, MPI.DOUBLE, 0);

        for (int i = 0; i < rowsPerProcess; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i * n + j] += blockMultiplied[i * n + k] * b[k * n + j];
                }
            }
        }

        MPI.COMM_WORLD.Gather(result, 0, rowsPerProcess * n, MPI.DOUBLE, c, 0, rowsPerProcess * n, MPI.DOUBLE, 0);
    }


    private static void print(double[][] m) {
        for(int i = 0; i < m.length; i++)
        {
            for(int j = 0; j < m[i].length; j++)
                System.out.print(" " + m[i][j]);
            System.out.println();
        }
    }
    public static double[][] to2D(double[] arr, int rows, int cols) {
        if (arr.length != rows * cols) {
            throw new IllegalArgumentException("Invalid array size");
        }
        double[][] mat = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat[i][j] = arr[i * cols + j];
            }
        }
        return mat;
    }
    public static double[] generateRandomMatrix(int numRows, int numCols, int minValue, int maxValue) {
        double[] matrix = new double[numRows * numCols];
        Random rand = new Random();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix[i * numCols + j] = (double)rand.nextInt(maxValue - minValue + 1) + minValue;
            }
        }
        return matrix;
    }
    public static void exp(int rank, int size)
    {
        if(rank == 0)
            System.out.println("Results for " + size + "processors");
        int experC = 2;
        List<Integer> ns = new ArrayList();
        ns.add(500);
        ns.add(1000);
        ns.add(1500);
        ns.add(1750);

        for(int n: ns) {

            double[] A = generateRandomMatrix(n, n, 10, 20);
            double[] B = generateRandomMatrix(n, n, 10, 20);
            double[] C = new double[n*n];

            Stopwatch s = Stopwatch.createStarted();
            for(int i = 0; i < experC; i++)
            {
                CollectiveMultiply(rank, size, n, A, B, C);
            }
            MPI.COMM_WORLD.Barrier();
            if(rank == 0)
                System.out.println(s.elapsed().toMillis()/experC + " : Result for " + n + " elements");
        }
    }
}
