package hand.hmw.framework.quartz.view.backingBean;

import hand.hmw.framework.quartz.core.QuartzSchedulerSingleton;
import hand.hmw.framework.quartz.utils.DBConnUtils;
import hand.hmw.framework.quartz.utils.WSClientUtils;
import hand.hmw.framework.view.common.CustomManagedBean;

import java.io.Serializable;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

import oracle.jbo.Row;

import org.quartz.SchedulerException;

public class NewSchedulerJobBean extends CustomManagedBean implements Serializable{
    
    
    
    
    
    private static final long serialVersionUID = 1003364247616582249L;
    
    
    
    private String jobTypeName = null;
    private String jobClassName = null;
    private String jobName = null;
    private String jobDesc = null;
    private String cronExpr = null;
    
    
    public String uri = null;
    public String requestMessage = null;
  private RichPanelGroupLayout panelGL;


  public NewSchedulerJobBean() {
        super();
    }



    //获取JobType DCIterator绑定：
    private DCIteratorBinding getNewJobInfoIterator() {
        
       return this.findIterator("JobTypeInfoVO1Iterator");
        
    }
    
    
    //获取DBJobInfoIterator 绑定：
    private DCIteratorBinding getDBJobInfoIterator() {
        
       return this.findIterator("DBJobInfoVO1Iterator");
        
    }
    
    

    //获取 Quartz 中当前运行的Scheduler (单)实例：
    private QuartzSchedulerSingleton getStdSchedulerInBean() {
        
        return QuartzSchedulerSingleton.getInstance();
         
    }



    //保存监听：
    public String saveNewJobAction() throws SchedulerException {
        // Add event code here...
        
        //先执行判断logi
        if (this.judeUserInput() ) {  
            
            Row newJobInfoRow = this.getNewJobInfoIterator().getCurrentRow() ;
            this.jobTypeName = (String)newJobInfoRow.getAttribute("JobTypeName");
            this.jobClassName = (String)newJobInfoRow.getAttribute("JobClassName"); 
            String jobTypeId = (String)this.getNewJobInfoIterator().getCurrentRow().getAttribute("JobTypeId");     
            String jobName_ = this.jobName + this.getTimeStamp();
        
            HashMap<String,String> map = new HashMap<String,String>();
            
            if ("1".equals(jobTypeId)) {
                map.put("uri", this.getUri());
                map.put("requestMessage", this.getRequestMessage());   
                map.put("cronExpr", this.getCronExpr());
                
                
                WSClientUtils u = new WSClientUtils();
                try{
                    String res = u.callWebService(requestMessage,uri) ;
                    
                    this.getStdSchedulerInBean().scheduleJobMap(jobName_, this.jobClassName, this.getCronExpr(),map);
                        
                    this.fireFacesMessage("info", "WebService Job调用成功");  
                    this.findOperation("Rollback").execute();
                    this.clearInputComponent();
                    return "back";   
                    
                }catch(Exception e) {            
                    this.fireFacesMessage("error", "WebService URI或请求消息内容有误，请检查！");  
                    
                }    
            } else if("2".equals(jobTypeId)) {
                
                Row row = this.getDBJobInfoIterator().getCurrentRow();
                String dbConnTypeId = (String)row.getAttribute("ConnTypeId");
                
                if("1".equals(dbConnTypeId)) {
                    
                    String hostName =  (String)row.getAttribute("HostName");  
                    String port =  (String)row.getAttribute("Port"); 
                    String sid =  (String)row.getAttribute("SId"); 
                    String username =  (String)row.getAttribute("UserName"); 
                    String pwd =  (String)row.getAttribute("Pwd"); 
                    String script =  (String)row.getAttribute("PlSql"); 
                    
                    map.put("hostName", hostName);
                    map.put("port", port);
                    map.put("sid", sid);
                    map.put("username", username);
                    map.put("pwd", pwd);
                    map.put("script", script);
                    map.put("connType", "jdbcURL");
                    
                    DBConnUtils dbUtils = new DBConnUtils();
                    
                    try {
                       Connection conn = dbUtils.getJDBCConn(hostName, port, sid, username, pwd);
                       CallableStatement stmt = conn.prepareCall(script);  
                    //                   stmt.execute(); 不需要执行，便可以尝试利用用户提供的数据 进行验证。
                       conn.close();
                        
                        this.getStdSchedulerInBean().scheduleJobMap(jobName_, this.jobClassName, this.getCronExpr(),map);
                            
                        this.fireFacesMessage("info", "数据库pl/sql调用成功");  
                        this.findOperation("Rollback").execute();
                        this.clearInputComponent();
                        return "back";    
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.fireFacesMessage("error", "提供的jdbc URL信息有误，请检查！");
                    } 
                    
                }else if("2".equals(dbConnTypeId)) {
                    
                    String jndiName = (String)row.getAttribute("JndiName"); 
                    String plsql2 = (String)row.getAttribute("PlSql2"); 
                    
                    map.put("jndiName", jndiName);
                    map.put("plsql2", plsql2);
                    map.put("connType", "jdbcDS");
                    
                    DBConnUtils dbUtils = new DBConnUtils();
                    
                    try {
                        Connection conn = dbUtils.getJNDIConnectionByContainer(jndiName);
                        
                      if(null != conn) {
                        CallableStatement stmt = conn.prepareCall(plsql2);
                        conn.close();
                        
                        this.getStdSchedulerInBean().scheduleJobMap(jobName_, this.jobClassName, this.getCronExpr(),map);
                            
                        this.fireFacesMessage("info", "数据库pl/sql调用成功");  
                        this.findOperation("Rollback").execute();
                        this.clearInputComponent();
                        return "back"; 
                      }else {
                        this.findOperation("Rollback").execute();
                        this.refreshComponent(this.panelGL);
                        this.addInfoMessage(null, "由于网络连接异常，请再次重新输入！");
                      }
                        
                        
                    } catch (SQLException e) {
                        e.printStackTrace();
                        this.fireFacesMessage("error", "提供的jdbc DataSource信息有误，请检查！");
                    }
                      
                }            
               
            }  
            
                
        }else {
            
            this.fireFacesMessage("error","请输入必输项（*）！再执行保存操作");
        }
        
            
        
        return null;
    }


    
    //清空新增页面输入组件的值：
    private void clearInputComponent() {
        
        this.jobName = null;
        this.jobDesc = null;
        this.cronExpr = null;
        
        this.uri = null;
        this.requestMessage = null;
            
    }





