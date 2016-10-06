package org.quartz.examples.i_JobListeners;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.ListenerManager;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListenerExample
{
  public void run()
    throws Exception
  {
    Logger log = LoggerFactory.getLogger(ListenerExample.class);

    log.info("------- Initializing ----------------------");
        System.out.println("------- Initializing ----------------------");

    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete -----------");
        System.out.println("------- Initialization Complete -----------");

    log.info("------- Scheduling Jobs -------------------");
        System.out.println("------- Scheduling Jobs -------------------");

    JobDetail job = JobBuilder.newJob(SimpleJob1.class).withIdentity("job1").build();

    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1").startNow().build();

    JobListener listener = new Job1Listener();
    Matcher matcher = KeyMatcher.keyEquals(job.getKey());
    sched.getListenerManager().addJobListener(listener, matcher);

    sched.scheduleJob(job, trigger);

    log.info("------- Starting Scheduler ----------------");
        System.out.println("------- Starting Scheduler ----------------");
    sched.start();

    log.info("------- Waiting 30 seconds... --------------");
        System.out.println("------- Waiting 30 seconds... --------------");
    try
    {
      Thread.sleep(30000L);
    }
    catch (Exception e)
    {
    }

    log.info("------- Shutting Down ---------------------");
        System.out.println("------- Shutting Down ---------------------");
    sched.shutdown(true);
    log.info("------- Shutdown Complete -----------------");
        System.out.println("------- Shutdown Complete -----------------");

    SchedulerMetaData metaData = sched.getMetaData();
    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
        System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
  }

  public static void main(String[] args)
    throws Exception
  {
    ListenerExample example = new ListenerExample();
    example.run();
  }
}