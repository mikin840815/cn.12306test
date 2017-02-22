package cn.websuper.domain.validate;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 单例
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
public class GroupSingleton {
	
	private static List<GroupParams> instance;
    private GroupSingleton (){}

    public static List<GroupParams> getInstance() {
		if (instance == null) {
		    instance = new ArrayList<GroupParams>();
		}
		return instance;
    }
    
    public static boolean clear(){
    	instance = null;
    	return true;
    }
}
