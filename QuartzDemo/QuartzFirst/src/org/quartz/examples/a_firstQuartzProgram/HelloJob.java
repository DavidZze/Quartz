package org.quartz.examples.a_firstQuartzProgram;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job
{
  private static Logger _log = LoggerFactory.getLogger(HelloJob.class);

  public void execute(JobExecutionContext context)
    throws JobExecutionException
  {
    _log.info("Hello World! - " + new Date());
     // System.out.println("Hello World! - " + new Date());
    System.out.println("Hello World! - " + new Date());
        
  }
  
}