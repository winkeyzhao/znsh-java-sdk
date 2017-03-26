package com.znsh.sdk.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignatureUtil {

	private static final String ALGORITHM = "HmacSHA1";

	/**
	 * 生成签名
	 * @param secretKey 用户的secretKey
	 * @param str 用户的secretId+timestamp+nonce
	 * @return
	 */
	public static String signature(String secretKey, String str) {
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(new SecretKeySpec((secretKey).getBytes(), ALGORITHM));
			byte[] signData = mac.doFinal((str).getBytes());
			String signature = new String(Base64Util.encode(signData));
			return signature;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
