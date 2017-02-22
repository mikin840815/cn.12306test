package cn.websuper.constant.validate;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 认证结果枚举
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
public enum ValidateEnum {

	FAILED(10001, "FAILED", "认证失败"), // 认证失败
	SUCCESS(10002, "SUCCESS", "认证成功");// 认证成功

	/**
	 * 类型
	 */
	private int featureType;
	/**
	 * 名称
	 */
	private String featureName;
	/**
	 * 描叙
	 */
	private String description;

	/**
	 * 初始化
	 * 
	 * @param featureType
	 * @param featureName
	 * @param description
	 */
	ValidateEnum(int featureType, String featureName, String description) {
		this.featureType = featureType;
		this.featureName = featureName;
		this.description = description;
	}

	public static ValidateEnum getValueByType(int featureType) {
		for (ValidateEnum enums : values()) {
			if (enums.getFeatureType() == featureType) {
				return enums;
			}
		}
		return null;
	}

	public static ValidateEnum getValueByName(String featureName) {
		for (ValidateEnum enums : values()) {
			if (enums.getFeatureName().equalsIgnoreCase(featureName)) {
				return enums;
			}
		}
		return null;
	}

	/**
	 * @return the featureType
	 */
	public int getFeatureType() {
		return featureType;
	}

	/**
	 * @return the featureName
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
