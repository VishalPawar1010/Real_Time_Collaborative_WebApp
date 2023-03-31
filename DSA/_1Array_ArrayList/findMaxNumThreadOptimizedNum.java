package DSA._1Array_ArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Get max number and print execution time for (1-20) threads used

public class findMaxNumThreadOptimizedNum {

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Integer> list = new ArrayList<>();
        Random rand = new Random();
        while (list.size() < 1000) {
            int num = rand.nextInt(9001) + 1000; // generates random number between 1000 and 10000
            if (!list.contains(num)) {
                list.add(num); // adds unique number to the list
            }
        }

        long[] times = new long[21]; // array to store execution times for each thread count
        for (int numThreads = 1; numThreads <= 20; numThreads++) {
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
            times[numThreads] = elapsedTime; // store elapsed time for current cycle

            // System.out.println("Number of threads: " + numThreads + ", maximum number: "
            // + max + ", elapsed time: "
            // + elapsedTime + " ns");
            System.out.println("Total execution time for " + numThreads + " threads: " + elapsedTime + " ns");
        }

        // print total execution times for each thread count
        // for (int i = 1; i <= 20; i++) {
        // System.out.println("Total execution time for " + i + " threads: " + times[i]
        // + " ns");
        // }
    }

    public static class SublistMaxFinder extends Thread {
        private List<Integer> list;
        private int max = Integer.MIN_VALUE;

        public SublistMaxFinder(List<Integer> list) {
            this.list = list;
        }

        public int getMax() {
            return max;
        }

        public void run() {
            for (int num : list) {
                if (num > max) {
                    max = num;
                }
            }
        }
    }

}
