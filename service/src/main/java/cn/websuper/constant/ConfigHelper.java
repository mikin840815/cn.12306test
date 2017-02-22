package cn.websuper.constant;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import cn.websuper.common.DES3Encrypt;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 配置文件帮助类
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
public class ConfigHelper {

	private static final String CONFIG_PATH_RESOURCE = "conf/resources.properties";
	
	private final static ConfigHelper instance = new ConfigHelper();
	private static Object[] lock = new Object[0];
	private static long lastTime = 0;
	private static long intervalTime = 5 * 60 * 1000; 
	

	private AbstractConfiguration config;

	private ConfigHelper() {
		reload();
	}

	public void reload() {
		PropertiesConfiguration c = null;
		try {
			c = new PropertiesConfiguration(CONFIG_PATH_RESOURCE);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		if (c != null)
			config = c;
	}

	public static ConfigHelper getDefault() {
		
		if (System.currentTimeMillis() - lastTime > intervalTime) {
			synchronized (lock) {
				if (System.currentTimeMillis() - lastTime > intervalTime) {
					lastTime = System.currentTimeMillis();
					instance.reload();
				}
			}
		}
		
		return instance;
	}

	public String getString(String infoLabel) {
		return instance.config.getString(infoLabel);
	}
	
	public byte[] getBytesFromBase64(String infoLabel) {
		String base64 = getString(infoLabel);
		if (base64 == null) {
			throw new NullPointerException("read ：" +CONFIG_PATH_RESOURCE+ " [" + infoLabel + "] is null, can not byte[]");
		}
		return Base64.decodeBase64(base64);
	}

	public String[] getStringArray(String infoLabel) {
		return instance.config.getStringArray(infoLabel);
	}

	public boolean getBoolean(String infoLabel) {
		return instance.config.getBoolean(infoLabel);
	}

	public int getInt(String infoLabel) {
		return instance.config.getInt(infoLabel);
	}

	public int getInt(String infoLabel, int defaultValue) {
		try {
			return instance.config.getInt(infoLabel);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public double getDouble(String infoLabel) {
		return instance.config.getDouble(infoLabel);
	}
	
    //获取解密值
    public String getStringFromDES3Encrypt(String key){
       return DES3Encrypt.decrypt(getString(key));
    }
    /**
     * 获取解密值
     * @param key
     * @return
     */
    public String getDES3String(String key){
    	String v = getString(key);
    	if (v == null) return null;
       return DES3Encrypt.decrypt(v);
    }
    
}
