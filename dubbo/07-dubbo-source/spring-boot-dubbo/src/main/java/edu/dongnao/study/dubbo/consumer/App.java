package edu.dongnao.study.dubbo.consumer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * App
 * 
 */
public class App {
	public static void main(String[] args) {
		try {
			System.out.println(URLDecoder.decode("dubbo%3A%2F%2F192.168.56.1%3A20880%2Fedu.dongnao.study.dubbo.DemoService%3Fanyhost%3Dtrue%26application%3Dhello-world-app%26bean.name%3Dedu.dongnao.study.dubbo.DemoService%26cluster%3Dfailsafe%26default.deprecated%3Dfalse%26default.dynamic%3Dfalse%26default.register%3Dtrue%26deprecated%3Dfalse%26dubbo%3D2.0.2%26dynamic%3Dfalse%26generic%3Dfalse%26interface%3Dedu.dongnao.study.dubbo.DemoService%26methods%3DsayHello%2CsayGood%26pid%3D18096%26register%3Dtrue%26release%3D2.7.1%26sayGood.loadbalance%3Droundrobin%26side%3Dprovider%26timestamp%3D1555586315215, dubbo%3A%2F%2F192.168.56.1%3A40880%2Fedu.dongnao.study.dubbo.DemoService%3Fanyhost%3Dtrue%26application%3Dhello-world-app%26bean.name%3Dedu.dongnao.study.dubbo.DemoService%26cluster%3Dfailsafe%26default.deprecated%3Dfalse%26default.dynamic%3Dfalse%26default.register%3Dtrue%26deprecated%3Dfalse%26dubbo%3D2.0.2%26dynamic%3Dfalse%26generic%3Dfalse%26interface%3Dedu.dongnao.study.dubbo.DemoService%26methods%3DsayHello%2CsayGood%26pid%3D16808%26register%3Dtrue%26release%3D2.7.1%26sayGood.loadbalance%3Droundrobin%26side%3Dprovider%26timestamp%3D1555586509916, dubbo%3A%2F%2F192.168.56.1%3A30880%2Fedu.dongnao.study.dubbo.DemoService%3Fanyhost%3Dtrue%26application%3Dhello-world-app%26bean.name%3Dedu.dongnao.study.dubbo.DemoService%26cluster%3Dfailsafe%26default.deprecated%3Dfalse%26default.dynamic%3Dfalse%26default.register%3Dtrue%26deprecated%3Dfalse%26dubbo%3D2.0.2%26dynamic%3Dfalse%26generic%3Dfalse%26interface%3Dedu.dongnao.study.dubbo.DemoService%26methods%3DsayHello%2CsayGood%26pid%3D6168%26register%3Dtrue%26release%3D2.7.1%26sayGood.loadbalance%3Droundrobin%26side%3Dprovider%26timestamp%3D1555586465600", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
}

