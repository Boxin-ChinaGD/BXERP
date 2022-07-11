package com.bx.erp.action.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bx.erp.action.BaseAction;

/**
 * 实现用户在网页端或POS端的单点登录控制逻辑
 */
public class LoginGuard {
	private Log logger = LogFactory.getLog(LoginGuard.class);
	
	public LoginGuard() {
		mapStaffLoginDevice = new HashMap<String, LoginDevice>();
		lock = new ReentrantReadWriteLock();
	}
	
	public class LoginDevice {
		public LoginDevice() {
			// 本系统内部的POSID=INVALID_POS_ID=-1时，代表用户在网页端登录。
			// POSID>0时，代表用户在POS机登录。
			// POSID=0时，代表用户未在网页端和POS机登录。
			clientSessionOfWeb = null;
			posIDOfPos = 0;
		}

		protected HttpSession clientSessionOfWeb;

		public HttpSession getClientSessionOfWeb() {
			return clientSessionOfWeb;
		}

		public void setClientSessionOfWeb(HttpSession clientSessionOfWeb) {
			this.clientSessionOfWeb = clientSessionOfWeb;
		}

		protected int posIDOfPos;

		public int getPosIDOfPos() {
			return posIDOfPos;
		}

		public void setPosIDOfPos(int posIDOfPos) {
			this.posIDOfPos = posIDOfPos;
		}
	}

	/**
	 * Key=Staff.F_ID，Value=BaseModel <br />
	 */
	protected Map<String, LoginDevice> mapStaffLoginDevice;
	
	// 测试中使用判断是否将POSID以及session更换
	public Map<String, LoginDevice> getMapStaffLoginDevice() {
		return mapStaffLoginDevice;
	}

	protected ReentrantReadWriteLock lock;

	private String generateKey(String companySN, int staffID) {
		if (StringUtils.isEmpty(companySN)) {
			logger.error("黑客行为：用户登录时没有传递companySN！");
		}
		return companySN + "-" + String.valueOf(staffID);
	}
	
	/**
	 * 用户（不是POS）登录成功后，可能存在其它同类设备的登录会话，需要用本次登录会话替换掉旧的登录会话。旧的登录会话在发送请求到服务器时将被阻止
	 * @param staffID 当前登录的用户ID
	 * @param posID 当前登录的POSID。如果是BaseAction.INVALID_POS_ID，表明是网页端登录。如果是>0，表明是在POS端登录
	 * @param session 用户登录会话的对象，用来区分不同的连接及其浏览器/设备
	 */
	public void login(String companySN, int staffID, int posID, HttpSession session) {
		if ((posID != BaseAction.INVALID_POS_ID && posID <= 0) || staffID <= 0) {
			return;
		}

		lock.writeLock().lock();

		LoginDevice currentLoginDevice = mapStaffLoginDevice.get(generateKey(companySN, staffID)); 
		if (currentLoginDevice != null) {
			if (posID == BaseAction.INVALID_POS_ID) {
				if (currentLoginDevice.getClientSessionOfWeb() != null && currentLoginDevice.getClientSessionOfWeb().hashCode() != session.hashCode()) {
					logger.debug("踢走旧的网页端登录会话，old session=" + currentLoginDevice.getClientSessionOfWeb());
				}
				currentLoginDevice.setClientSessionOfWeb(session);
			} else {
				if (currentLoginDevice.getPosIDOfPos() != posID) {
					logger.debug("踢走旧的POS端登录会话，old posID=" + currentLoginDevice.getPosIDOfPos());
				}
				currentLoginDevice.setPosIDOfPos(posID);
			}
		} else {
			// 之前未有任何登录操作，网页，pos都未登录过
			LoginDevice ld = new LoginDevice();
			if (posID == BaseAction.INVALID_POS_ID) {
				ld.setClientSessionOfWeb(session); // 网页端登录
			} else {
				ld.setPosIDOfPos(posID); // POS机上登录
			}
			mapStaffLoginDevice.put(generateKey(companySN, staffID), ld);
		}

		lock.writeLock().unlock();
	}
	
	/**
	 * 在拦截器中检查此请求是否为旧的用户会话发出的请求。
	 * @param staffID 当前登录的用户ID
	 * @param posID 当前登录的POSID。如果是BaseAction.INVALID_POS_ID，表明是网页端登录。如果是>0，表明是在POS端登录
	 * @param session 用户登录会话的对象，用来区分不同的连接及其浏览器/设备
	 * @return true，拦截用户的请求，返回错误信息;false，允许用户继续正常操作
	 */
	public boolean needToIntercept(String companySN, int staffID, int posID, HttpSession session) {
		if ((posID != BaseAction.INVALID_POS_ID && posID <= 0) || staffID <= 0) {
			return true;
		}
		
		boolean needToIntercept = false;

		lock.writeLock().lock();

		LoginDevice currentLoginDevice = mapStaffLoginDevice.get(generateKey(companySN, staffID));
		if (currentLoginDevice != null) {
			if (posID == BaseAction.INVALID_POS_ID) {// 网页端登录
				if (currentLoginDevice.getClientSessionOfWeb().hashCode() != session.hashCode()) {
					needToIntercept = true;
				}
			} else {// POS机上登录
				if (currentLoginDevice.getPosIDOfPos() != posID) {
					needToIntercept = true;
				}
			}
		}

		lock.writeLock().unlock();
		
		return needToIntercept;
	}
}
