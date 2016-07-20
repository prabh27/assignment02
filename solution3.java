/**
 * Created by prabh on 20/07/16.
 *
 * Thread.start() starts a thread, while Runnable.run() just calls a method.
 *
 */

import java.lang.Runnable;


public class solution3 {

    public static void main(String args[]) {
        Thread myThread = new Thread(new MyRunnable());
        myThread.start();
    }
}


class MyRunnable implements Runnable {

    public void run(){
        System.out.println("MyRunnable running");
    }
}