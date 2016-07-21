import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Logger;


public class SimpleTransformer implements ClassFileTransformer {

    private static final Logger log = Logger.getLogger(SimpleMain.class.getName());

    public SimpleTransformer() {
        super();
    }

    public String createSysOutStatement(String x) {
        return "System.out.println(\"" + x + "\")";
    }

    public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
        if (className.contains("java") || className.contains("sun"))
            return bytes;
        try {
            ClassPool pool = ClassPool.getDefault();
            try {
                //log.info(className);
                CtClass cl = pool.makeClass(new java.io.ByteArrayInputStream(bytes));
                CtMethod[] methods = cl.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if(!method.isEmpty()) {
                        method.insertBefore("{System.out.println(\"Entering Function :" + method.getName() + "\");}");
                        method.insertBefore("{for (int i=0; i < $args.length; i++)" + " {System.out.print($args[i]+' ');}}");
                        method.insertAfter("{System.out.println($_);}");
                        method.insertAfter("{System.out.println(\"Exiting Function :" + method.getName() + "\");}");
                    }
                }
                bytes = cl.toBytecode();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            System.out.println("Completed for Class: " + className);

            return bytes;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}


