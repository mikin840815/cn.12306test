package cn.websuper.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import cn.websuper.constant.Constants;
import cn.websuper.domain.validate.GroupSingleton;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 容器初始化监听类
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
public class ApplicationContextListener implements ServletContextListener {
	
	private final static Logger logger = Logger.getLogger(ApplicationContextListener.class);

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.info("servlet上下文销毁!");
	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.info("正在初始化上下文监听器");
		//生成字典
		if(GroupSingleton.getInstance().size()==0){
			if (Constants.readfile(Constants.PIC_RESOURCES)) {
				logger.info("生成成功!");
			} else {
				logger.info("生成失败!");
			}
		}
	}
}
