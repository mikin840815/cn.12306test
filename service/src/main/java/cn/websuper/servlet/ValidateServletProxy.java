package cn.websuper.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 代理
 * </p>
 * <p>
 * Copyright: Copyright (c) 12306test.com 2015
 * </p>
 * <p>
 * Company: websuper
 * </p>
 * 
 * @author [mikin840815] [58294114@qq.com]
 * @version 1.0
 */
public class ValidateServletProxy extends GenericServlet {  

	private static final long serialVersionUID = 2012374931287002717L;
	
	private String targetBean;  
    private Servlet proxy;  
  
    @Override  
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {  
        proxy.service(req, res);  
    }  
  
    @Override  
    public void init() throws ServletException {  
        this.targetBean = getServletName();  
        getServletBean();  
        proxy.init(getServletConfig());  
    }  
  
    private void getServletBean() {  
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());  
        this.proxy = (Servlet) wac.getBean(targetBean);  
    }  
}  
