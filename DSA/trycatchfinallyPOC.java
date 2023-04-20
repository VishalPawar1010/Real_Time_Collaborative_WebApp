package DSA;

public class trycatchfinallyPOC {
    public static int divide(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            return -1;
        } finally {
            return 0;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int result = divide(10, 0);
        System.out.println(result);
    }

}
