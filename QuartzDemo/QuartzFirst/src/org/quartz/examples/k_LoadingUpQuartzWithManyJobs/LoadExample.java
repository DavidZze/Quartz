package org.quartz.examples.k_LoadingUpQuartzWithManyJobs;

import java.io.PrintStream;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadExample
{
  private int _numberOfJobs = 500;

  public LoadExample(int inNumberOfJobs) {
    this._numberOfJobs = inNumberOfJobs;
  }

  public void run() throws Exception {
    Logger log = LoggerFactory.getLogger(LoadExample.class);

    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete -----------");

    for (int count = 1; count <= this._numberOfJobs; count++) {
      JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job" + count, "group_1").requestRecovery().build();

      long timeDelay = (long)(Math.random() * 2500.0D);
      job.getJobDataMap().put("delay time", timeDelay);

      Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + count, "group_1").startAt(DateBuilder.futureDate(10000 + count * 100, DateBuilder.IntervalUnit.MILLISECOND)).build();

      sched.scheduleJob(job, trigger);
      if (count % 25 == 0) {
        log.info("...scheduled " + count + " jobs");
      }
    }

    log.info("------- Starting Scheduler ----------------");

    sched.start();

    log.info("------- Started Scheduler -----------------");

    log.info("------- Waiting five minutes... -----------");
    try
    {
      Thread.sleep(300000L);
    }
    catch (Exception e)
    {
    }

    log.info("------- Shutting Down ---------------------");
    sched.shutdown(true);
    log.info("------- Shutdown Complete -----------------");

    SchedulerMetaData metaData = sched.getMetaData();
    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
  }

  public static void main(String[] args) throws Exception
  {
    int numberOfJobs = 500;
    if (args.length == 1) {
      numberOfJobs = Integer.parseInt(args[0]);
    }
    if (args.length > 1) {
      System.out.println("Usage: java " + LoadExample.class.getName() + "[# of jobs]");
      return;
    }
    LoadExample example = new LoadExample(numberOfJobs);
    example.run();
  }
}