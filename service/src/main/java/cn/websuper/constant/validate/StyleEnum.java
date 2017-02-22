package cn.websuper.constant.validate;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 验证码风格枚举
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
public enum StyleEnum {
	EIGHT(10001, "eight", 8), SIX(10002, "six", 6), NINE(10003, "nine", 9);

	/**
	 * 类型
	 */
	private int featureType;
	/**
	 * 名称
	 */
	private String featureName;
	/**
	 * 值
	 */
	private int featureValue;

	/**
	 * 初始化
	 * 
	 * @param featureType
	 * @param featureName
	 * @param featureValue
	 */
	StyleEnum(int featureType, String featureName, int featureValue) {
		this.featureType = featureType;
		this.featureName = featureName;
		this.featureValue = featureValue;
	}

	public static StyleEnum getValueByType(int featureType) {
		for (StyleEnum enums : values()) {
			if (enums.getFeatureType() == featureType) {
				return enums;
			}
		}
		return null;
	}

	public static StyleEnum getValueByName(String featureName) {
		for (StyleEnum enums : values()) {
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
	public int getFeatureValue() {
		return featureValue;
	}
}
