package com.znsh.sdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.znsh.sdk.pojo.Result;

public class ApiDemo {
	public static void main(String[] args) {
		
		Api api = new Api("yourSecretId", "yourSecretKey");
		
		
		//根据图片url识别
		List<String> images = new ArrayList<String>();
		images.add("http://image_url");
		images.add("http://image_url");
		List<Result> resultList = api.detectImageUrls("101,102,103", images);
		for (Result result : resultList) {
			System.out.println(result.getProduct());
			System.out.println(result.getLabel());
		}
		
		//识别本地图片
		List<File> fileList = new ArrayList<File>();
		fileList.add(new File("imagePath"));
		fileList.add(new File("imagePath"));
		
		resultList = api.detectImages("101,102,103", fileList);
		for (Result result : resultList) {
			System.out.println(result.getProduct());
			System.out.println(result.getLabel());
		}

		
		
	}
	
	
}