    //验证必输项目
    private boolean judeUserInput() {
        
        boolean flag = false;
        
        
        if(this.getJobName() != null && this.getCronExpr() != null) {
            
            //1 代表WebService , 2 代表DB job
            String jobTypeId = (String)this.resolveExpression("#{bindings.JobTypeId.inputValue}");
            if("1".equals(jobTypeId)) {
                if(this.getUri() != null && this.getRequestMessage() != null) {
                    flag = true;       
                } 
            }else if("2".equals(jobTypeId)){
                //...
                Row row = this.getDBJobInfoIterator().getCurrentRow();
                
                String dbConnTypeId = (String)row.getAttribute("ConnTypeId");
                
                if("1".equals(dbConnTypeId)) {
                    if( null != row.getAttribute("HostName") && null != row.getAttribute("Port") &&
                        null != row.getAttribute("SId") && null != row.getAttribute("UserName") &&
                        null != row.getAttribute("Pwd") && null != row.getAttribute("PlSql") ) {
                        
                        flag = true;  
                    }
                }else if ("2".equals(dbConnTypeId)) {
                    if( null != row.getAttribute("JndiName") && 
                        null != row.getAttribute("PlSql2") ) {
                        
                        flag = true;  
                    }
                    
                }
                
                
            }  else {
                flag = true;
            } 
        }
         
        return flag;    
    }








    //获取时间戳字符串
    private String getTimeStamp() {
        
        String sDateTime = null;
        
            Long startTime = System.currentTimeMillis();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd_HHmmss");
            java.util.Date dt = new Date(startTime);
            sDateTime = sdf.format(dt); 
        
        return sDateTime;    
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
    



    public void backLsnr(ActionEvent actionEvent) {
        // Add event code here...
        this.clearInputComponent();
    }



    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setCronExpr(String cronExpr) {
        this.cronExpr = cronExpr;
    }

    public String getCronExpr() {
        return cronExpr;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getRequestMessage() {
        return requestMessage;
    }


  public void setPanelGL(RichPanelGroupLayout panelGL) {
    this.panelGL = panelGL;
  }

  public RichPanelGroupLayout getPanelGL() {
    return panelGL;
  }
}
