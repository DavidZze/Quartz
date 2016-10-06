package hand.hmw.framework.quartz.jobs;

import hand.hmw.framework.quartz.utils.DBConnUtils;

import java.io.Serializable;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

public class DBJob implements Job , Serializable{
  private static final long serialVersionUID = 1L;

  public DBJob() {
        super();
    }

    public void execute(JobExecutionContext context) {
        
        
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        
        
        String connType = jobDataMap.getString("connType");
        
        
        if("jdbcURL".equals(connType)) {
            
            String hostName = jobDataMap.getString("hostName");  
            String port = jobDataMap.getString("port"); 
            String sid = jobDataMap.getString("sid"); 
            String username = jobDataMap.getString("username"); 
            String pwd = jobDataMap.getString("pwd"); 
            String script = jobDataMap.getString("script"); 
            
            DBConnUtils dbUtils = new DBConnUtils();
            
            try {
               Connection conn = dbUtils.getJDBCConn(hostName, port, sid, username, pwd);
               CallableStatement stmt = conn.prepareCall(script);  
               stmt.execute(); 
               conn.close();
                
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("DBJob 数据库存储过程调用失败，请检查jdbc信息!");
            } 
            
        }else if("jdbcDS".equals(connType)) {
            
            String jndiName = jobDataMap.getString("jndiName"); 
            String plsql2 = jobDataMap.getString("plsql2"); 
            
            DBConnUtils dbUtils = new DBConnUtils();
            
            try {
                
                Connection conn = dbUtils.getJNDIConnectionByContainer(jndiName);
                
                CallableStatement stmt = conn.prepareCall(plsql2);
                stmt.execute();
                conn.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("DBJob 数据库存储过程调用失败，请检查jdbc DS信息!");
            }
            
        }
            
        
    }
}
