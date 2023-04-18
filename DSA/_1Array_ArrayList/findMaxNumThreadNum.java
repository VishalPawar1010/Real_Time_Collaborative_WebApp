package DSA._1Array_ArrayList;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Author - Vishal Pawar
// Problem statement :  create array/arrayList of 1000 numbers, using random number generator function, number should be between 
// 1000 to 10000 such that every number stored in array by random generator should be unique. Now Fnd the max number in array by 
// using multithreading concept - define how much number of threads u will use and why? How much time takes for each thread to perform task? 

public class findMaxNumThreadNum {

    public static void main(String[] args) throws InterruptedException {

        int numThreads = 7; // set number of threads to use
        int maxSize = 1000; // set maxSize of unique random numbers need to be generated

        // set number of threads dynamically
        // int numThreads = Runtime.getRuntime().availableProcessors();

        SecureRandom random = new SecureRandom();
        List<Integer> list = random.ints(1000, 10001)
                .limit(maxSize)
                .boxed()
                .collect(Collectors.toList());

        System.out.println("==================================================================");
        // System.out.println("List size " + list.size());
        System.out.println("Total threads used " + numThreads);
        int sublistSize = list.size() / numThreads;
        int remaining = list.size() % numThreads;
        ArrayList<SublistMaxFinder> threads = new ArrayList<>();
        long startTime = System.nanoTime();

        // 4 create and start threads
        for (int i = 0; i < numThreads; i++) {

            SublistMaxFinder thread = new SublistMaxFinder(list.subList(i * sublistSize, (i + 1) * sublistSize));
            // System.out.println(start + " to " + end);
            threads.add(thread);
            thread.start();
        }

        int max = Integer.MIN_VALUE;

        for (int i = 0; i < numThreads; i++) {
            threads.get(i).join();
            int threadMax = threads.get(i).getMax();
            if (threadMax > max) {
                max = threadMax;
            }
        }
        long endTime = System.nanoTime();

        System.out.println("Maximum number: " + max);
        System.out.println("Total execution time : " + (endTime - startTime) + " nano-secs");
        System.out.println("==================================================================");

    }

    // 5 create thread object
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
