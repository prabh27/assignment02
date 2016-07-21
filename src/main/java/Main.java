import java.util.logging.Logger;

/**
 * Created by prabh on 20/07/16.
 */
public class Main {
    private static final Logger log = Logger.getLogger (SimpleMain.class.getName ());

    public static void main(String args[]) {
        Main f = new Main();
        int a = f.add(1, 2);
        int b = f.multiply(2, 3);
    }

    public int add(int a, int b) {
        log.info("entering add method: ");
        return a + b;
    }

    private int multiply(int a, int b) {
        log.info("entering multiply method: ");
        return a*b;
    }


}