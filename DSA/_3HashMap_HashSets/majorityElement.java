package DSA._3HashMap_HashSets;

import java.util.HashMap;

// problem - majority element
// to find the element which has occurred in array more that n/3 times.
// n is size of array

public class majorityElement {
    public static void majorityElement(int nums[]) {

        // create hashmap
        HashMap<Integer, Integer> map = new HashMap<>();
        int n = nums.length;

        // to put value in HashMap as key=element , value = count/freq
        for (int i = 0; i < n; i++) {
            if (map.containsKey(nums[i])) {
                map.put(nums[i], map.get(nums[i]) + 1);
            } else {
                map.put(nums[i], 1);
            }
        }

        // check if count/freq is greater than n/3 and print it
        for (int key : map.keySet()) {
            if (map.get(key) > n / 3) {
                System.out.println("===============================================");
                System.out.println("===>>> Element occured more than n/3 is : " + key);
                System.out.println("===============================================");
            }

        }

    }

    public static void main(String[] args) {
        // size = n

        int nums[] = { 1, 3, 2, 5, 1, 3, 1, 5, 1 }; // ans 1
        // int nums1[] = { 1, 2 }; // ans 1,2
        majorityElement(nums);
        // majorityElement(nums1);

    }

}
