/**
 * Created by prabh on 20/07/16.
 *
 * squid:S1217: Thread.run() and Runnable.run() should not be called directly.
 * The purpose of the Thread.run() and Runnable.run() methods is to execute code
 * in a separate, dedicated thread.
 * Calling those methods directly doesn't make sense because it causes their code
 * to be executed in the current thread.
 *
 *
 */
public class bug3 {
    public static void main(String args[]) {
        Thread myThread = new Thread(new MyRunnable());
        myThread.run();
    }
}
