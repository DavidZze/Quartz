package org.quartz.examples.i_JobListeners;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob1
  implements Job
{
  private static Logger _log = LoggerFactory.getLogger(SimpleJob1.class);

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    JobKey jobKey = context.getJobDetail().getKey();
    _log.info("SimpleJob1 says: " + jobKey + " executing at " + new Date());
        System.out.println("SimpleJob1 says: " + jobKey + " executing at " + new Date());
  }
}