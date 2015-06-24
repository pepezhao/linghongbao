package com.bridge.shenzhoucheng.model;

public class UserModel {

	private String uid;// 用户id
	private String remainder;// 余额
	private String points;// 积分
	private String ordernum;// 订单数量
	private String tel;// 绑定手机号码
	private String giftnum;// 礼品数量
	private String redgiftnum;// 红包数量
	private String cardnum;// 卡片数量
	private String bankurl;// 银行联名卡网页url
	private String banknum;// 联名银行卡数量

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRemainder() {
		return remainder;
	}

	public void setRemainder(String remainder) {
		this.remainder = remainder;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getGiftnum() {
		return giftnum;
	}

	public void setGiftnum(String giftnum) {
		this.giftnum = giftnum;
	}

	public String getRedgiftnum() {
		return redgiftnum;
	}

	public void setRedgiftnum(String redgiftnum) {
		this.redgiftnum = redgiftnum;
	}

	public String getCardnum() {
		return cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	public String getBankurl() {
		return bankurl;
	}

	public void setBankurl(String bankurl) {
		this.bankurl = bankurl;
	}

	public String getBanknum() {
		return banknum;
	}

	public void setBanknum(String banknum) {
		this.banknum = banknum;
	}

	@Override
	public String toString() {
		return "UserModel [uid=" + uid + ", remainder=" + remainder
				+ ", points=" + points + ", ordernum=" + ordernum + ", tel="
				+ tel + ", giftnum=" + giftnum + ", redgiftnum=" + redgiftnum
				+ ", cardnum=" + cardnum + ", bankurl=" + bankurl
				+ ", banknum=" + banknum + "]";
	}

}
