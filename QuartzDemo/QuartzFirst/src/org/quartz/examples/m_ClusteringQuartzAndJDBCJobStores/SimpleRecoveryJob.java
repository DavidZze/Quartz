package org.quartz.examples.m_ClusteringQuartzAndJDBCJobStores;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRecoveryJob
  implements Job
{
  private static Logger _log = LoggerFactory.getLogger(SimpleRecoveryJob.class);
  private static final String COUNT = "count";

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    JobKey jobKey = context.getJobDetail().getKey();

    if (context.isRecovering())
      _log.info("SimpleRecoveryJob: " + jobKey + " RECOVERING at " + new Date());
    else {
      _log.info("SimpleRecoveryJob: " + jobKey + " starting at " + new Date());
    }

    long delay = 10000L;
    try {
      Thread.sleep(delay);
    }
    catch (Exception e)
    {
    }
    JobDataMap data = context.getJobDetail().getJobDataMap();
    int count;
    if (data.containsKey("count"))
      count = data.getInt("count");
    else {
      count = 0;
    }
    count++;
    data.put("count", count);

    _log.info("SimpleRecoveryJob: " + jobKey + " done at " + new Date() + "\n Execution #" + count);
  }
}