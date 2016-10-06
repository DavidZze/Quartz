package org.quartz.examples.g_InterruptingJobs;

import java.util.Date;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterruptExample
{
  public void run()
    throws Exception
  {
    Logger log = LoggerFactory.getLogger(InterruptExample.class);

    log.info("------- Initializing ----------------------");

    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete -----------");

    log.info("------- Scheduling Jobs -------------------");

    Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

    JobDetail job = JobBuilder.newJob(DumbInterruptableJob.class).withIdentity("interruptableJob1", "group1").build();

    SimpleTrigger trigger = (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

    Date ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every " + trigger.getRepeatInterval() / 1000L + " seconds");

    sched.start();
    log.info("------- Started Scheduler -----------------");

    log.info("------- Starting loop to interrupt job every 7 seconds ----------");
    for (int i = 0; i < 50; i++) {
      try {
        Thread.sleep(7000L);

        sched.interrupt(job.getKey());
      }
      catch (Exception e)
      {
      }
    }
    log.info("------- Shutting Down ---------------------");

    sched.shutdown(true);

    log.info("------- Shutdown Complete -----------------");
    SchedulerMetaData metaData = sched.getMetaData();
    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
  }

  public static void main(String[] args)
    throws Exception
  {
    InterruptExample example = new InterruptExample();
    example.run();
  }
}