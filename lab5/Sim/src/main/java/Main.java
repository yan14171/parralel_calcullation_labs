import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        double[] result;
        SMS singleTask;

        List<Future<double[]>> results;
        List<Callable<double[]>> tasks;

//        System.out.println("Single task results:");
//        singleTask = new SMS(100);
//        result = singleTask.call();
//        System.out.println(result[0]);
//        System.out.println(result[1]);




        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SMS t = new SMS(100);
            tasks.add(t);
        }
        results = executor.invokeAll(tasks);
        executor.shutdown();
        double avgMessagesinQueue = 0;
        double failures = 0;
        for(Future<double[]> taskResult : results) {
            result = taskResult.get();
            avgMessagesinQueue += result[1];
            failures += result[0];
        }
        System.out.println("Parallel runs results:");
        System.out.println(failures / results.size());
        System.out.println(avgMessagesinQueue / results.size());
    }
}