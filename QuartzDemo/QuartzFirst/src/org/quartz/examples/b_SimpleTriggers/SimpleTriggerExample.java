package org.quartz.examples.b_SimpleTriggers;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
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

public class SimpleTriggerExample {
    public void run() throws Exception {
        Logger log = LoggerFactory.getLogger(SimpleTriggerExample.class);

        log.info("------- Initializing -------------------");
            System.out.println("------- Initializing -------------------");

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        log.info("------- Initialization Complete --------");
            System.out.println("------- Initialization Complete --------");
        log.info("------- Scheduling Jobs ----------------");
            System.out.println("------- Scheduling Jobs ----------------");

        Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

        JobDetail job =
            JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();

        SimpleTrigger trigger =
            (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger1",
                                                                    "group1").startAt(startTime).build();

        Date ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " +
                 trigger.getRepeatCount() + " times, every " +
                 trigger.getRepeatInterval() / 1000L + " seconds");
        
            System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " +
                     trigger.getRepeatCount() + " times, every " +
                     trigger.getRepeatInterval() / 1000L + " seconds");
        
        

        job =
 JobBuilder.newJob(SimpleJob.class).withIdentity("job2", "group1").build();

        trigger =
                (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger2",
                                                                        "group1").startAt(startTime).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " +
                 trigger.getRepeatCount() + " times, every " +
                 trigger.getRepeatInterval() / 1000L + " seconds");
            System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " +
                 trigger.getRepeatCount() + " times, every " +
                 trigger.getRepeatInterval() / 1000L + " seconds");

        job =
 JobBuilder.newJob(SimpleJob.class).withIdentity("job3", "group1").build();

        trigger =
                (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger3",
                                                                        "group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10)).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " +
                 trigger.getRepeatCount() + " times, every " +
                 trigger.getRepeatInterval() / 1000L + " seconds");

            System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " +
                     trigger.getRepeatCount() + " times, every " +
                     trigger.getRepeatInterval() / 1000L + " seconds");


        trigger =
                (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger3",
                                                                        "group2").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(2)).forJob(job).build();

        ft = sched.scheduleJob(trigger);
        log.info(job.getKey() + " will [also] run at: " + ft +
                 " and repeat: " + trigger.getRepeatCount() +
                 " times, every " + trigger.getRepeatInterval() / 1000L +
                 " seconds");

        job =
 JobBuilder.newJob(SimpleJob.class).withIdentity("job4", "group1").build();

        trigger =
                (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger4",
                                                                        "group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(5)).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " +
                 trigger.getRepeatCount() + " times, every " +
                 trigger.getRepeatInterval() / 1000L + " seconds");

        job =
 JobBuilder.newJob(SimpleJob.class).withIdentity("job5", "group1").build();

        trigger =
                (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger5",
                                                                        "group1").startAt(DateBuilder.futureDate(5,
                                                                                                                 DateBuilder.IntervalUnit.MINUTE)).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " +
                 trigger.getRepeatCount() + " times, every " +
                 trigger.getRepeatInterval() / 1000L + " seconds");

        job =
 JobBuilder.newJob(SimpleJob.class).withIdentity("job6", "group1").build();

        trigger =
                (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger6",
                                                                        "group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " +
                 trigger.getRepeatCount() + " times, every " +
                 trigger.getRepeatInterval() / 1000L + " seconds");

        log.info("------- Starting Scheduler ----------------");

        sched.start();

        log.info("------- Started Scheduler -----------------");

        job =
 JobBuilder.newJob(SimpleJob.class).withIdentity("job7", "group1").build();

        trigger =
                (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger7",
                                                                        "group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

        ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " +
                 trigger.getRepeatCount() + " times, every " +
                 trigger.getRepeatInterval() / 1000L + " seconds");

        job =
 JobBuilder.newJob(SimpleJob.class).withIdentity("job8", "group1").storeDurably().build();

        sched.addJob(job, true);

        log.info("'Manually' triggering job8...");
        sched.triggerJob(JobKey.jobKey("job8", "group1"));

        log.info("------- Waiting 30 seconds... --------------");
        try {
            Thread.sleep(30000L);
        } catch (Exception e) {
        }

        log.info("------- Rescheduling... --------------------");
        trigger =
                (SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("trigger7",
                                                                        "group1").startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

        ft = sched.rescheduleJob(trigger.getKey(), trigger);
        log.info("job7 rescheduled to run at: " + ft);

        log.info("------- Waiting five minutes... ------------");
        try {
            Thread.sleep(300000L);
        } catch (Exception e) {
        }

        log.info("------- Shutting Down ---------------------");

        sched.shutdown(true);

        log.info("------- Shutdown Complete -----------------");

        SchedulerMetaData metaData = sched.getMetaData();
        log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }

    public static void main(String[] args) throws Exception {
        SimpleTriggerExample example = new SimpleTriggerExample();
        example.run();
    }
}
