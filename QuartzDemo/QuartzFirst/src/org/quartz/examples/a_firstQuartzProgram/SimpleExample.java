package org.quartz.examples.a_firstQuartzProgram;

import java.util.Date;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleExample
{
  public void run()  throws Exception
  {
    Logger log = LoggerFactory.getLogger(SimpleExample.class);

    log.info("------- Initializing ----------------------");
    System.out.println("------- Initializing -----------------");

    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete -----------");
    System.out.println("------- Initialization Complete -----------------");

    Date runTime = DateBuilder.evenMinuteDate(new Date());

    log.info("------- Scheduling Job  -------------------");
    System.out.println("------- Scheduling Job  -------------------");


    JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();

    sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + runTime);
    System.out.println(job.getKey() + " will run at: " + runTime);

    sched.start();

    log.info("------- Started Scheduler -----------------");
    System.out.println("------- Started Scheduler -----------------");

    log.info("------- Waiting 65 seconds... -------------");
    System.out.println("------- Waiting 65 seconds... -------------");
    
    try
    {
      Thread.sleep(5000L);
    }
    catch (Exception e)
    {
    }

    log.info("------- Shutting Down ---------------------");
    sched.shutdown(true);
    log.info("------- Shutdown Complete -----------------");
    System.out.println("------- Shutdown Complete -----------------");
  }

  public static void main(String[] args) throws Exception
  {
    SimpleExample example = new SimpleExample();
    example.run();
  }
}