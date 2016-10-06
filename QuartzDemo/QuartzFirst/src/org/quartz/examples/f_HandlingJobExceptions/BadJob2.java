package org.quartz.examples.f_HandlingJobExceptions;

import java.util.Date;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BadJob2
  implements Job
{
  private static Logger _log = LoggerFactory.getLogger(BadJob2.class);
  private int calculation;

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    JobKey jobKey = context.getJobDetail().getKey();
    _log.info("---" + jobKey + " executing at " + new Date());
    try
    {
      int zero = 0;
      this.calculation = (4815 / zero);
    } catch (Exception e) {
      _log.info("--- Error in job!");
      JobExecutionException e2 = new JobExecutionException(e);

      e2.setUnscheduleAllTriggers(true);
      throw e2;
    }

    _log.info("---" + jobKey + " completed at " + new Date());
  }
}