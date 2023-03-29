package DSA._3HashMap_HashSets;

import java.util.HashSet;

//unionOf2Arrays
// given are 2 arrays
// find and print sizee of arr which is unionOf2Arrays
public class unionOf2Arrays {

    public static void unionOf2Arrays(int arr1[], int arr2[]) {

        HashSet<Integer> set1 = new HashSet<>();

        for (int i = 0; i < arr1.length; i++) {
            set1.add(arr1[i]);
        }
        for (int i = 0; i < arr2.length; i++) {
            set1.add(arr2[i]);
        }

        System.out.println("===============================================");
        System.out.println("===>>> size of unionOf2Arrays is : " + set1.size());
        System.out.println("===>>> values in set unionOf2Arrays is : " + set1);
        System.out.println("===============================================");

    }

    public static void main(String[] args) {
        // ans is 6
        int arr1[] = { 7, 3, 9 };
        int arr2[] = { 6, 3, 9, 2, 9, 4 };

        unionOf2Arrays(arr1, arr2);
    }
}
