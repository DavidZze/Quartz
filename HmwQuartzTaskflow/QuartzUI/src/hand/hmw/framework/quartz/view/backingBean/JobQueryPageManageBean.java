package hand.hmw.framework.quartz.view.backingBean;

import hand.hmw.framework.quartz.core.QuartzSchedulerSingleton;


import hand.hmw.framework.view.common.CustomManagedBean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.ViewObject;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

public class JobQueryPageManageBean extends CustomManagedBean implements Serializable{
   

    private static final long serialVersionUID = 1L;
    private RichTable schedulerJobTable;
    
    private String newCronExpr = null;


    public JobQueryPageManageBean() {
        super();
    }


    
    //获取table 迭代器绑定：
    private DCIteratorBinding getSqlSchedulerJobIterator() {
        
        return this.findIterator("SchedulejobsBySqlVO1Iterator");  
                                  
    }


    //刷新页面的表
    private void refereshTable () {
        
        this.findOperation("Execute").execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.schedulerJobTable);
        
    }

    //消息提示：
    private void fireFacesMessage(String flag,String messgae) {
        
        FacesContext fctx = FacesContext.getCurrentInstance();
        
        if("error".equals(flag)) {
            fctx.addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,null,messgae)); 
        }else if ("info".equals(flag)) {
            fctx.addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_INFO,null,messgae));     
        }
        
    }
    


    //获取 Quartz 中当前运行的Scheduler (单)实例：
    private QuartzSchedulerSingleton getStdSchedulerInBean() {
        
        return QuartzSchedulerSingleton.getInstance();
         
    }

    //获取被选中当前行 JobName
    private String getCurrentJobName() {
        
        return  (String)this.getSqlSchedulerJobIterator().getCurrentRow().getAttribute("JobName");   
    }
    
    //获取被选中当前行 JobGroup
    private String getCurrentJobGroup() {
        
        return  (String)this.getSqlSchedulerJobIterator().getCurrentRow().getAttribute("JobGroup");   
    }



    //删除事件 监听：
    public void deleteDialogLsnr(DialogEvent dialogEvent) {
        // Add event code here...
        
        if(DialogEvent.Outcome.ok.equals(dialogEvent.getOutcome())) {
            
            
            String jobName = this.getCurrentJobName();
            String jobGroup = this.getCurrentJobGroup();
            
            this.getStdSchedulerInBean().deleteScheduledJob(jobName, jobGroup);
            System.out.println("removeCurrentRow "+ jobName + "" + jobGroup);
            
           this.refereshTable();

        }
        
       
        
        
    }

    
    //暂停job
    public void pauseJob(ActionEvent actionEvent){
        // Add event code here...
        JobKey jobKey = JobKey.jobKey(this.getCurrentJobName(), this.getCurrentJobGroup());

        try {
            this.getStdSchedulerInBean().getScheduler().pauseJob(jobKey);
            this.fireFacesMessage("info", this.getCurrentJobName() + " 作业暂停成功");
        } catch (SchedulerException e) {
            this.fireFacesMessage("error", this.getCurrentJobName() + " 作业暂停失败");
        }finally {
            this.refereshTable(); 
        }
        
    }

    
    //恢复job
    public void resumeJob(ActionEvent actionEvent) {
        // Add event code here...
        JobKey jobKey = JobKey.jobKey(this.getCurrentJobName(), this.getCurrentJobGroup());
        try {
            this.getStdSchedulerInBean().getScheduler().resumeJob(jobKey);
            this.fireFacesMessage("info", this.getCurrentJobName() + " 作业恢复成功");
        } catch (SchedulerException e) {
            this.fireFacesMessage("error", this.getCurrentJobName() + " 作业恢复失败");
        }finally {
            this.refereshTable(); 
        }
       
    }
    
    
    //立即运行一次
    public void triggerJob(ActionEvent actionEvent) {
        // Add event code here...
        JobKey jobKey = JobKey.jobKey(this.getCurrentJobName(), this.getCurrentJobGroup());
        try {
            this.getStdSchedulerInBean().getScheduler().triggerJob(jobKey);
          //this.refereshTable();
            this.fireFacesMessage("info", this.getCurrentJobName() + " 立即执行成功");
        } catch (Exception e) {
          //this.refereshTable();
            this.fireFacesMessage("error", this.getCurrentJobName() + " 立即执行失败");
        }finally {
          
             
               
             
//          ViewObject vo = this.getSqlSchedulerJobIterator().getViewObject();
//          int qMode = vo.getQueryMode();
//          vo.setQueryMode(vo.QUERY_MODE_SCAN_DATABASE_TABLES);
//          vo.executeQuery();
//          vo.setQueryMode(qMode);
        }
        
        
    }
    
    
    //更新指定行的定时器：
    public void rescheduleJobDialogLsnr(DialogEvent dialogEvent) {
        // Add event code here...
        
        
        System.out.println("---------------------" + this.getNewCronExpr());
        
        if(DialogEvent.Outcome.ok.equals(dialogEvent.getOutcome())) {
            
            
          if(this.getNewCronExpr() !=null ) {
            
            TriggerKey triggerKey = TriggerKey.triggerKey(this.getCurrentJobName(), this.getCurrentJobGroup());
            CronTrigger trigger;
            // 表达式调度构建器
            try {
                trigger = (CronTrigger) this.getStdSchedulerInBean().getScheduler().getTrigger(triggerKey);
                
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(this.getNewCronExpr());
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                
                try {
                    // 按新的trigger重新设置job执行
                    this.getStdSchedulerInBean().getScheduler().rescheduleJob(triggerKey, trigger);
                    this.newCronExpr = null;
                    this.fireFacesMessage("info", this.getCurrentJobName() + " 定时器更新成功");
                    
                } catch (SchedulerException e) {
                    this.fireFacesMessage("error", "更新定时器失败！");
                }
                
                
            } catch (SchedulerException e) {
                this.fireFacesMessage("error", "Cron表达式错误，请检查！");
            }finally {
                           
                 this.refereshTable();         
            }
            
          }else {
            
            this.addInfoMessage(null, "时间表达式不能更新为空，请输入一个时间表达式");  
          }  
           
        }
        
       
    }
    
    
    


    public void setSchedulerJobTable(RichTable schedulerJobTable) {
        this.schedulerJobTable = schedulerJobTable;
    }

    public RichTable getSchedulerJobTable() {
        return schedulerJobTable;
    }


    public void setNewCronExpr(String newCronExpr) {
        this.newCronExpr = newCronExpr;
    }

    public String getNewCronExpr() {
        return newCronExpr;
    }

    
}
