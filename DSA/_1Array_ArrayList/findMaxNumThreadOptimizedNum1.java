package DSA._1Array_ArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Get optimized thread count which takes min execution time among (1-20) nu of threads

public class findMaxNumThreadOptimizedNum1 {

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Integer> list = new ArrayList<>();
        Random rand = new Random();
        while (list.size() < 1000) {
            int num = rand.nextInt(9001) + 1000; // generates random number between 1000 and 10000
            if (!list.contains(num)) {
                list.add(num); // adds unique number to the list
            }
        }

        long[] minThreadTimes = new long[21];
        int minThreadNum = 0;
        long minTime = Long.MAX_VALUE; // array to store execution times for each thread count
        for (int numThreads = 1; numThreads <= 20; numThreads++) {
            long totalTime = 0;
            long[] threadTimes = new long[numThreads];
            int sublistSize = list.size() / numThreads; // size of each sublist
            ArrayList<SublistMaxFinder> threads = new ArrayList<>();

            long startTime = System.nanoTime(); // start time for current cycle

            // create and start threads
            for (int i = 0; i < numThreads; i++) {
                SublistMaxFinder thread = new SublistMaxFinder(list.subList(i * sublistSize, (i + 1) * sublistSize));
                threads.add(thread);
                thread.start();
            }

            int max = Integer.MIN_VALUE;
            // join threads and find maximum number among returned values
            for (SublistMaxFinder thread : threads) {
                thread.join();
                int threadMax = thread.getMax();
                if (threadMax > max) {
                    max = threadMax;
                }

            }
            long endTime = System.nanoTime(); // end time for current cycle
            long elapsedTime = endTime - startTime; // total elapsed time for current cycle
            // times[numThreads] = elapsedTime; // store elapsed time for current cycle
            // System.out.println("Total execution time for " + numThreads + " threads: " +
            // elapsedTime + " ns");

            for (int i = 0; i < numThreads; i++) {
                threads.get(i).join();
                threadTimes[i] = threads.get(i).getElapsedTime();
                totalTime += threadTimes[i];
                System.out.println("Total execution time for " + numThreads + " threads: " + totalTime + " ns");
            }

            if (totalTime < minTime) {
                minTime = totalTime;
                minThreadNum = numThreads;
                minThreadTimes = threadTimes;
            }

            // long endTime = System.nanoTime(); // end time for current cycle
            // long elapsedTime = endTime - startTime; // total elapsed time for current
            // cycle

            // times[numThreads] = elapsedTime; // store elapsed time for current cycle

            // System.out.println("Number of threads: " + numThreads + ", maximum number: "
            // + max + ", elapsed time: "
            // + elapsedTime + " ns");
            // System.out.println("Total execution time for " + numThreads + " threads: " +
            // elapsedTime + " ns");
        }
        System.out.println("Minimum execution time is " + minTime + " ns for " + minThreadNum + " threads.");

        // print total execution times for each thread count
        // for (int i = 1; i <= 20; i++) {
        // System.out.println("Total execution time for " + i + " threads: " + times[i]
        // + " ns");
        // }
    }

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
