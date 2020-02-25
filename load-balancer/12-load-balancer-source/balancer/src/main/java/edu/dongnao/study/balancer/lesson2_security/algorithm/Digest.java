package edu.dongnao.study.balancer.lesson2_security.algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 单向加密，消息摘要
 * Digest
 * 
 */
public class Digest {
	// JDK8支持的加密种类，参考
	// https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
	private static String KEY_MD5 = "MD5";
	private static String KEY_SHA = "SHA";
	private static String KEY_MAC = "HmacMD5";
	/**
	 * md5加密
	 * <br/>
	 * MD5 -- message-digest algorithm 5 （信息-摘要算法）缩写，
	 * 广泛用于加密和解密技术，常用于文件校验。管文件多大，经过MD5后都能生成唯一的MD5值。
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {  
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		return md5.digest();  
	}
	/**
	 * sha加密
	 * <br/>
	 * SHA(Secure Hash Algorithm，安全散列算法），
	 * 数字签名等密码学应用中重要的工具，被广泛地应用于电子商务等信息安全领域。
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {  
	    MessageDigest md5 = MessageDigest.getInstance(KEY_SHA);
	    md5.update(data);  
	    return md5.digest();  
	}
	
	/** 
          * 初始化HMAC密钥 
     *  
     * @return 
     * @throws Exception 
     */  
    public static byte[] initMacKey() throws Exception {  
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);  
        SecretKey secretKey = keyGenerator.generateKey();  
        return secretKey.getEncoded();  
    }  
  
    /** 
     * HMAC加密 
     * <br/>
     *  HMAC(Hash Message Authentication Code，散列消息鉴别码，
     *  基于密钥的Hash算法的认证协议。消息鉴别码实现鉴别的原理是，
     *  用公开函数和密钥产生一个固定长度的值作为认证标识， 用这个标识鉴别消息的完整性。
     *  使用一个密钥生成一个固定大小的小数据块，即MAC，并将其加入到消息中，
     *  然后传输。接收方利用与发送方共享的密钥进行鉴别认证等。 
     * @param data 
     * @param key 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptHMAC(byte[] data, byte[] key) throws Exception {  
  
        SecretKey secretKey = new SecretKeySpec(key, KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        mac.init(secretKey);  
        return mac.doFinal(data);
  
    }  
	
	public static void main(String[] args) {
		try {
			byte[] md5 = encryptMD5("hello".getBytes());
			System.out.println("hello md5："+new BigInteger(md5).toString(16));
			
			byte[] sha = encryptSHA("hash".getBytes());
			System.out.println("hash sha："+new BigInteger(sha).toString(16));
			
			byte[] macKey = initMacKey();
			byte[] hmac = encryptHMAC("hello hash".getBytes(), macKey);
			System.out.println("hello hash key："+new BigInteger(macKey).toString(16)
					+"\r\n encrypt："+new BigInteger(hmac).toString(16));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

