package DSA._1Array_ArrayList;

/*
 * Author: Vishal Pawar
 *
 * Problem statement:
 * Create array/arrayList of (maxSize)1000 numbers, using random number generator function, number should be between 
 * 1000 to 10000 such that every number stored in array by random generator should be unique. Now Fnd the max number in array by 
 * using multithreading concept - define how much number of threads(numThreads) u will use and why? How much time takes for 
 * each thread to perform task? 
 */
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class optimizedCode1 {

    public static void main(String[] args) throws InterruptedException {

        // set number of threads dynamically
        // int numThreads = Runtime.getRuntime().availableProcessors();
        int numThreads = 7; // set number of threads to use
        int maxSize = 1000; // set maxSize of unique random numbers need to be generated

        SecureRandom random = new SecureRandom();

        // generate a list of unique random numbers
        List<Integer> list = random.ints(1000, 10001)
                .distinct()
                .limit(maxSize)
                .boxed()
                .collect(Collectors.toList());

        System.out.println("===================================================");
        System.out.println("Total threads used: " + numThreads);

        int sublistSize = list.size() / numThreads;
        int remaining = list.size() % numThreads;
        List<SublistMaxFinder> threads = new ArrayList<>(numThreads);
        AtomicInteger max = new AtomicInteger(Integer.MIN_VALUE);

        // create and submit tasks to the executor service
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        long startTime = System.nanoTime();
        for (int i = 0; i < numThreads; i++) {
            int start = i * sublistSize;
            int end = (i + 1) * sublistSize;
            if (i == numThreads - 1) {
                end += remaining;
            }

            SublistMaxFinder task = new SublistMaxFinder(list.subList(start, end), max);
            threads.add(task);
            executor.submit(task);
        }

        // wait for all tasks to complete
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.println("Maximum number: " + max.get());
        System.out.println("Total execution time: " + totalTime + " nano-secs");
        System.out.println("===================================================");

    }

    // define a task that finds the maximum value in a sublist
    public static class SublistMaxFinder implements Runnable {

        private List<Integer> list;
        private AtomicInteger max;

        public SublistMaxFinder(List<Integer> list, AtomicInteger max) {
            this.list = list;
            this.max = max;
        }

        public void run() {
            for (int num : list) {
                max.updateAndGet(currentMax -> Math.max(currentMax, num));
            }
        }
    }

}
