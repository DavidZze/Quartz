package hand.hmw.framework.quartz.view.servlets;


import hand.hmw.framework.quartz.core.QuartzSchedulerSingleton;

import java.io.IOException;

import java.io.Serializable;

import javax.servlet.*;
import javax.servlet.http.*;

public class StartSchedulerOnAppStart extends HttpServlet implements Serializable{
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final long serialVersionUID = 1L;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        QuartzSchedulerSingleton quartzSchedulerSingleton = QuartzSchedulerSingleton.getInstance();
        quartzSchedulerSingleton.getScheduledJobs();
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
                                                           IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException,
                                                            IOException {
       
    }
}
