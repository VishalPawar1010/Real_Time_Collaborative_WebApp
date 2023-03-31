package DSA._1Array_ArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class findMaxNumThreadNum {

    public static void main(String[] args) throws InterruptedException {

        // 1
        ArrayList<Integer> list = new ArrayList<>();
        // 2
        Random rand = new Random();
        while (list.size() < 1000) {
            int num = rand.nextInt(9001) + 1000; // generates random number between 1000 and 10000
            if (!list.contains(num)) {
                list.add(num); // adds unique number to the list
            }
        }
        // System.out.println("List of random no " + list);

        // 3 -> fix number of threads - subList size accordingly
        int numThreads = Runtime.getRuntime().availableProcessors(); // set number of threads dynamically
        System.out.println("Maximum number of CPU cores vaialable for machine: " + numThreads);
        int sublistSize = list.size() / numThreads; // size of each sublist
        ArrayList<SublistMaxFinder> threads = new ArrayList<>();

        // 4 create and start threads
        for (int i = 0; i < numThreads; i++) {
            SublistMaxFinder thread = new SublistMaxFinder(list.subList(i * sublistSize, (i + 1) * sublistSize));
            threads.add(thread);
            thread.start();
        }

        // 6 pre-setup for time measurement (after thread start)
        // long startTime = System.currentTimeMillis();
        long startTime = System.nanoTime();

        int max = Integer.MIN_VALUE;
        long[] threadTimes = new long[numThreads];

        // 7 - join threads and find maximum number among returned values

        // for (SublistMaxFinder thread : threads) {
        // thread.join();
        // int threadMax = thread.getMax();
        // if (threadMax > max) {
        // max = threadMax;
        // }
        // threadTimes[i] = threads.get(i).getElapsedTime();
        // }
        for (int i = 0; i < numThreads; i++) {
            threads.get(i).join();
            int threadMax = threads.get(i).getMax();
            if (threadMax > max) {
                max = threadMax;
            }
            threadTimes[i] = threads.get(i).getElapsedTime();
        }

        // long endTime = System.currentTimeMillis();
        long endTime = System.nanoTime();

        // 8 print out the results
        System.out.println("Maximum number: " + max);
        for (int i = 0; i < numThreads; i++) {
            System.out.println("Thread " + i + " time: " + threadTimes[i] + " ns");
        }
        // System.out.println("Time taken by each thread: " + (endTime - startTime) /
        // numThreads + " milli-s");
        System.out.println("Avg-Time taken by each thread: " + (endTime - startTime) / numThreads + " nano-secs");

    }

    // 5 create thread object
    public static class SublistMaxFinder extends Thread {
        private List<Integer> list;
        private int max = Integer.MIN_VALUE;
        private long elapsedTime;

        public SublistMaxFinder(List<Integer> list) {
            this.list = list;
        }

        public int getMax() {
            return max;
        }

        public long getElapsedTime() {
            return elapsedTime;
        }

        public void run() {
            long startTime = System.nanoTime();
            for (int num : list) {
                if (num > max) {
                    max = num;
                }
            }
            long endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
        }
    }

}
