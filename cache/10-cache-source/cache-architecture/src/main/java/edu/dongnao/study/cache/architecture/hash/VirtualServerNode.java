package edu.dongnao.study.cache.architecture.hash;

/**
 */
public class VirtualServerNode {
    private String serverNodeName; //真实的物理服务器节点名字
    private long virtualServerNodeHash;

    public VirtualServerNode(String serverNodeName, long virtualServerNodeHash) {
        super();
        this.serverNodeName = serverNodeName;
        this.virtualServerNodeHash = virtualServerNodeHash;
    }
    public String getServerNodeName() {
        return serverNodeName;
    }
    public void setServerNodeName(String serverNodeName) {
        this.serverNodeName = serverNodeName;
    }
    public long getVirtualServerNodeHash() {
        return virtualServerNodeHash;
    }
    public void setVirtualServerNodeHash(long virtualServerNodeHash) {
        this.virtualServerNodeHash = virtualServerNodeHash;
    }

}
