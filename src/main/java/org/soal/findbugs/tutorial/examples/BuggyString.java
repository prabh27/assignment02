package org.soal.findbugs.tutorial.examples;

import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Logger;

public class BuggyString
{
    private static Logger log = Logger.getLogger(BuggyString.class.getName());
    
    public void printLongName(String name, PrintStream stream)
    {
        log.entering(BuggyString.class.getName(), "hello", name.length());
        
        if (name != null && name.length() > 23)
        {
            stream.println("Long name '" + name + "' is long.");
        }
    }
    
    public boolean stringCompare_bad()
    {
        String string1 = new Date().toString();
        String string2 = new Date().toString();
        Thread t = new Thread();
        t.run();

        return (string1 == string2);
    }

    public boolean stringCompare_okay()
    {
        String literal1 = "foo";
        String literal2 = "world";
        System.out.print("abc");

        return (literal1 == literal2);
    }

    public boolean stringCompare_okayToo()
    {
        String interned1 = new Date().toString().intern();
        String interned2 = new Date().toString().intern();

        return (interned1 == interned2);
    }
    
    private String getInterned()
    {
        return new Date().toString().intern();
    }
    
    public boolean stringCompare_shouldBeOkay()
    {
        String interned1 = getInterned();
        String interned2 = getInterned();

        return (interned1 == interned2);
    }


}
