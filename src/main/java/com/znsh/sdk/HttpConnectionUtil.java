package com.znsh.sdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Created by soap on 16/7/4. 上传文件并返回结果
 */

public class HttpConnectionUtil {

	/**
	 * 
	 * @param url			请求路径
	 * @param secretId		以后secretId
	 * @param timestamp		时间戳
	 * @param nonce			随机码
	 * @param signature		签名
	 * @param expire		失效时间 < 3600s
	 * @return
	 */
	public static String getAuth(String url, String secretId,
			String timestamp, String nonce, String signature,int expire){
		String result = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("expire", expire+""));
			params.add(new BasicNameValuePair("secret_id", secretId));
			params.add(new BasicNameValuePair("timestamp", timestamp));
			params.add(new BasicNameValuePair("nonce", nonce));
			params.add(new BasicNameValuePair("signature", signature));
			httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpEntity, "utf-8");
			}
			response.close();
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * @param url		请求路径
	 * @param key		接口1返回key+文件名
	 * @param policy	接口1返回的policy 失效期内可以一直使用
	 * @param signature	签名
	 * @param accessId	权限id
	 * @param file		本地文件 一次上传一个
	 * @return boolean	上传成功/失败
	 */
	public static boolean uploadImg(String url, String key, String policy,
			String signature, String ossAccessKeyId, File file) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			FileBody fileBody = new FileBody(file);
			HttpEntity reqEntity = MultipartEntityBuilder
					.create()
					.addPart("key", new StringBody(key, ContentType.TEXT_PLAIN))
					.addPart("policy",
							new StringBody(policy, ContentType.TEXT_PLAIN))
					.addPart("signature",
							new StringBody(signature, ContentType.TEXT_PLAIN))
					.addPart("ossaccesskeyid",
							new StringBody(ossAccessKeyId, ContentType.TEXT_PLAIN))
					.addPart("file", fileBody).build();
			httppost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 204) {
				return true;
			}
			response.close();
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param url			请求路径
	 * @param secretId		以后secretId
	 * @param timestamp		时间戳
	 * @param nonce			随机码
	 * @param signature		签名
	 * @param product		识别种类
	 * @param fileLists		文件名列表 key+文件名  < 10个 
	 * @return json			返回json格式结果
	 */
	public static String fastDetect(String url, String secretId,
			String timestamp, String nonce, String signature, String product,
			List<String> fileLists) throws Exception {
		String result = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("secret_id", secretId));
			params.add(new BasicNameValuePair("timestamp", timestamp));
			params.add(new BasicNameValuePair("nonce", nonce));
			params.add(new BasicNameValuePair("signature", signature));
			params.add(new BasicNameValuePair("product", product));
			for (String file : fileLists) {
				params.add(new BasicNameValuePair("images", file));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpEntity, "utf-8");
			}
			response.close();
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * @param url			请求路径
	 * @param secretId		以后secretId
	 * @param timestamp		时间戳
	 * @param nonce			随机码
	 * @param signature		签名
	 * @param product		识别种类
	 * @param images		图片url列表
	 * @return
	 */
	public static String detectImageUrl(String url,String secretId,
			String timestamp, String nonce, String signature, String product,List<String>images ){
		String result = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("secret_id", secretId));
			params.add(new BasicNameValuePair("timestamp", timestamp));
			params.add(new BasicNameValuePair("nonce", nonce));
			params.add(new BasicNameValuePair("signature", signature));
			params.add(new BasicNameValuePair("product", product));
			for (String image : images) {
				params.add(new BasicNameValuePair("image", image));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpEntity, "utf-8");
			}
			response.close();
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
