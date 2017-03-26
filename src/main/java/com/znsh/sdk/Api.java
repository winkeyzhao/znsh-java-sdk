package com.znsh.sdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.znsh.sdk.pojo.OtherReturnJson;
import com.znsh.sdk.pojo.Result;
import com.znsh.sdk.pojo.ReturnJson;
import com.znsh.sdk.util.SignatureUtil;

public class Api {
	private String secretId;
	private String secretKey;

	private String key;
	private long expiredTime;
	private String policy;
	private String signature;
	private String ossAccessKeyId;
	final int DEFAULT_EXPIRED_TIME = 1800;

	public Api(String secretId, String secretKey) {
		this.secretId = secretId;
		this.secretKey = secretKey;
	}

	/**
	 * 高级接口 1 获取授权
	 * 
	 * @param expire
	 *            授权时间最大3600s
	 * @return
	 */
	public Map<String, String> getAuth(int expire) {
		try {
			if (expire <= 0) {
				expire = DEFAULT_EXPIRED_TIME;
			}
			String timestamp = System.currentTimeMillis() + "";
			String nonce = getNonce();
			String signature = SignatureUtil.signature(secretKey, secretId
					+ timestamp + nonce);
			String json = HttpConnectionUtil.getAuth(
					"http://api.zhinengsh.com/auth/path", secretId, timestamp,
					nonce, signature, expire);

			OtherReturnJson result = JSON.parseObject(json,
					OtherReturnJson.class);
			return result.getResult();
		} catch (Exception e) {
			System.out.println("高级接口 get auth 调用出错!");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 高级接口 2 上传图片 结合其他高级接口使用
	 * 
	 * @param file
	 *            文件
	 * @param fileName
	 *            文件名
	 * @param policy
	 *            高级接口1返回的授权策略码
	 * @param signature
	 *            高级接口1授权返回的授权签名
	 * @param key
	 *            高级接口1返回的授权key值
	 * @param ossAccessKeyId
	 *            高级接口1返回的授权id
	 * @return
	 */
	public boolean uploadimg(File file, String fileName, String policy,
			String signature, String key, String ossAccessKeyId) {
		String name = key + fileName;
		return HttpConnectionUtil.uploadImg("http://uploadimg.zhinengsh.com",
				name, policy, signature, ossAccessKeyId, file);
	}

	/**
	 * 高级接口 3 识别图片 结合其他高级接口使用
	 * 
	 * @param product
	 *            识别类别 可以批量,半角','隔开如101,102
	 * @param fileLists
	 *            文件名列表(key+上传图片的文件名)
	 * @return
	 */
	public ReturnJson fastDetect(String product, List<String> fileLists) {
		try {
			String url = "http://api.zhinengsh.com/fast/detect";
			String timestamp = System.currentTimeMillis() + "";
			String nonce = getNonce();
			String signature = SignatureUtil.signature(secretKey, secretId
					+ timestamp + nonce);
			String json = HttpConnectionUtil.fastDetect(url, secretId,
					timestamp, nonce, signature, product, fileLists);
			return JSON.parseObject(json, ReturnJson.class);
		} catch (Exception e) {
			System.out.println("高级接口 fast detect 调用出错!");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 识别图片接口
	 * 
	 * @param product
	 *            识别类别 可以批量,半角','隔开如101,102
	 * @param fileLists
	 *            本地图片列表 < 10
	 * @return 返回结果 注返回结果里面的文件名为key+文件名
	 */
	public List<Result> detectImages(String product, List<File> fileLists) {
		List<Result> res = new ArrayList<Result>();
		long current = System.currentTimeMillis() / 1000 + 60;
		if (current > expiredTime) {
			Map<String, String> authMap = getAuth(DEFAULT_EXPIRED_TIME);
			if (authMap != null && authMap.size() > 0) {
				this.key = authMap.get("key");
				this.expiredTime = Long.parseLong(authMap.get("expire"));
				this.policy = authMap.get("policy");
				this.signature = authMap.get("signature");
				this.ossAccessKeyId = authMap.get("ossaccesskeyid");
			} else {
				return null;
			}
		}
		List<String> fileNames = new ArrayList<String>();
		for (File file : fileLists) {
			uploadimg(file, file.getName(), policy, signature, key,
					ossAccessKeyId);
			fileNames.add(key + file.getName());
		}
		ReturnJson json = fastDetect(product, fileNames);
		if (json.getCode() == 200) {
			res = json.getResult();
		}
		return res;
	}

	/**
	 * 识别URL图片接口
	 * 
	 * @param product
	 *            识别类别 可以批量,半角','隔开如101,102
	 * @param images
	 *            数量不超过5个
	 * @return
	 */
	public List<Result> detectImageUrls(String product, List<String> images) {
		List<Result> res = new ArrayList<Result>();
		String url = "http://api.zhinengsh.com/image/detect_url";
		String timestamp = System.currentTimeMillis() + "";
		String nonce = getNonce();
		String signature = SignatureUtil.signature(secretKey, secretId
				+ timestamp + nonce);
		ReturnJson json = JSON.parseObject(HttpConnectionUtil.detectImageUrl(
				url, secretId, timestamp, nonce, signature, product, images),
				ReturnJson.class);
		if (json.getCode() == 200) {
			res = json.getResult();
		}
		return res;
	}

	private String getNonce() {
		Random random = new Random(System.currentTimeMillis());
		String nonce = Math.abs(random.nextInt()) % 1000000 + "";
		return nonce;
	}

	public long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOssAccessKeyId() {
		return ossAccessKeyId;
	}

	public void setOssAccessKeyId(String ossAccessKeyId) {
		this.ossAccessKeyId = ossAccessKeyId;
	}

}
