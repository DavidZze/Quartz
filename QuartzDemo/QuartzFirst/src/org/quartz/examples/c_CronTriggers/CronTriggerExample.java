package org.quartz.examples.c_CronTriggers;

import java.util.Date;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CronTriggerExample
{
  public void run()
    throws Exception
  {
    Logger log = LoggerFactory.getLogger(CronTriggerExample.class);

    log.info("------- Initializing -------------------");

    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete --------");

    log.info("------- Scheduling Jobs ----------------");

    JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();

    CronTrigger trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();

    Date ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: " + trigger.getCronExpression());

    job = JobBuilder.newJob(SimpleJob.class).withIdentity("job2", "group1").build();

    trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity("trigger2", "group1").withSchedule(CronScheduleBuilder.cronSchedule("15 0/2 * * * ?")).build();

    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: " + trigger.getCronExpression());

    job = JobBuilder.newJob(SimpleJob.class).withIdentity("job3", "group1").build();

    trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity("trigger3", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?")).build();

    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: " + trigger.getCronExpression());

    job = JobBuilder.newJob(SimpleJob.class).withIdentity("job4", "group1").build();

    trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity("trigger4", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0 0/3 17-23 * * ?")).build();

    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: " + trigger.getCronExpression());

    job = JobBuilder.newJob(SimpleJob.class).withIdentity("job5", "group1").build();

    trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity("trigger5", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0 0 10am 1,15 * ?")).build();

    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: " + trigger.getCronExpression());

    job = JobBuilder.newJob(SimpleJob.class).withIdentity("job6", "group1").build();

    trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity("trigger6", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * MON-FRI")).build();

    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: " + trigger.getCronExpression());

    job = JobBuilder.newJob(SimpleJob.class).withIdentity("job7", "group1").build();

    trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity("trigger7", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * SAT,SUN")).build();

    ft = sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: " + trigger.getCronExpression());

    log.info("------- Starting Scheduler ----------------");

    sched.start();

    log.info("------- Started Scheduler -----------------");

    log.info("------- Waiting five minutes... ------------");
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

  public static void main(String[] args)
    throws Exception
  {
    CronTriggerExample example = new CronTriggerExample();
    example.run();
  }
}