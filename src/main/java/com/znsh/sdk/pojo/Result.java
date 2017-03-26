package com.znsh.sdk.pojo;

import java.io.Serializable;
import java.util.HashMap;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Api返回Json
 * 
 * @author zhaoyiwei
 *
 */
public class Result implements Serializable {
	private static final long serialVersionUID = 5535924048731710836L;
	
	@JSONField(ordinal=1)
	private int product;
	@JSONField(ordinal=2)
	private HashMap<String, HashMap<Integer, Double>> label;
	public int getProduct() {
		return product;
	}
	public void setProduct(int product) {
		this.product = product;
	}
	public HashMap<String, HashMap<Integer, Double>> getLabel() {
		return label;
	}
	public void setLabel(HashMap<String, HashMap<Integer, Double>> label) {
		this.label = label;
	}
	
}
/**
 * code Number 接口调用状态，200:正常，其他值：调用出错，返回码见 响应返回码 msg String
 * 结果说明，如果接口调用出错，那么返回错误描述，成功返回 ok result String 接口返回结果，各个接口自定义
 **/
