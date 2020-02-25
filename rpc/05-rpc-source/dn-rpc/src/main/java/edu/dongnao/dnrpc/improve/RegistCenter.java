package edu.dongnao.dnrpc.improve;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
/**
 * Register
 * 
 */
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;

public class RegistCenter {
	ZkClient client = new ZkClient("localhost:2181");
	
	private String centerRootPath = "/dnRpc";
	
	public RegistCenter() {
		client.setZkSerializer(new MyZkSerializer());
	}
	
	public void regist(ServiceResource serviceResource) {
		String serviceName = serviceResource.getServiceName();
		String uri = JsonMapper.toJsonString(serviceResource);
		try {
			uri = URLEncoder.encode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String servicePath = centerRootPath + "/"+serviceName+"/service";
		if(! client.exists(servicePath)) {
			client.createPersistent(servicePath, true);
		}
		String uriPath = servicePath+"/"+uri;
		client.createEphemeral(uriPath);
	}
	
	/**
	 * 加载配置中心中服务资源信息
	 * @param serviceName
	 * @return
	 */
	public List<ServiceResource> loadServiceResouces(String serviceName) {
		String servicePath = centerRootPath + "/"+serviceName+"/service";
		List<String> children = client.getChildren(servicePath);
		List<ServiceResource> resources = new ArrayList<ServiceResource>();
		for(String ch : children) {
			try {
				String deCh = URLDecoder.decode(ch, "UTF-8");
				ServiceResource r = JsonMapper.fromJsonString(deCh, ServiceResource.class);
				resources.add(r);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return resources;
	}
	
	private void sub(String serviceName, ChangeHandler handler) {
		/*
		String path = centerRootPath + "/"+serviceName+"/service";
		client.subscribeChildChanges(path, new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				handler();
			}
		});
		client.subscribeDataChanges(path, new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				handler();
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				handler();
			}
		});
		*/
	}
	
	interface ChangeHandler {
		/**
		 * 发生变化后给一个完整的属性对象
		 * @param resource
		 */
		void itemChange(ServiceResource resource);
	}
}

