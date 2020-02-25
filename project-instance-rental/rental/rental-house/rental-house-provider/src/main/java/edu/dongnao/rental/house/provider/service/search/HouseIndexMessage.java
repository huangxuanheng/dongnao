package edu.dongnao.rental.house.provider.service.search;

/**
 * 房源索引操作消息
 */
public class HouseIndexMessage {
	// 索引操作动作
    public static final String INDEX = "index";		// 构建索引
    public static final String REMOVE = "remove";	// 删除索引

    public static final int MAX_RETRY = 3;		// 消息重试次数

    private Long houseId;
    private String operation;
    private int retry = 0;

    /**
     * 默认构造器 防止jackson序列化失败
     */
    public HouseIndexMessage() {
    }

    public HouseIndexMessage(Long houseId, String operation, int retry) {
        this.houseId = houseId;
        this.operation = operation;
        this.retry = retry;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
