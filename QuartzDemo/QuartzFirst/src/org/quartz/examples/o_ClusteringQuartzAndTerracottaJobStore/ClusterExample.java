package org.quartz.examples.o_ClusteringQuartzAndTerracottaJobStore;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterExample
{
  private static Logger _log = LoggerFactory.getLogger(ClusterExample.class);

  public void run(boolean inClearJobs, boolean inScheduleJobs)
    throws Exception
  {
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    if (inClearJobs) {
      _log.warn("***** Deleting existing jobs/triggers *****");
      sched.clear();
    }

    _log.info("------- Initialization Complete -----------");

    if (inScheduleJobs)
    {
      _log.info("------- Scheduling Jobs ------------------");

      String schedId = sched.getSchedulerInstanceId();

      int count = 1;

      JobDetail job = JobBuilder.newJob(SimpleRecoveryJob.class).withIdentity("job_" + count, schedId).requestRecovery().build();

      SimpleTrigger trigger = (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("triger_" + count, schedId).startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND)).withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInSeconds(5)).build();

      _log.info(job.getKey() + " will run at: " + trigger.getNextFireTime() + " and repeat: " + trigger.getRepeatCount() + " times, every " + trigger.getRepeatInterval() / 1000L + " seconds");

      sched.scheduleJob(job, trigger);

      count++;

      job = JobBuilder.newJob(SimpleRecoveryJob.class).withIdentity("job_" + count, schedId).requestRecovery().build();

      trigger = (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("triger_" + count, schedId).startAt(DateBuilder.futureDate(2, DateBuilder.IntervalUnit.SECOND)).withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInSeconds(5)).build();

      _log.info(job.getKey() + " will run at: " + trigger.getNextFireTime() + " and repeat: " + trigger.getRepeatCount() + " times, every " + trigger.getRepeatInterval() / 1000L + " seconds");

      sched.scheduleJob(job, trigger);

      count++;

      job = JobBuilder.newJob(SimpleRecoveryStatefulJob.class).withIdentity("job_" + count, schedId).requestRecovery().build();

      trigger = (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("triger_" + count, schedId).startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND)).withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInSeconds(3)).build();

      _log.info(job.getKey() + " will run at: " + trigger.getNextFireTime() + " and repeat: " + trigger.getRepeatCount() + " times, every " + trigger.getRepeatInterval() / 1000L + " seconds");

      sched.scheduleJob(job, trigger);

      count++;

      job = JobBuilder.newJob(SimpleRecoveryJob.class).withIdentity("job_" + count, schedId).requestRecovery().build();

      trigger = (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("triger_" + count, schedId).startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND)).withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInSeconds(4)).build();

      _log.info(job.getKey() + " will run at: " + trigger.getNextFireTime() + " & repeat: " + trigger.getRepeatCount() + "/" + trigger.getRepeatInterval());

      sched.scheduleJob(job, trigger);

      count++;

      job = JobBuilder.newJob(SimpleRecoveryJob.class).withIdentity("job_" + count, schedId).requestRecovery().build();

      trigger = (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("triger_" + count, schedId).startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND)).withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(20).withIntervalInMilliseconds(4500L)).build();

      _log.info(job.getKey() + " will run at: " + trigger.getNextFireTime() + " & repeat: " + trigger.getRepeatCount() + "/" + trigger.getRepeatInterval());

      sched.scheduleJob(job, trigger);
    }

    _log.info("------- Starting Scheduler ---------------");
    sched.start();
    _log.info("------- Started Scheduler ----------------");

    _log.info("------- Waiting for one hour... ----------");
    try {
      Thread.sleep(3600000L);
    }
    catch (Exception e)
    {
    }
    _log.info("------- Shutting Down --------------------");
    sched.shutdown();
    _log.info("------- Shutdown Complete ----------------");
  }

  public static void main(String[] args) throws Exception {
    boolean clearJobs = false;
    boolean scheduleJobs = true;

    for (String arg : args) {
      if (arg.equalsIgnoreCase("clearJobs"))
        clearJobs = true;
      else if (arg.equalsIgnoreCase("dontScheduleJobs")) {
        scheduleJobs = false;
      }
    }

    ClusterExample example = new ClusterExample();
    example.run(clearJobs, scheduleJobs);
  }
}