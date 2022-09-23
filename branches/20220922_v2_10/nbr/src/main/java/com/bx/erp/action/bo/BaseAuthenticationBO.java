package com.bx.erp.action.bo;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseAuthenticationModel;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RSAInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.MD5Util;
import com.bx.erp.util.RSAUtils;

@Component("baseAuthenticationBO")
public abstract class BaseAuthenticationBO extends BaseBO {
	private Log logger = LogFactory.getLog(BaseAuthenticationBO.class);

	private final static HashMap<String, HashMap<String, Object>> mapRSA = new HashMap<String, HashMap<String, Object>>();

	/**
	 * @param key
	 *            登录者的ID，可能是POS ID，也可能是其他人的ID，也可能是手机等其他的字段
	 * @param sPasswordEncrypted
	 *            公钥加密后的密码
	 * @return 真正的密码
	 */

	/**
	 * 代表查询的对象要返回salt
	 */
	public static final int RETURN_SALT = 1;

	public String decrypt(String key, String sPasswordEncrypted) {
		lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
		lastErrorMessage = "";
		HashMap<String, Object> map = mapRSA.get(key);
		if (map == null) {
			return null;
		}
		RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
		//
		String pwd = "";
		try {
			pwd = RSAUtils.decryptByPrivateKey(sPasswordEncrypted, privateKey);
			if (!FieldFormat.checkRawPassword(pwd)) {
				lastErrorCode = ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField;
				lastErrorMessage = FieldFormat.FIELD_ERROR_Password;
				logger.info(FieldFormat.FIELD_ERROR_Password);
				return null;
			}
			// logger.info("String decrypted=" + pwd);
		} catch (Exception e) {
			logger.info(e);
			lastErrorCode = EnumErrorCode.EC_Hack;
			return null;
		}

		return pwd;
	}

	// int2代表查询的对象是否要返回salt
	public BaseAuthenticationModel login(String dbName, int iUseCaseID, BaseModel bmIn, String sPasswordEncrypted) {
		bmIn.setReturnSalt(RETURN_SALT);
		DataSourceContextHolder.setDbName(dbName);
		BaseModel bm = retrieve1Object(BaseBO.SYSTEM, iUseCaseID, bmIn);
		if (getLastErrorCode() != EnumErrorCode.EC_NoError || bm == null) {
			lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoSuchData;

			return null;
		}

		if (!isActiveObject(bm)) {
			return null;
		}

		String pwd = decrypt(((BaseAuthenticationModel) bmIn).getKey(), sPasswordEncrypted);
		if (pwd == null) {
			return null;
		}

		String md5 = MD5Util.MD5(pwd + BaseAction.SHADOW);
		if (md5 == null) {
			lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
			return null;
		}
		if (md5.equals(((BaseAuthenticationModel) bm).getSalt())) {
			return (BaseAuthenticationModel) bm;
		} else {
			lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoSuchData;
		}

		return null;
	}

	/**
	 * 检查对象的状态。如果不是活动状态，则登录失败，设置对应的错误码
	 */
	protected boolean isActiveObject(BaseModel bm) {
		throw new RuntimeException("Not yet implemented!");
	}

	public RSAInfo generateRSA(String id) throws Exception {
		HashMap<String, Object> map = RSAUtils.getKeys();
		RSAPublicKey publicKey = (RSAPublicKey) map.get("public");

		mapRSA.put(id, map);

		String modulus = publicKey.getModulus().toString(16);
		String exponent = publicKey.getPublicExponent().toString(16);

		RSAInfo rsa = new RSAInfo();
		rsa.setExponent(exponent);
		rsa.setModulus(modulus);

		return rsa;
	}

}
