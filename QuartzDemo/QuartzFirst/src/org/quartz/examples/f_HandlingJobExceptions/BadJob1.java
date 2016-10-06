package org.quartz.examples.f_HandlingJobExceptions;

import java.util.Date;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BadJob1
  implements Job
{
  private static Logger _log = LoggerFactory.getLogger(BadJob1.class);
  private int calculation;

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    JobKey jobKey = context.getJobDetail().getKey();
    JobDataMap dataMap = context.getJobDetail().getJobDataMap();

    int denominator = dataMap.getInt("denominator");
    _log.info("---" + jobKey + " executing at " + new Date() + " with denominator " + denominator);
    try
    {
      this.calculation = (4815 / denominator);
    } catch (Exception e) {
      _log.info("--- Error in job!");
      JobExecutionException e2 = new JobExecutionException(e);

      dataMap.put("denominator", "1");

      e2.setRefireImmediately(true);
      throw e2;
    }

    _log.info("---" + jobKey + " completed at " + new Date());
  }
}