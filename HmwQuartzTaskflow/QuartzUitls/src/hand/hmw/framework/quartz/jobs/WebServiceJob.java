package hand.hmw.framework.quartz.jobs;

import hand.hmw.framework.quartz.utils.WSClientUtils;

import java.io.Serializable;

import java.util.Set;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;

public class WebServiceJob implements Job , Serializable{
  private static final long serialVersionUID = 1L;

  public WebServiceJob() {
        super();
    }

    public void execute(JobExecutionContext context) {
        
        
        Scheduler scheduler = context.getScheduler();
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        JobKey jobKey = jobDetail.getKey();
        String cronExpr = jobDataMap.getString("cronExpr");
        
        String uri = jobDataMap.getString("uri");
        String requestMessage = jobDataMap.getString("requestMessage");
        
        WSClientUtils u = new WSClientUtils();
        
        try{
            String res = u.callWebService(requestMessage,uri) ;

//                System.out.println("--------执行成功-----------");
//                    System.out.println(res);
        }catch(Exception e) {            
            
            e.printStackTrace();
            throw new IllegalArgumentException("WebService 服务调用失败，请检查报文与uri endpoint信息!");  
            

        }
        
        
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
