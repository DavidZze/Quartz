package org.quartz.zzTestDemo.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class ExecuteProcedureJob implements Job{
    public ExecuteProcedureJob() {
        super();
    }

    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println("ExecuteProcedureJob");
        
        
        
        
    }
}
