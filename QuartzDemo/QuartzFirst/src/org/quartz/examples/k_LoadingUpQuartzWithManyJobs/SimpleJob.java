package org.quartz.examples.k_LoadingUpQuartzWithManyJobs;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob
  implements Job
{
  private static Logger _log = LoggerFactory.getLogger(SimpleJob.class);
  public static final String DELAY_TIME = "delay time";

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    JobKey jobKey = context.getJobDetail().getKey();
    _log.info("Executing job: " + jobKey + " executing at " + new Date());

    long delayTime = context.getJobDetail().getJobDataMap().getLong("delay time");
    try {
      Thread.sleep(delayTime);
    }
    catch (Exception e)
    {
    }
    _log.info("Finished Executing job: " + jobKey + " at " + new Date());
  }
}