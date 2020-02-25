package edu.dongnao.study.balancer.lesson2_security;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.junit.Test;

/**
 * PemSginTest
 * 
 */
public class PemSginTest {
	final static Base64.Decoder decoder = Base64.getDecoder();
	final static Base64.Encoder encoder = Base64.getEncoder();
	
	@Test
	public void test1() {
		try {
			URL url = PemSginTest.class.getClassLoader().getResource("servercert.pem");   //证书路径
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate)cf.generateCertificate(new FileInputStream(url.getFile()));
			PublicKey publicKey = cert.getPublicKey();
			String publicKeyString = encoder.encodeToString(publicKey.getEncoded());
			System.out.println("-----------------公钥--------------------");
			System.out.println(publicKeyString);
			System.out.println("-----------------公钥--------------------");
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		try {
			URL url = PemSginTest.class.getClassLoader().getResource("servercert.pem");
			BufferedReader br = new BufferedReader(new FileReader(url.getPath()));
			String s = br.readLine();
			s = br.readLine();

			StringBuffer publickey = new StringBuffer();
			while (s!= null && s.charAt(0) != -1) {
			    publickey.append(s );
			    s = br.readLine();
			    System.out.println("s:"+s);
			}
			System.out.println("publickey.toString().getBytes(\"UTF-8\"): "+publickey.toString().getBytes("UTF-8"));
			byte[] keyBytes = decoder.decode(publickey.toString().getBytes("UTF-8"));
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			System.out.println(encoder.encodeToString(publicKey.getEncoded()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

