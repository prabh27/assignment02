package org.soal.findbugs.tutorial;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;

/**
 * Created by prabh on 22/07/16.
 */
public class DemoDetector6 extends OpcodeStackDetector {
    private BugReporter reporter;

    public DemoDetector6(BugReporter reporter) {
        this.reporter = reporter;
    }

    public void sawOpcode(int seen) {
        if (seen == GETSTATIC) {
            FieldDescriptor operand = getFieldDescriptorOperand();
            ClassDescriptor classDescriptor = operand.getClassDescriptor();
            if ("java/lang/System".equals(classDescriptor.getClassName())
                    && ("err".equals(operand.getName())
                    || ("out".equals(operand.getName())))) {
                reporter.reportBug(
                        new BugInstance("DEMO_BUG_6", Priorities.HIGH_PRIORITY)
                                .addClass(this)
                                .addMethod(this)
                                .addSourceLine(this)
                );
            }

        }
    }
}
