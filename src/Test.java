public class Test {
    public static void main(String[] args) {
        double x = 1.45465;
        double y;

        y = (double) Math.round(x * 10000) / 10000;
        System.out.println(y);
    }
}
