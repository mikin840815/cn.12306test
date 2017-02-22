package cn.websuper.domain.validate;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 分组对象
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
public class GroupParams implements Serializable {

	private static final long serialVersionUID = 160695438358582371L;
	private String name;
	private List<ImageParam> imageParam;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ImageParam> getImageParam() {
		return imageParam;
	}
	public void setImageParam(List<ImageParam> imageParam) {
		this.imageParam = imageParam;
	}
	public GroupParams(String name, List<ImageParam> imageParam) {
		this.name = name;
		this.imageParam = imageParam;
	}
	
}
