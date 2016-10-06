package hand.hmw.framework.quartz.core;

import java.io.Serializable;

import java.util.Date;


/**
 * This class represents a scheduled object in Quartz framework.
 * The object is used by the ScheduledJobsVO to encapsulate details of a
 * scheduled job during processing.
 */
public class ScheduledJob implements Serializable{
    
    /*
     * Variables to store the information for a scheduled job.
     */
  private static final long serialVersionUID = 1L;
  private String jobName = null;
    private String groupName = null;
    private Date nextExecution = null;
    private String jobClass = null;

    /**
     * Default constructor
     * @param jobName Name of the scheduled job
     * @param groupName Group to which the scheduled job belongs to
     * @param nextExecution The time of next execution for the job
     * @param jobClass Name of the class executed for the job
     */
    public ScheduledJob(String jobName, String groupName,
                        Date nextExecution, String jobClass) {
        super();
        this.jobName = jobName;
        this.groupName = groupName;
        this.nextExecution = nextExecution;
        this.jobClass = jobClass;
    }

    /**
     * Setter for a variable.
     * @param jobName value for the variable
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * Getter for a variable.
     * @return value of the variable
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Setter for a variable.
     * @param groupName value for the variable
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Getter for a variable.
     * @return value of the variable
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Setter for a variable.
     * @param nextExecution value for the variable
     */
    public void setNextExecution(Date nextExecution) {
        this.nextExecution = nextExecution;
    }

    /**
     * Getter for a variable.
     * @return value of the variable
     */
    public Date getNextExecution() {
        return nextExecution;
    }
    
    /**
     * Setter for a variable.
     * @param jobClass value for the variable
     */
    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    /**
     * Getter for a variable.
     * @return value of the variable
     */
    public String getJobClass() {
        return jobClass;
    }
    
    @Override
    public String toString() {
        String nextExecutionFormatted = (null == nextExecution ? "null" : String.format("%tc", nextExecution));
        return jobName  + " " + groupName + " " + nextExecutionFormatted + " " + jobClass;
    }    
}

