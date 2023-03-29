package DSA._3HashMap_HashSets;

import java.util.HashSet;

public class intersectionOf2Arrays {

    public static void intersection(int arr1[], int arr2[]) {

        HashSet<Integer> set1 = new HashSet<>();
        HashSet<Integer> set2 = new HashSet<>();

        // add any one arrays elements to set1
        for (int i = 0; i < arr1.length; i++) {
            set1.add(arr1[i]);
        }
        for (int j = 0; j < arr2.length; j++) {
            if (set1.contains(arr2[j])) {
                set2.add(arr2[j]);
                set1.remove(arr2[j]);

            }
        }

        System.out.println("===============================================");
        System.out.println("===>>> size of intersectionOf2Arrays is : " + set2.size());
        System.out.println("===>>> values in set intersectionOf2Arrays is : " + set2);
        System.out.println("===============================================");

    }

    public static void main(String[] args) {
        // ans is
        int arr1[] = { 7, 3, 9 };
        int arr2[] = { 6, 3, 9, 2, 9, 4 };

        intersection(arr1, arr2);
    }

}
