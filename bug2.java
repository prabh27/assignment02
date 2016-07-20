/**
 * Created by prabh on 20/07/16.
 *
 */

/**
 *  NP_GUARANTEED_DEREF_ON_EXCEPTION_PATH
 *  A reference to null should never be dereferenced/accessed.
 *  Doing so will cause a NullPointerException to be thrown.
 */

public class bug2 {
    public static void main(String args[]) {
        bug2 b = new bug2();
        if(b.getName().length() == 0){
            System.out.print("length is 0");
        }
    }
    public String getName() {
        return null;
    }
}
