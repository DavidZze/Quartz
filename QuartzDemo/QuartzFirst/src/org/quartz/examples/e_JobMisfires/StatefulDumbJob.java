package org.quartz.examples.e_JobMisfires;

import java.io.PrintStream;
import java.util.Date;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatefulDumbJob
  implements Job
{
  public static final String NUM_EXECUTIONS = "NumExecutions";
  public static final String EXECUTION_DELAY = "ExecutionDelay";

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    System.err.println("---" + context.getJobDetail().getKey() + " executing.[" + new Date() + "]");

    JobDataMap map = context.getJobDetail().getJobDataMap();

    int executeCount = 0;
    if (map.containsKey("NumExecutions")) {
      executeCount = map.getInt("NumExecutions");
    }

    executeCount++;

    map.put("NumExecutions", executeCount);

    long delay = 5000L;
    if (map.containsKey("ExecutionDelay")) {
      delay = map.getLong("ExecutionDelay");
    }
    try
    {
      Thread.sleep(delay);
    }
    catch (Exception ignore)
    {
    }
    System.err.println("  -" + context.getJobDetail().getKey() + " complete (" + executeCount + ").");
  }
}