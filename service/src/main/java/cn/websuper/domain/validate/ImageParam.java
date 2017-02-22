package cn.websuper.domain.validate;

import java.io.Serializable;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 图片对象
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
public class ImageParam implements Serializable{

	private static final long serialVersionUID = -2873521745014315794L;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String data;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public ImageParam(String name, String data) {
		this.name = name;
		this.data = data;
	}
	
}
