package com.bx.erp.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumEnv;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.report.RetailTradeDailyReportBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.report.RetailTradeDailyReport;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.model.wx.WxAccessToken;
import com.bx.erp.model.wx.WxTemplate;
import com.bx.erp.model.wx.WxTemplateData;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.WxUtils;

import net.sf.json.JSONObject;

@Component("retailTradeDailyReportSummaryTaskThread")
@Scope("prototype")
public class RetailTradeDailyReportSummaryTaskThread extends TaskThread {
	private Log logger = LogFactory.getLog(RetailTradeDailyReportSummaryTaskThread.class);

	@Value("${wx.templateid.DailyReportSummary}")
	private String WXMSG_TEMPLATEID_DailyReportSummary;

	// private static final String GET_TOKEN_URL =
	// "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET&connect_redirect=1";

	@Resource
	private RetailTradeDailyReportBO retailTradeDailyReportBO;

	/**
	 * 为了使测试代码能够在任何时间驱动夜间任务，而不必真的等待到夜间才开始夜间任务，特设置本变量。 默认值为null。 <br />
	 * 1、值为null时，生成当前日期前一天的报表。这是用于功能代码的值，功能代码不能设置其它值！ <br />
	 * 2、为其它值时，生成reportDateForTest-1天的报表
	 */
	public static Date reportDateForTest;

	/** 将此值设置为true，夜间任务将无条件执行。运行一次后，如果不想再运行，可以设置为false */
	public static boolean runOnce;

	public Date getReportDateForTest() {
		return reportDateForTest;
	}

	public RetailTradeDailyReportSummaryTaskThread() {
		reportDateForTest = null;
		name = "推送日销售报表";
	}

	@Override
	protected Date getStartDatetime(String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral cgStart = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.RetailTradeDailyReportSummaryTaskScanStartTime, BaseBO.SYSTEM, ecOut, dbName);
		return DatetimeUtil.getNextDayWithTime(new Date(), cgStart.getValue());
	}

	@Override
	public boolean canRunOnceForTest() {
		return runOnce;
	}

	@Override
	protected void doTaskForEveryCompany(Company company) {
		// logger.debug(this.getName() + "--正在检查是否到达定时任务执行时期段（DB名称=" + dbName + "）...");
		String dbName = company.getDbName();
		logger.info("定时任务执行时间段已到，开始运行任务：查询当天" + name + "并推送 \t本线程hashcode=" + this.hashCode() + "\t 执行的dbName=" + dbName);
		RetailTradeDailyReport retailTradeDailyReport = new RetailTradeDailyReport();
		// reportDateForTest==null时获取当前时间的前一天，reportDateForTest!=null时，生成reportDateForTest当天报表
		Date targetDate = (reportDateForTest == null ? DatetimeUtil.getDays(new Date(), -1) : DatetimeUtil.getDays(reportDateForTest, -1));
		// 获取所有门店信息，循环生成所有门店的日报表
		List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店
		for(BaseModel bm : shopList) {
			Shop shop  = (Shop) bm;
			retailTradeDailyReport.setShopID(shop.getID());
			retailTradeDailyReport.setSaleDatetime(targetDate);
			retailTradeDailyReport.setDeleteOldData(deleteReportOfDateRun);
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> bmList = retailTradeDailyReportBO.createObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, retailTradeDailyReport);
			if (retailTradeDailyReportBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
				// TaskCP.verifyRetailTradeDailyReportSummaryTask(bmList, dbName);
				if(bmList.size() == 0) {
					continue;
				} else if (!generateWxMessage(bmList, company)) {
					return;
				}
			} else {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("夜间检查任务【推送日销售报表】创建日销售报表异常：" + retailTradeDailyReportBO.printErrorInfo(dbName, retailTradeDailyReport));
				}
				return;
			}
		}
	}

	/** 根据微信公众号模板1获取发送数据 */
	private boolean generateWxMessage(List<List<BaseModel>> bmList, Company company) {
		ErrorInfo ei = new ErrorInfo();
		List<Staff> staffList = hasAtLeast1BossBindWx(ei, company.getDbName(), name);

		if (ei.getErrorCode() != EnumErrorCode.EC_NoError) {
			return false;
		} else if (staffList == null) {
			logger.info("夜间检查任务【" + name + "】创建消息时失败；门店老板未绑定openid!");
			return false;
		} else {
			if (bmList != null && bmList.size() != 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);
				for (Staff staffBoss : staffList) {
					if (staffBoss.getOpenid() != null && !"".equals(staffBoss.getOpenid())) {
						String tmpurl = null;
						WxTemplateData templateData = new WxTemplateData();
						Map<String, Object> params = templateData.getTemplateData1(bmList); // 根据模板1获取发送数据
						//
						RetailTradeDailyReportSummary reportSummary = (RetailTradeDailyReportSummary) bmList.get(1).get(0);
						params.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("你的商户营业额已出：", WxTemplateData.backgroundColor));
						params.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(sdf.format(reportSummary.getDateTime()), WxTemplateData.backgroundColor));
						params.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(reportSummary.getTotalNO() + " 笔", WxTemplateData.backgroundColor));
						params.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(GeneralUtil.formatToShow(reportSummary.getTotalAmount()) + " 元", WxTemplateData.backgroundColor));
						params.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("点击查看详情", WxTemplateData.backgroundColor));  // TODO 详情这还需要KIM他们确定
						JSONObject jsData = WxUtils.toJsonObject(params);

						JSONObject json = new JSONObject();
						// WxTemplate template = new WxTemplate();
						json.put(WxTemplate.field.getFIELD_NAME_touser(), staffBoss.getOpenid());
						json.put(WxTemplate.field.getFIELD_NAME_template_id(), WXMSG_TEMPLATEID_DailyReportSummary); // 模板id
						json.put(WxTemplate.field.getFIELD_NAME_url(), BaseWxModel.WX_URL_1);
						json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
						json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());

						WxAccessToken token = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
						if (token == null) {
							if (BaseAction.ENV != EnumEnv.DEV) {
								logger.error("无法得到微信的token");
							}
							return false;
						}

						tmpurl = String.format(GET_SENDTEMPLATEMSG_URL, token.getAccessToken());
						logger.debug("tmpurl=" + tmpurl + "\ttoken.getAccessToken()=" + token.getAccessToken());
						JSONObject result = WxUtils.postToWxServer(tmpurl, json.toString());
						logger.info(result);
						int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
						logger.info("====" + errcode);
						String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
						logger.info(errmsg);
						if (errcode == 0) {
							logger.info("夜间检查任务【" + name + "】微信推送消息发送成功！！！");
						} else {
							logger.error("夜间检查任务【" + name + "】微信推送消息发送失败！！！错误信息：" + errmsg + "---错误码：" + errcode);
							return false;
						}
					}
				}
			} else {
				logger.info("无营业记录,不推送微信消息");
			}
		}

		return true;
	}
}
