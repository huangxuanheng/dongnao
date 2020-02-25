package edu.dongnao.study.balancer.lesson2_security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Base64;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.dongnao.study.balancer.lesson2_security.algorithm.RSACoder;

/**
 * RSACoderTest
 * 
 */
public class RSACoderTest {
	private byte[] publicKey;
	private byte[] privateKey;
	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();

	@Before
	public void setUp() throws Exception {
		Map<String, Object> keyMap = RSACoder.initKey();
		publicKey = RSACoder.getPublicKey(keyMap);
		privateKey = RSACoder.getPrivateKey(keyMap);
		System.out.println("获取新的秘钥对，私钥公钥");
	}
	@After
	public void after() {
		System.out.println();
	}

	@Test
	public void test() throws Exception {
		System.out.println("=====================公钥加密——私钥解密=====================");
		System.out.println("公钥：\r" + encoder.encodeToString(publicKey));
		System.out.println("私钥：\r" + encoder.encodeToString(privateKey)+ "\n\r");
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();
		// 客户端用公钥加密数据
		byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);
		
		// 服务端用私钥解密数据
		byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData, privateKey);
		
		// 解密后的内容
		String outputStr = new String(decodedData);
		System.out.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		assertEquals(inputStr, outputStr);

	}

	/**
	 * 数字签名验证
	  * <ul>
	 * <li>甲方构建密钥对儿，将公钥公布给乙方，将私钥保留。
	 * <li>甲方使用私钥加密数据，然后用私钥对加密后的数据签名，发送给乙方签名以及加密后的数据；
	 * <li>乙方使用公钥、签名来验证待解密数据是否有效，如果有效使用公钥对数据解密。
	 * <li>乙方使用公钥加密数据，向甲方发送经过加密后的数据；甲方获得加密数据，通过私钥解密。 
	 * </ul>
	 * @throws Exception
	 */
	@Test
	public void testSign() throws Exception {
		System.out.println("=====================验证数字签名=====================");
		System.out.println("公钥：\r" + encoder.encodeToString(publicKey));
		System.out.println("私钥：\r" + encoder.encodeToString(privateKey)+ "\n\r");
		String inputStr = "sign";
		byte[] data = inputStr.getBytes();
		// 服务端用私钥加密
		byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);
		// 客户端用公钥解密
		byte[] decodedData = RSACoder.decryptByPublicKey(encodedData, publicKey);

		String outputStr = new String(decodedData);
		System.out.println("加密前：" + inputStr + "\n\r解密后: " + outputStr+"\r\n");
		assertEquals(inputStr, outputStr);

		System.out.println("私钥签名——公钥验证签名");
		// 产生签名
		byte[] sign = RSACoder.sign(encodedData, privateKey);
		System.out.println("签名：\r\n" + encoder.encodeToString(sign)+"\r\n");

		// 验证签名
		boolean status = RSACoder.verify(encodedData, publicKey, sign);
		System.out.println("签名验证结果：" + status);
		assertTrue(status);

	}
}
