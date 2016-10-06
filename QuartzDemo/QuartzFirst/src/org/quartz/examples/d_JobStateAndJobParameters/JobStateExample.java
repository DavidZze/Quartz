package org.quartz.examples.d_JobStateAndJobParameters;

import java.util.Date;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobStateExample
{
  public void run()
    throws Exception
  {
    Logger log = LoggerFactory.getLogger(JobStateExample.class);

    log.info("------- Initializing -------------------");

    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete --------");

    log.info("------- Scheduling Jobs ----------------");

    Date startTime = DateBuilder.nextGivenSecondDate(null, 10);

    JobDetail job1 = JobBuilder.newJob(ColorJob.class).withIdentity("job1", "group1").build();

    SimpleTrigger trigger1 = (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(4)).build();

    job1.getJobDataMap().put("favorite color", "Green");
    job1.getJobDataMap().put("count", 1);

    Date scheduleTime1 = sched.scheduleJob(job1, trigger1);
    log.info(job1.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount() + " times, every " + trigger1.getRepeatInterval() / 1000L + " seconds");

    JobDetail job2 = JobBuilder.newJob(ColorJob.class).withIdentity("job2", "group1").build();

    SimpleTrigger trigger2 = (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger2", "group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(4)).build();

    job2.getJobDataMap().put("favorite color", "Red");
    job2.getJobDataMap().put("count", 1);

    Date scheduleTime2 = sched.scheduleJob(job2, trigger2);
    log.info(job2.getKey().toString() + " will run at: " + scheduleTime2 + " and repeat: " + trigger2.getRepeatCount() + " times, every " + trigger2.getRepeatInterval() / 1000L + " seconds");

    log.info("------- Starting Scheduler ----------------");

    sched.start();

    log.info("------- Started Scheduler -----------------");

    log.info("------- Waiting 60 seconds... -------------");
    try
    {
      Thread.sleep(60000L);
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

  public static void main(String[] args)
    throws Exception
  {
    JobStateExample example = new JobStateExample();
    example.run();
  }
}