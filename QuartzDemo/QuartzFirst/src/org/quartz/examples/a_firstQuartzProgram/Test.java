package org.quartz.examples.a_firstQuartzProgram;

import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class Test {
    public Test() {
        super();
    }

    public static void main(String[] args) {
        Test test = new Test();
        // 通过SchedulerFactory获取一个调度器实例  
        SchedulerFactory sf = new StdSchedulerFactory(); 
    }
}
