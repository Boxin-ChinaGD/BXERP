package com.bx.erp.action.bo;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RSAInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.RSAUtils;

@Component("baseUserBO")
@Scope("prototype")
public class BaseUserBO extends BaseBO {
	private Log logger = LogFactory.getLog(BaseUserBO.class);

	private static final HashMap<String, HashMap<String, Object>> mapRSA = new HashMap<String, HashMap<String, Object>>();

	@Override
	public void setMapper() {
		throw new RuntimeException("尚未实现的方法setMapper()");
	}

	protected String decrypt(String key, String sPasswordEncrypted) {
		HashMap<String, Object> map = mapRSA.get(key);
		if (map == null) {
			return null;
		}
		RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
		//
		String pwd = "";
		try {
			pwd = RSAUtils.decryptByPrivateKey(sPasswordEncrypted, privateKey);
			logger.info("String decrypted=" + pwd);
		} catch (Exception e) {
			return null;
		}

		return pwd;
	}

	public BaseModel login(String key, String sPasswordEncrypted) {
		String pwd = decrypt(key, sPasswordEncrypted);
		if (pwd == null) {
			lastErrorCode = EnumErrorCode.EC_Hack;
			return null;
		}
		return searchForLogin(key, pwd);
	}

	protected BaseModel searchForLogin(String key, String pwd) {
		return null;
	}

	protected RSAInfo generateRSA(String mobile) throws Exception {
		HashMap<String, Object> map = RSAUtils.getKeys();
		RSAPublicKey publicKey = (RSAPublicKey) map.get("public");

		mapRSA.put(mobile, map);

		String modulus = publicKey.getModulus().toString(16);
		String exponent = publicKey.getPublicExponent().toString(16);
		RSAInfo rsa = new RSAInfo();
		rsa.setExponent(exponent);
		rsa.setModulus(modulus);

		return rsa;
	}

	public BaseModel resetPassword(String key, String sPasswordEncryptedOld, String sPasswordEncryptedNew) {
		String pwdOld = decrypt(key, sPasswordEncryptedOld);
		if (pwdOld == null) {
			lastErrorCode = EnumErrorCode.EC_Hack;
			return null;
		}
		String pwdNew = decrypt(key, sPasswordEncryptedNew);
		if (pwdNew == null) {
			lastErrorCode = EnumErrorCode.EC_Hack;
			return null;
		}

		return doResetPassword(key, pwdOld, pwdNew);
	}

	protected BaseModel doResetPassword(String key, String pwdOld, String pwdNew) {
		return null;
	}
}
