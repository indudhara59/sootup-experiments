public class SampleTarget {
    public static void main(String[] args) {
        SampleTarget target = new SampleTarget();
        int result = target.add(2, 3);
        target.printResult(result);
    }

    public int add(int left, int right) {
        return left + right;
    }

    public void printResult(int value) {
        String message = format(value);
        System.out.println(message);
    }

    private String format(int value) {
        return "Result: " + value;
    }
}
