package com.bx.erp.action.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

import com.bx.erp.model.BxStaff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Vip;
import com.bx.erp.model.config.BxConfigGeneral;

public class StaffLoginInterceptor implements Filter {
	private Log logger = LogFactory.getLog(StaffLoginInterceptor.class);

	public static final LoginGuard loginGuard = new LoginGuard();

	private static List<String> pattenURL = new ArrayList<String>();

	private SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1, Locale.ENGLISH);
	private SimpleDateFormat sdf2 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default5, Locale.ENGLISH);
	private Map<String, Object> mapRequestCountIn1Day = new HashMap<String, Object>();
	private Map<String, Object> mapRequestCountIn1Min = new HashMap<String, Object>();
	/** 一天中请求服务器的次数。从配置表中加载，不可改变 */
	private int MAX_RequestCountIn1Day;
	/** 一分钟中请求服务器的次数。从配置表中加载，不可改变 */
	private int MAX_RequestCountIn1Min;
	/** 请求服务器的次数超过最大值后，log4j会发警报邮件。请求次数继续增长，每次超过一定的增量后，会重复发警报邮件。设计这个业务逻辑可以避免过多的警报邮件。
	 * 比如，MaxRequestCountIn1Min设置为600，增量为600X0.1=60。当1分钟内的访问次数超过600时，会发送警报邮件。超过660时，再发一次。超过720时，再发一次。 */
	private double MAX_RequestIncrementPercent; // 超过阀值后的增量比例。从配置表中加载，不可改变
	/** 一天中超过请求服务器的次数超过最大值后,会根据MAX_RequestIncrementPercent的比例，增量一天中服务器请求的最大次数
	 * currentRequestIncrementCountIn1Day +=currentRequestIncrementCountIn1Min *
	 * MAX_RequestIncrementPercent */
	private int currentRequestIncrementCountIn1Day;
	/** 一分钟内超过请求服务器的次数超过最大值后,会根据MAX_RequestIncrementPercent的比例，增量一分钟内服务器请求的最大次数
	 * currentRequestIncrementCountIn1Min += currentRequestIncrementCountIn1Min *
	 * MAX_RequestCountIn1Min */
	private int currentRequestIncrementCountIn1Min;
	/** 当天请求数 */
	private int currentRequestCountIn1Day;
	/** 当前分钟内的请求数 */
	private int currentRequestCountIn1Min;
	/** 天数Map集合最大元素个数 */
	private final int MAX_MAP_SIZE_IN1DAY = 10;
	/** 分钟数Map集合最大元素个数 */
	private final int MAX_MAP_SIZE_IN1MIN = 60;

	/*
	 * 在pattenURL中的全部不拦截，使用：path.indexOf(urlStr) > -1 判断路径是否存在
	 */

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		pattenURL.add("/staff/getTokenEx.bx");// getToken请求
		pattenURL.add("/staff/loginEx.bx");// 登录请求
		pattenURL.add("/bxStaff/getTokenEx.bx");// getToken请求
		pattenURL.add("/bxStaff/loginEx.bx");// 登录请求
		pattenURL.add("/home/adminLogin.bx");// staff登录.jsp
		pattenURL.add("/home/bxAdminLogin.bx");// bxstaff登陆.jsp
		pattenURL.add("/wx");
		pattenURL.add("/wx/*");
		// pattenURL.add("/wx/getSessionKey");
		pattenURL.add("/pos/getTokenEx.bx");// getToken请求
		pattenURL.add("/pos/loginEx.bx");// 登录请求
		pattenURL.add("/pos/retrieve1BySNEx.bx");// BX交付POS后，用于POS获取身份

		pattenURL.add("/wx3rdPartyAuthorization/*");
		pattenURL.add("/wx3rdPartyCard/*");
		// pattenURL.add("/shop/retrieveNEx.bx");

		// 静态资源
		pattenURL.add("css");
		pattenURL.add("images");
		pattenURL.add("scripts");
		pattenURL.add("BXUI");
		pattenURL.add("/bx.ico");

		// 小程序调用的请求
		// pattenURL.add("/wx/getSessionKeyEX.bx");
		// pattenURL.add("/wx/getOpenIdEX.bx");
		// pattenURL.add("/vip/getPhoneEx.bx");
		pattenURL.add("/vip/loginEx.bx");
		pattenURL.add("/miniprogram/decryptPhoneEx.bx");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		checkSecurity();

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		String loginUrl = null;

		String basePath = httpRequest.getContextPath();
		// 登陆url
		String staffLoginUrl = basePath + "/home/adminLogin.bx";
		String bxstaffLoginUrl = basePath + "/home/bxAdminLogin.bx";
		String vipLoginUrl = basePath + "/miniprogram/loginEx.bx";
		// 首次登陆修改密码的界面
		String staffLoginUrl2 = basePath + "/home/updateMyPwd.bx";
		// 已经登陆且不拦截的页面（首次修改密码）
		String staffLoginUrl3 = basePath + "/staff/resetMyPasswordEx.bx";
		String url = httpRequest.getRequestURI().toString();

		StringBuilder sb = new StringBuilder();
		logger.info(sb.append(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date())) //
				.append("\t\t开始运行函数/请求文件：") //
				.append(url).append("()...").toString()); //

		/** 根据staff或bxstaff拦截跳转URL */
		if (url.equals(staffLoginUrl)) {
			loginUrl = staffLoginUrl;
		} else if (url.equals("/home/company.bx") || url.equals("/shop.bx")) {
			loginUrl = bxstaffLoginUrl;
		} else if (url.equals("/miniprogram/loginEx.bx")) { // vip登录的惟一入口是/vip/loginEx.bx，如果没有请求此URL登录，则只能让其跳到老板登录的入口/home/adminLogin.bx
			loginUrl = vipLoginUrl;
		} else {
			loginUrl = "/home/adminLogin.bx";
		}

		// 微信官方授权页面的验证txt文件加入拦截器白名单
		String fileName_PublicAccountAuthorityTxt = new LazyInterceptor().getPublicAccountAuthorityTxt();
		if (!pattenURL.contains(fileName_PublicAccountAuthorityTxt)) {
			pattenURL.add(fileName_PublicAccountAuthorityTxt);
		}

		// 微信第三方平台的验证txt文件加入拦截器白名单
		// String fileName_ThirdPartyTxt = new
		// LazyInterceptor().getFileName_ThirdPartyTxt();
		// if (!pattenURL.contains(fileName_ThirdPartyTxt)) {
		// pattenURL.add(fileName_ThirdPartyTxt);
		// }

		/*
		 * 注：在pattenURL中的全部不拦截 url.indexOf(urlStr) > -1 表示urlStr在url中出现过，出现就不拦截
		 */
		for (String urlStr : pattenURL) {
			if (url.indexOf(urlStr) > -1) {
				chain.doFilter(request, response);
				return;
			}
		}

		/*
		 * 超时处理，ajax请求超时设置超时状态，页面请求超时则返回提示并重定向 session.getAttribute("")是获取到登录人的session信息
		 */
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.toString());
		BxStaff bxstaff = (BxStaff) session.getAttribute(EnumSession.SESSION_BxStaff.toString());
		Vip vip = (Vip) session.getAttribute(EnumSession.SESSION_Vip.toString());

		if (staff == null && bxstaff == null && vip == null) {
			handleAnyOneLoginWithoutSession(request, response, httpRequest, httpResponse, session, loginUrl, staffLoginUrl, bxstaffLoginUrl, vipLoginUrl);
		} else if (staff != null && bxstaff == null && vip == null) {
			if (!handleStaffRequestWithSession(request, response, chain, httpRequest, httpResponse, session, loginUrl, staffLoginUrl2, staffLoginUrl3, url, staff)) {
				return;
			}
		} else if (bxstaff != null && staff == null && vip == null) {// OP多点登录是允许的
		} else if (vip != null && bxstaff == null && staff == null) { // 处理vip的请求
		} else {
			logger.error("非法的会话状态：vip=" + vip + "\r\nstaff=" + staff + "\r\nbxStaff=" + bxstaff);
			return;
		}
		chain.doFilter(request, response);
	}

	private boolean handleStaffRequestWithSession(ServletRequest request, ServletResponse response, FilterChain chain, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession session, String loginUrl,
			String staffLoginUrl2, String staffLoginUrl3, String url, Staff staff) throws ServletException, IOException {
		if (staff.getIsFirstTimeLogin() == EnumBoolean.EB_Yes.getIndex() && !(url.indexOf(staffLoginUrl3) > -1) && staff.getIsLoginFromPos() == 0) { // isLoginFromPos的值为1表示staff是从pos端进行登录的
			// 首次登陆
			logger.info("拦截到到用户=" + staff.getName() + "首次登陆系统！跳转至 " + staffLoginUrl2 + "进行修改密码!");
			httpRequest.getRequestDispatcher(staffLoginUrl2).forward(request, response);
		} else {
			// 网页端或POS机单点登录检测
			int posID = BaseAction.INVALID_POS_ID;
			Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
			if (pos != null) {
				posID = pos.getID();
			}
			// 获取公司SN
			Company company = (Company) session.getAttribute(EnumSession.SESSION_Company.getName());
			if (company == null || StringUtils.isEmpty(company.getSN())) {
				logger.error("黑客行为：company的session不存在！");
				return false;
			}
			if (loginGuard.needToIntercept(company.getSN(), staff.getID(), posID, session)) {
				String str = null;
				if (posID == BaseAction.INVALID_POS_ID) { // 这是网页端上的操作
					// 判断是否为ajax请求
					if (httpRequest.getHeader("x-requested-with") != null) {
						httpResponse.addHeader("staffLoginUrl", loginUrl);// 返回url
						httpResponse.addHeader("sessionStatus", "duplicatedSession"); // 返回单点登录标识
						str = "<script language='window.top.location.href='" + loginUrl + "';</script>";// ......
					} else {
						str = "<script language='javascript'>alert('你已经在其它地方登录'); window.top.location.href='" + loginUrl + "';</script>";
					}
				} else { // 这是POS端上的操作
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_DuplicatedSession.toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "你已经在其它地方登录");
					str = JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				}
				response.setContentType("text/html;charset=UTF-8");
				try {
					PrintWriter writer = response.getWriter();
					writer.write(str);
					writer.flush();
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * 人类登录走进本函数。但Pos登录后，人类未登录，仍然跑进本函数
	 */
	private void handleAnyOneLoginWithoutSession(ServletRequest request, ServletResponse response, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession session, String loginUrl, String staffLoginUrl,
			String bxstaffLoginUrl, String vipLoginUrl) throws ServletException, IOException {
		logger.info("拦截到的URL=" + httpRequest.getRequestURL() + "	Cookie=" + httpRequest.getCookies() + "		SessionID=" + session.getId());
		// 判断是否为ajax请求
		if (httpRequest.getHeader("x-requested-with") != null && httpRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
			httpResponse.addHeader("sessionStatus", "timeOut"); // 返回超时标识
			logger.info("url=" + loginUrl);
			// 判断当前登录的是哪个页面,跳转到相应的页面
			if (loginUrl.equals(staffLoginUrl)) {
				httpResponse.addHeader("staffLoginUrl", loginUrl);// 返回url
				httpRequest.getRequestDispatcher(staffLoginUrl).forward(request, response);
			} else if (loginUrl.equals(bxstaffLoginUrl)) {
				httpResponse.addHeader("bxstaffLoginUrl", bxstaffLoginUrl);// 返回url
				httpRequest.getRequestDispatcher(bxstaffLoginUrl).forward(request, response);
			} else if (loginUrl.equals(vipLoginUrl)) {
				httpResponse.addHeader("vipLoginUrl", vipLoginUrl);// 返回url
				httpRequest.getRequestDispatcher(vipLoginUrl).forward(request, response);
			}

			// chain.doFilter(request, response);// 不可少，否则请求会出错
		} else { // 非ajax请求，让其直接跳转到登录页面
			String str = "";
			if (session.getAttribute(EnumSession.SESSION_POS.toString()) == null) {
				str = "<script language='javascript'>" + "window.top.location.href='" + loginUrl + "';</script>";
				httpResponse.addHeader("sessionStatus", "timeOut"); // 返回超时标识
			}
			response.setContentType("text/html;charset=UTF-8");
			try {
				PrintWriter writer = response.getWriter();
				writer.write(str);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 做安全审计。如果出现DDOS类型的攻击，可以通过log4j.log error收到邮件警报 */
	private void checkSecurity() {
		// 获取Action访问次数阀值
		if (MAX_RequestCountIn1Day == 0 || MAX_RequestCountIn1Min == 0 || MAX_RequestIncrementPercent == 0) {
			setRequestCount();
		}
		// 统计一天的Action访问次数
		String dateForDay = sdf1.format(new Date());
		String dateForMin = sdf2.format(new Date());
		if (mapRequestCountIn1Day.get(dateForDay) == null) {
			// 判断Map元素是否到达上限，如果到达上限，则遍历出来所有的元素并打印出来，然后删除所有元素
			if (mapRequestCountIn1Day.size() == MAX_MAP_SIZE_IN1DAY) {
				Iterator<Entry<String, Object>> iter = mapRequestCountIn1Day.entrySet().iterator();
				Entry<String, Object> entry = null;
				while (iter.hasNext()) {
					entry = iter.next();
					logger.info(entry.getKey() + " 访问Action次数:" + entry.getValue());
				}
				mapRequestCountIn1Day.clear();
			}
			mapRequestCountIn1Day.put(sdf1.format(new Date()), 1/* 当前访问算1次 */);
			// 初始化增量
			currentRequestIncrementCountIn1Day = 0;
		} else {
			currentRequestCountIn1Day = (int) mapRequestCountIn1Day.get(dateForDay);
			mapRequestCountIn1Day.put(dateForDay, ++currentRequestCountIn1Day);
			// 如果访问次数大于阀值，则发出警告
			if (currentRequestCountIn1Day > (MAX_RequestCountIn1Day + currentRequestIncrementCountIn1Day)) {
				logger.error("警告！当前时间 " + dateForDay + " 已超过每天请求服务器次数的最大值+增量，值=" + (MAX_RequestCountIn1Day + currentRequestIncrementCountIn1Day));
				currentRequestIncrementCountIn1Day += MAX_RequestCountIn1Day * MAX_RequestIncrementPercent;
			}
		}
		// 统计一分钟的Action访问次数
		if (mapRequestCountIn1Min.get(dateForMin) == null) {
			mapRequestCountIn1Min.put(dateForMin, 1);
			// 判断Map元素是否到达上限，如果到达上限，删除所有元素
			if (mapRequestCountIn1Min.size() > MAX_MAP_SIZE_IN1MIN) {
				mapRequestCountIn1Min.clear();
			}
			// 初始化增量
			currentRequestIncrementCountIn1Min = 0;
		} else {
			currentRequestCountIn1Min = (int) mapRequestCountIn1Min.get(dateForMin);
			mapRequestCountIn1Min.put(dateForMin, ++currentRequestCountIn1Min);
			// 如果访问次数大于阀值，则发出警告
			if (currentRequestCountIn1Min > (MAX_RequestCountIn1Min + currentRequestIncrementCountIn1Min)) {
				logger.error("警告！当前时间 " + dateForMin + " 已超过每分钟请求服务器次数的最大值+增量，值=" + (MAX_RequestCountIn1Min + currentRequestIncrementCountIn1Min));
				currentRequestIncrementCountIn1Min += MAX_RequestCountIn1Min * MAX_RequestIncrementPercent;
			}
		}
	}

	private void setRequestCount() {
		ErrorInfo ecOut = new ErrorInfo();
		BxConfigGeneral cgMaxRequestCountIn1Day = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MaxRequestCountIn1Day, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
		BxConfigGeneral cgMaxRequestCountIn1Min = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MaxRequestCountIn1Min, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);
		BxConfigGeneral cgMaxRequestIncrementPercent = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MaxRequestIncrementPercent, BaseBO.SYSTEM, ecOut,
				BaseAction.DBName_Public);
		MAX_RequestCountIn1Day = Integer.valueOf(cgMaxRequestCountIn1Day.getValue());
		MAX_RequestCountIn1Min = Integer.valueOf(cgMaxRequestCountIn1Min.getValue());
		MAX_RequestIncrementPercent = Double.valueOf(cgMaxRequestIncrementPercent.getValue());
	}

}
