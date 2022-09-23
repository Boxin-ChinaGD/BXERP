package com.bx.erp.action.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 延迟拦截器。项目启动后，先运行了拦截器，再从配置文件中加载数据，后者无法被拦截。本类专门为了延迟拦截而设
 */
@Component
public class LazyInterceptor {
	/**
	 * 微信官方授权页面的验证txt文件
	 */
	public static String fileName_PublicAccountAuthorityTxt;
   
	/**
	 * 微信第三方平台验证的txt文件
	 */
//	public static String fileName_ThirdPartyTxt;
	
	@Value("${public.account.txt}")
	public void setPublicAccountAuthorityTxt(String txt) {
		LazyInterceptor.fileName_PublicAccountAuthorityTxt = txt;
	}

	public String getPublicAccountAuthorityTxt() {
		return fileName_PublicAccountAuthorityTxt;
	}

	
//	public String getFileName_ThirdPartyTxt() {
//		return fileName_ThirdPartyTxt;
//	}
//	@Value("${third.party.txt}")
//	public void setFileName_ThirdPartyTxt(String fileName_ThirdPartyTxt) {
//		LazyInterceptor.fileName_ThirdPartyTxt = fileName_ThirdPartyTxt;
//	}
}
