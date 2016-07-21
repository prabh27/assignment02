/**
 * Created by prabh on 20/07/16.
 */

/**
 *  Bug1 : findbugs:LI_LAZY_INIT_UPDATE_STATIC
 *  Creating an instance of the class.
 *  This will fail in the multithreaded environment.
 *  The risk of the race condition is in the if condition of the getInstance() method.
 *  The first call enters the function. However, it is possible for another thread to get into the function while
 *  first one is already there. This will create two instances of the static object.
 *
 */
public class bug1 {

    private static bug1 b1 = null;

    public static final bug1 getInstance()
    {
        if ( b1 == null )
            b1 = new bug1();
        return b1;
    }

    public static void main(String args[]) {
        b1 = getInstance();
    }


}


