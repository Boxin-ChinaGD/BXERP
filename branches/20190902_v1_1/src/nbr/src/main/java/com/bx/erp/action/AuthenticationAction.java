package com.bx.erp.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;

import com.bx.erp.action.bo.BaseAuthenticationBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseAuthenticationModel;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RSAInfo;
import com.bx.erp.model.config.BxConfigGeneral;

@Scope("prototype")
public class AuthenticationAction extends BaseAction {
	private static Log logger = LogFactory.getLog(AuthenticationAction.class);

	protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private SimpleDateFormat sdfLoginCountIn1Day = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1, Locale.ENGLISH);

	protected static Map<String, Object> mapLoginDay = new HashMap<String, Object>();

	protected static Map<String, Object> mapLoginCountIn1Day = new HashMap<String, Object>();

	/** 只有测试代码能够调用！！ 有的测试会导致登录次数过多而引起测试失败，所以要重置相应的map */
	public static void resetLoginCount() {
		mapLoginCountIn1Day.clear();
		mapLoginDay.clear();
	}

	protected BaseAuthenticationBO getBaseAuthenticationBO() {
		throw new RuntimeException("没有提供BaseAuthenticationBO");
	}

	protected RSAInfo generateRSA(String id) throws Exception {
		return getBaseAuthenticationBO().generateRSA(id);
	}

	public BaseAuthenticationModel authenticate(String dbName, int iUseCaseID, BaseModel bmIn, String sEncrytedPassword, ErrorInfo ec) throws Exception {
		BaseAuthenticationModel bam = getBaseAuthenticationBO().login(dbName, iUseCaseID, bmIn, sEncrytedPassword);
		ec.setErrorCode(getBaseAuthenticationBO().getLastErrorCode());
		ec.setErrorMessage(getBaseAuthenticationBO().getLastErrorMessage());

		return bam;
	}

	@SuppressWarnings("unchecked")
	protected boolean checkLoginCount(Object phone, ErrorInfo ecOut) {
		boolean bLoginToManyTimes = false; // 检查Staff是否登录次数过多
		lock.writeLock().lock();
		try {
			BxConfigGeneral cgMaxRequestCountIn1Day = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MaxLoginCountIn1Day, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
			int MAX_LoginCountIn1Day = Integer.valueOf(cgMaxRequestCountIn1Day.getValue());
			// MAX_LoginCountIn1Day = 5;
			String dateForDay = sdfLoginCountIn1Day.format(new Date());
			if (mapLoginDay.get(dateForDay) == null) {
				mapLoginDay.clear();
				mapLoginCountIn1Day.put(String.valueOf(phone), 1);
				mapLoginDay.put(dateForDay, mapLoginCountIn1Day);
			} else {
				mapLoginCountIn1Day = (Map<String, Object>) mapLoginDay.get(dateForDay);
				if (mapLoginCountIn1Day.get(phone) == null) {
					mapLoginCountIn1Day.put(String.valueOf(phone), 1);
				} else {
					int currentLoginCountIn1Day = (int) mapLoginCountIn1Day.get(phone);
					mapLoginCountIn1Day.put(String.valueOf(phone), ++currentLoginCountIn1Day);
					if (currentLoginCountIn1Day >= MAX_LoginCountIn1Day) {
						if (currentLoginCountIn1Day % MAX_LoginCountIn1Day == 0) {
							logger.error("手机号码" + phone + "当天已超过登录次数，已登录" + currentLoginCountIn1Day + "次");
						}
						bLoginToManyTimes = true;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		lock.writeLock().unlock();
		//
		if (bLoginToManyTimes) {
			return false;// 从此禁止用户再次登录。那么，用户什么时候可以再次登录？1、重启Tomcat。2、给OP开发一个接口。（后面再做）
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	protected void updateLoginCount(Object phone) {
		lock.writeLock().lock();
		//
		try {
			String dateForDay = sdfLoginCountIn1Day.format(new Date());
			if (mapLoginDay.get(dateForDay) != null) {
				mapLoginCountIn1Day = (Map<String, Object>) mapLoginDay.get(dateForDay);
				if (mapLoginCountIn1Day.get(phone) != null) {
					int currentLoginCountIn1Day = (int) mapLoginCountIn1Day.get(phone);
					mapLoginCountIn1Day.put(String.valueOf(phone), --currentLoginCountIn1Day);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		//
		lock.writeLock().unlock();
	}
}
