package org.quartz.examples.d_JobStateAndJobParameters;

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
public class ColorJob
  implements Job
{
  private static Logger _log = LoggerFactory.getLogger(ColorJob.class);
  public static final String FAVORITE_COLOR = "favorite color";
  public static final String EXECUTION_COUNT = "count";
  private int _counter = 1;

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    JobKey jobKey = context.getJobDetail().getKey();

    JobDataMap data = context.getJobDetail().getJobDataMap();
    String favoriteColor = data.getString("favorite color");
    int count = data.getInt("count");
    _log.info("ColorJob: " + jobKey + " executing at " + new Date() + "\n" + "  favorite color is " + favoriteColor + "\n" + "  execution count (from job map) is " + count + "\n" + "  execution count (from job member variable) is " + this._counter);

    count++;
    data.put("count", count);

    this._counter += 1;
  }
}