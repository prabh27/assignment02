package org.soal.findbugs.tutorial;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;

/**
 * Created by prabh on 22/07/16.
 */
public class DemoDetector8 extends BytecodeScanningDetector {

    private static final ClassDescriptor runnable_class = DescriptorFactory.createClassDescriptor(Runnable.class);
    private BugReporter reporter;
    public DemoDetector8(BugReporter reporter){

        this.reporter = reporter;
    }

    public void sawMethod() {
        try {
            MethodDescriptor methodDescriptor = getMethodDescriptorOperand();
            ClassDescriptor classDescriptor = getClassDescriptorOperand();
            ClassDescriptor[] interfaceList = classDescriptor.getXClass().getInterfaceDescriptorList();
            Class c = Class.forName(classDescriptor.getDottedClassName());
            for (ClassDescriptor descriptor : interfaceList) {
                if ("run".equals(methodDescriptor.getName())) {
                    if (descriptor.equals(runnable_class) && !Thread.class.isAssignableFrom(c)) {
                        reporter.reportBug(
                                new BugInstance("DEMO_BUG_8", Priorities.HIGH_PRIORITY)
                                        .addClass(this)
                                        .addMethod(this)
                                        .addSourceLine(this));
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }


        }
    }

