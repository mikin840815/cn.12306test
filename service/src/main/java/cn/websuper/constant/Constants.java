package cn.websuper.constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.websuper.domain.validate.GroupParams;
import cn.websuper.domain.validate.GroupSingleton;
import cn.websuper.domain.validate.ImageParam;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 常量
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
public class Constants {
	 /**
     * 编码
     */
    public final static String UTF8 = "utf-8";
    
    private static String getLableValue(String key) {
		return ConfigHelper.getDefault().getString(key);
	}
	
	public final static String PIC_RESOURCES=Constants.getLableValue("pic.resources");
	public final static String PIC_CREATE_PATH=Constants.getLableValue("pic.create.path");
    
    /**
	 * 读取某个文件夹下的所有文件
	 */
	public static boolean readfile(String path) { 
    	boolean flag = false;
    	try{
	    	Map<String, List<ImageParam>> map = new HashMap<String, List<ImageParam>>();
	        File file = new File(path);  
	        if (file.isDirectory()) {  
	            File[] dirFile = file.listFiles();  
	            for (File f : dirFile) {  
	                if (f.isDirectory())  
	                	readfile(f.getAbsolutePath());  
	                else {  
	                	int len = 0;
						FileInputStream inputStream = new FileInputStream(
								f);
						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						byte[] buffer = new byte[1024];
						while ((len = inputStream.read(buffer)) != -1) {
							outStream.write(buffer, 0, len);
						}
						//byte[] buf = outStream.toByteArray();
						//Base64 encoder = new Base64();
						String str = f.getName();
						//System.out.println("fileName = "+str);
						String arr[] = str.split("_");
						String name = arr[0];
						List<ImageParam> sp = null;
						if (map.containsKey(name)) {
							sp = (List<ImageParam>) map.get(name);
						} else {
							sp = new ArrayList<ImageParam>();
						}
						//sp.add(new ImageParam(RandomUUID.get(6), encoder.encode(buf)));
						//sp.add(new ImageParam(RandomUUID.get(6), str));
						sp.add(new ImageParam(name, str));
						map.put(name, sp);
	                }  
	            }  
	        }  
	        Iterator<Entry<String, List<ImageParam>>> it = map.entrySet()
					.iterator();
			while (it.hasNext()) {
				Entry<String, List<ImageParam>> entry = it.next();
				GroupSingleton.getInstance().add(new GroupParams(entry.getKey(), entry.getValue()));
			}
	        flag = true;
    	}catch(Exception e){
    		System.out.println("readfile()   Exception:" + e.getMessage());
    	}
    	return flag;
    }  
    
}