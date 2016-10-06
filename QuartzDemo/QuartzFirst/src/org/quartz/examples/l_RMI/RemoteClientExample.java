package org.quartz.examples.l_RMI;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteClientExample
{
  public void run()
    throws Exception
  {
    Logger log = LoggerFactory.getLogger(RemoteClientExample.class);

    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("remotelyAddedJob", "default").build();

    JobDataMap map = job.getJobDataMap();
    map.put("msg", "Your remotely added job has executed!");

    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("remotelyAddedTrigger", "default").forJob(job.getKey()).withSchedule(CronScheduleBuilder.cronSchedule("/5 * * ? * *")).build();

    sched.scheduleJob(job, trigger);

    log.info("Remote job scheduled.");
  }

  public static void main(String[] args) throws Exception
  {
    RemoteClientExample example = new RemoteClientExample();
    example.run();
  }
}