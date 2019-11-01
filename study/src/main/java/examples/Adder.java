package examples;

public class Adder {
    public static void main(final String[] args) {
        Adder adder = new Adder();
        int result = adder.add(5, 2);
        System.out.println(result);
    }

    private int add(final int i, final int j) {
        return i + j;
    }
}
