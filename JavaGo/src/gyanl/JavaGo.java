import java.io.IOException;

public class JavaGo {

    public int add(int i1, int i2) {
        return i1 + i2;
    }

    public static void main(String[] args) {
        int sum = new JavaGo().add(1, 1);
        System.out.println(sum);
        System.out.println("please press any key:");
        try {
            int read = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
