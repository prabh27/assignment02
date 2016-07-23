package org.soal.findbugs.tutorial;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by prabh on 22/07/16.
 */
public class DemoDetector7 extends BytecodeScanningDetector{


    private static final ClassDescriptor thread_class = DescriptorFactory.createClassDescriptor(Thread.class);
    private BugReporter reporter;

    public DemoDetector7(BugReporter reporter){
        this.reporter = reporter;
    }

    @Override
    public void sawMethod() {
        MethodDescriptor methodDescriptor = getMethodDescriptorOperand();
        ClassDescriptor classDescriptor = getClassDescriptorOperand();
        if(methodDescriptor != null
                && "run".equals(methodDescriptor.getName())
                && thread_class.equals(classDescriptor)) {
            System.out.print("Method name " + methodDescriptor);
            reporter.reportBug(
                    new BugInstance("DEMO_BUG_7", Priorities.HIGH_PRIORITY)
                            .addClass(this)
                            .addMethod(this)
                            .addSourceLine(this)
            );
        }

    }
}
