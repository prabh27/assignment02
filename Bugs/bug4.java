/**
 * Created by prabh on 20/07/16.
 */

/**
 * squid:S2275 : Printf-style format strings should not lead to unexpected behavior at runtime.
 * printf statements are executed at runtime, rather than compile time, they can contain errors that
 * lead to unexpected behaviours or runtime errors.
 *
 */

import java.util.Formatter;
import java.lang.String;
import java.io.PrintStream;
public class bug4 {
    public static void main(String args[]) {
        String a = String.format("The value of my integer is %d", "Hello World");  // Incorrect
        System.out.print(a);
        String b = String.format("The value of my integer is %d", 3);   // Correct
        System.out.print(b);
    }
}
