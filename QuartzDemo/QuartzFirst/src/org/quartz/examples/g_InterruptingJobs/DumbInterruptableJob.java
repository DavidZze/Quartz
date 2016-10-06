package org.quartz.examples.g_InterruptingJobs;

import java.util.Date;
import org.quartz.InterruptableJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DumbInterruptableJob
  implements InterruptableJob
{
  private static Logger _log = LoggerFactory.getLogger(DumbInterruptableJob.class);

  private boolean _interrupted = false;

  private JobKey _jobKey = null;

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    this._jobKey = context.getJobDetail().getKey();
    _log.info("---- " + this._jobKey + " executing at " + new Date());
    try
    {
      for (int i = 0; i < 4; ) {
        try {
          Thread.sleep(1000L);
        } catch (Exception ignore) {
          ignore.printStackTrace();
        }

        if (this._interrupted) {
          _log.info("--- " + this._jobKey + "  -- Interrupted... bailing out!");
          return;
        }
        i++;
      }
    }
    finally
    {
      int i;
      _log.info("---- " + this._jobKey + " completed at " + new Date());
    }
  }

  public void interrupt()
    throws UnableToInterruptJobException
  {
    _log.info("---" + this._jobKey + "  -- INTERRUPTING --");
    this._interrupted = true;
  }
}