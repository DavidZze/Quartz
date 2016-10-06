package org.quartz.examples.l_RMI;

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
  public static final String MESSAGE = "msg";
  private static Logger _log = LoggerFactory.getLogger(SimpleJob.class);

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    JobKey jobKey = context.getJobDetail().getKey();

    String message = (String)context.getJobDetail().getJobDataMap().get("msg");

    _log.info("SimpleJob: " + jobKey + " executing at " + new Date());
    _log.info("SimpleJob: msg: " + message);
  }
}