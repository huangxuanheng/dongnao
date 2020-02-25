package edu.dongnao.study.rpc.client.net;

import edu.dongnao.study.rpc.discovery.ServiceInfo;

public interface NetClient {
	byte[] sendRequest(byte[] data, ServiceInfo sinfo);
}
