package com.woniu.action.pojo;

public class user {
   
	private Integer uid;
	private String user;
	private String upsw;
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUpsw() {
		return upsw;
	}
	public void setUpsw(String upsw) {
		this.upsw = upsw;
	}
	public user(Integer uid, String user, String upsw) {
		super();
		this.uid = uid;
		this.user = user;
		this.upsw = upsw;
	}
	public user() {
		super();
	}
	@Override
	public String toString() {
		return "user [uid=" + uid + ", user=" + user + ", upsw=" + upsw + "]";
	}
	
	
	
}
