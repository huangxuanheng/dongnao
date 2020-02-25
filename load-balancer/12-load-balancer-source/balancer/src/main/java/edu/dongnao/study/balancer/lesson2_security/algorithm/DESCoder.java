package edu.dongnao.study.balancer.lesson2_security.algorithm;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES-Data Encryption Standard,即数据加密算法。 DES安全编码组件，
 * DES算法的入口参数有三个:Key、Data、Mode。
 * 其中Key为8个字节共64位,是DES算法的工作密钥; Data也为8个字节64位,
 * 是要被加密或被解密的数据;
 * Mode为DES的工作方式,有两种:加密或解密。 DESCoder
 * 
 */
public class DESCoder {
	public static final String ALGORITHM = "DES";

	/**
	 * 转换密钥
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
		// SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);

		return secretKey;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);

		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);

		return cipher.doFinal(data);
	}

	/**
	 * 生成密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {
		return initKey(null);
	}

	/**
	 * 生成密钥
	 * 
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKey(byte[] seed) throws Exception {
		SecureRandom secureRandom = null;

		if (seed != null) {
			secureRandom = new SecureRandom(seed);
		} else {
			secureRandom = new SecureRandom();
		}

		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(secureRandom);

		SecretKey secretKey = kg.generateKey();

		return secretKey.getEncoded();
	}

	public static void main(String[] args) {
		try {
			String inputStr = "DES";
			byte[] key = DESCoder.initKey();
			System.err.println("原文:\t" + inputStr);
			System.err.println("密钥:\t" + key);

			byte[] inputData = inputStr.getBytes();
			inputData = DESCoder.encrypt(inputData, key);

			System.err.println("加密后:\t" + inputData);

			byte[] outputData = DESCoder.decrypt(inputData, key);
			String outputStr = new String(outputData);

			System.err.println("解密后:\t" + new String(outputStr));

			assert inputStr.equals(outputStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

