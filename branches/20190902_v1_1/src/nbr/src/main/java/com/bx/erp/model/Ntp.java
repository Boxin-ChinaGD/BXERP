package com.bx.erp.model;

public class Ntp extends BaseModel {
	private static final long serialVersionUID = 167373781403155563L;
	public static final NtpField field = new NtpField();

	private long t1; // 同步者发送同步请求的时间

	private long t2; // 同步者发送同步请求到达的时间

	private long t3; // 被同步者返回请求时的时间

	private long t4; // 同步者接受到响应的时间

	public long getT1() {
		return t1;
	}

	public void setT1(long t1) {
		this.t1 = t1;
	}

	public long getT2() {
		return t2;
	}

	public void setT2(long t2) {
		this.t2 = t2;
	}

	public long getT3() {
		return t3;
	}

	public void setT3(long t3) {
		this.t3 = t3;
	}

	public long getT4() {
		return t4;
	}

	public void setT4(long t4) {
		this.t4 = t4;
	}

	@Override
	public String toString() {
		return "NtpModel [t1=" + t1 + ", t2=" + t2 + ", t3=" + t3 + ", t4=" + t4 + "]";
	}

	@Override
	public String checkUpdate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return "";
	}

	@Override
	public String checkRetrieveN(int iUseCaseID) {
		return "";
	}
}
