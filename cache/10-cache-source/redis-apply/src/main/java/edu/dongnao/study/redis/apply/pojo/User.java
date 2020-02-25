package edu.dongnao.study.redis.apply.pojo;

import java.io.Serializable;

public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
    private String userId;

    public User(String userId, String userName) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

	@Override
	public String toString() {
		return "User [userName=" + userName + ", userId=" + userId + "]";
	}
    
}
