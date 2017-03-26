package com.znsh.sdk.pojo;

import java.io.Serializable;
import java.util.List;


/**
 * Api返回Json
 * @author zhaoyiwei
 * code Number 接口调用状态，200:正常，其他值：调用出错，返回码见 响应返回码 
 * message String 结果说明，如果接口调用出错，那么返回错误描述，成功返回 ok 
 * result String 接口返回结果
 */
public class ReturnJson implements Serializable {
	private static final long serialVersionUID = -7446615480949436588L;
	private int code;
	private String message;
	private List<Result> result;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Result> getResult() {
		return result;
	}

	public void setResult(List<Result> result) {
		this.result = result;
	}

}
