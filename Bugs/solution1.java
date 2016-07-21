/**
 * Created by prabh on 20/07/16.
 */

/**
 * Solution: Use Initialization on demand holder idiom : Lazy initialization.
 * When the class goes through initialization, it is guaranteed to be serial.
 * So, we create a class LazyHolder, which will be initialized when we call the function getInstance().
 *
 */
public class solution1 {

    public static class LazyHolder {
        private static final solution1 s1= new solution1();
    }

    public static final solution1 getInstance() {
        return LazyHolder.s1;
    }
}
