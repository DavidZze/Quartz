package hand.hmw.framework.quartz.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;




/**
 * Logic to interact with the Quartz library.
 * Implemented using the singleton pattern. Logic is accessed from both model
 * and view layers
 */
public class QuartzSchedulerSingleton implements Serializable{
    // Singleton static instance for the class
    private static QuartzSchedulerSingleton _instance = null;
    /*
     * Static instance of the scheduler factory initialized based on configuration
     * file uploaded to JCS. Instance is shared for all code in the sample.
     */
    private static StdSchedulerFactory _schedulerFactory = null;
    
    // Static instance for the shared scheduler. Created and started when singleton is created.
    private static Scheduler _scheduler = null;
  private static final long serialVersionUID = 1L;


  /**
   * Default constructor
   * Defined as protected to prevent instantiation
   */
    protected QuartzSchedulerSingleton() {
        System.out.println("QuartzSchedulerSingleton.constructor Start ");
        try {
           
            _schedulerFactory = new StdSchedulerFactory();
            _schedulerFactory.initialize("/hand/hmw/framework/quartz/resources/quartz.properties");

            _scheduler = _schedulerFactory.getScheduler();
            // Scheduler needs to be started for it to execute jobs 
            _scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("QuartzSchedulerSingleton.constructor End ");
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * Get the instance of the singleton
     * @return instance of the singleton
     */
    public static QuartzSchedulerSingleton getInstance() {
        if (_instance == null) {
            System.out.println("QuartzSchedulerSingleton.getInstance create new ");
            _instance = new QuartzSchedulerSingleton();
        }
        return _instance;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Get the scheduler initialized based on the properties uploaded to JCS.
     * @return initialized scheduler
     */
    public Scheduler getScheduler() {
        return _scheduler;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Gets a list of scheduled jobs
     * @return list of scheduler jobs
     */
    public List<ScheduledJob> getScheduledJobs() {
        System.out.println("QuartzSchedulerSingleton.getScheduledJobs Start ");
        List<ScheduledJob> result = new ArrayList<ScheduledJob>();
        try {            
            // Loop through the groups and all jobs for each group
            for (String groupName : _scheduler.getJobGroupNames()) {
                for (JobKey jobKey : _scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();

                    // Get the trigger for the job to determine next execution
                    List<Trigger> triggers =
                        (List<Trigger>)_scheduler.getTriggersOfJob(jobKey);
                    Date nextFireTime = triggers.get(0).getNextFireTime();
                    
                    // Get the JobDetail to obtain the name of the class for the job
                    JobDetail jobDetail = _scheduler.getJobDetail(jobKey);
                    
                    // Add a POJO object to the list containing the details of the scheduled jobs
                    ScheduledJob temp =
                        new ScheduledJob(jobName, jobGroup, nextFireTime,
                                         jobDetail.getJobClass().getName());
                    System.out.println("QuartzSchedulerSingleton.getScheduledJobs job : "+temp);
                    result.add(temp);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("QuartzSchedulerSingleton.getScheduledJobs result "+result);
        return result;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Delete scheduled job
     * @param jobName name of the job to be deleted
     * @param groupName group name of the job to be deleted
     */
    public void deleteScheduledJob(String jobName, String groupName) {
        System.out.println("QuartzSchedulerSingleton.deleteScheduledJob Start; "+jobName + " " + groupName);
        try {
            // Construct the key for the job to be deleted
            JobKey jobKey = new JobKey(jobName, groupName);
            
            // Delete the job
            _scheduler.deleteJob(jobKey);  
        } catch (Exception e) {
            throw new RuntimeException(e);
        }         
        System.out.println("QuartzSchedulerSingleton.deleteScheduledJob End ");
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Schedule a job
     * @param jobName name of the job to be scheduled
     * @param jobClassName class of the job to be schedule
     * @param cronSchedule the cron based schedule for the job
     */
    public void scheduleJob(String jobName, String jobClassName, String cronSchedule) {
        System.out.println("QuartzSchedulerSingleton.scheduleJob Start; "+jobName + " " + jobClassName + " " + cronSchedule);
        try {
            Class jobClass = Class.forName(jobClassName);

            // Create the job
            JobDetail job =
                JobBuilder.newJob(jobClass).withIdentity(jobName).build();

            // Create a new cron based schedule 
            Trigger trigger =
                TriggerBuilder.newTrigger().withIdentity(jobName).withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule)).build();

            _scheduler.scheduleJob(job,trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("QuartzSchedulerSingleton.scheduleJob End ");
    }
    
    
    
    
    /**
     * Schedule a job
     * @param jobName name of the job to be scheduled
     * @param jobClassName class of the job to be schedule
     * @param cronSchedule the cron based schedule for the job
     */
    public void scheduleJobMap(String jobName, String jobClassName, String cronSchedule, HashMap<String,String> map) {
        System.out.println("QuartzSchedulerSingleton.scheduleJob Start; "+jobName + " " + jobClassName + " " + cronSchedule);
        try {
            Class jobClass = Class.forName(jobClassName);

            // Create the job
            JobDetail job =
                JobBuilder.newJob(jobClass).withIdentity(jobName).build();
            
            
            Set<String> keySet = map.keySet();
            
            for(String key : keySet) {
                
                job.getJobDataMap().put(key, map.get(key));
                    
            }

            // Create a new cron based schedule 
            Trigger trigger =
                TriggerBuilder.newTrigger().withIdentity(jobName).withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule)).build();

            _scheduler.scheduleJob(job,trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("QuartzSchedulerSingleton.scheduleJob End ");
    }
    
    
    /**
     * Schedule a job
     * @param jobName name of the job to be scheduled
     * @param jobClassName class of the job to be schedule
     * @param cronSchedule the cron based schedule for the job
     */
    public void scheduleJobWithJobDataMap(String jobName, String jobClassName, String cronSchedule, JobDataMap jobDataMap) {
        System.out.println("QuartzSchedulerSingleton.scheduleJob Start; "+jobName + " " + jobClassName + " " + cronSchedule);
        try {
            Class jobClass = Class.forName(jobClassName);

            // Create the job
            JobDetail job =
                JobBuilder.newJob(jobClass).withIdentity(jobName).build();
            
                
                //job.getJobDataMap() = jobDataMap;    
                this.backDataMap(jobDataMap, job.getJobDataMap());
            
           

            // Create a new cron based schedule 
            Trigger trigger =
                TriggerBuilder.newTrigger().withIdentity(jobName).withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule)).build();

            _scheduler.scheduleJob(job,trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("QuartzSchedulerSingleton.scheduleJob End ");
    }
    
    
    
    
    /**
     * by David.ze 
     * @param jobDataMap   数据源
     * @param backJobDataMap 目标Map
     * @return
     */
    private JobDataMap backDataMap (JobDataMap jobDataMap , JobDataMap backJobDataMap) {
        
        
        Set<String> keySet = jobDataMap.keySet();
        for(String key : keySet) {
            
            backJobDataMap.put(key, jobDataMap.get(key));    
        }
        
        
        return backJobDataMap;    
    }
    
    
    
}


