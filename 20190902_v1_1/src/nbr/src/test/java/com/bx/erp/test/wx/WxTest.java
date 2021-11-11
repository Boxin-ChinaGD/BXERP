package com.bx.erp.test.wx;

import static org.testng.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumEnv;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.model.wx.Button;
import com.bx.erp.model.wx.WxAccessToken;
import com.bx.erp.model.wx.WxCustomMenu;
import com.bx.erp.model.wx.WxTemplate;
import com.bx.erp.model.wx.WxTemplateData;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.WxUtils;

import net.sf.json.JSONObject;

//做微信官方接口测试
public class WxTest extends BaseActionTest {

	@Value("${public.account.appid}")
	private String PUBLIC_ACCOUNT_APPID;

	@Value("${public.account.secret}")
	private String PUBLIC_ACCOUNT_SECRET;

	@Value("${get.accesstoken.url}")
	private String GET_ACCESSTOKEN_URL; // 获取访问微信端的token接口

	@Value("${get.wxip.url}")
	private String GET_WXIP_URL; // 获取微信IP地址接口

	@Value("${get.wxping.url}")
	private String GET_WXPING_URL; // ping微信IP地址的接口

	@Value("${get.code.url}")
	private String GET_CODE_URL; // 获取code的接口

	@Value("${get.user.attention.url}")
	private String GET_USER_ATTENTION_URL;// 获取到用户是否关注公众号的接口

	@Value("${get.templateMsgList.url}")
	private String GET_TEMPLATEMSGLIST_URL; // 获取模板列表的接口

	@Value("${get.templateMsgID.url}")
	private String GET_TEMPLATEMSGID_URL; // 获取模板ID的接口

	@Value("${get.sendTemplateMsg.url}")
	private String GET_SENDTEMPLATEMSG_URL; // 发送模板消息的接口

	@Value("${get.industry.type.url}")
	private String GRT_INDUSTRY_TYPE_URL; // 获取行业信息接口

	@Value("${get.userlist.url}")
	private String GET_USERLIST_URL; // 获取用户列表(openid)一次拉取调用最多拉取10000个关注者的OpenID (GET方式)

	@Value("${get.user.url}")
	private String GET_USER_URL; // 获取用户基本信息的接口

	@Value("${get.qrcode.url}")
	private String GET_QRCODE_URL; // #获取到二维码的ticket

	@Value("${get.showqrcode.url}")
	private String GET_SHOWQRCODE_URL; // 获取二维码ticket,获取二维码 GET方式

	@Value("${public.account.token}")
	private String TOKEN;

	@Value("${public.account.encodingAESKey}")
	private String EncodingAESKey;

	@Value("${wx.templateid.followsuccess}")
	private String FOLLOWSUCCESS;

	@Value("${wx.templateid.bindSuccess}")
	private String WXMSG_TEMPLATEID_BINDSUCCESS;

	@Value("${wx.templateid.LoginSuccess}")
	private String WSMSG_TEMPLATEID_LoginSuccess;

	@Value("${wx.templateid.LogoutSuccess}")
	private String WXMSG_TEMPLATEID_LogoutSuccess;

	@Value("${wx.templateid.warehousingDiff}")
	private String WXMSG_TEMPLATEID_warehousingDiff;

	@Value("${wx.templateid.successCreatePurchasingOrder}")
	private String WXMSG_TEMPLATEID_createPurchasingOrder;

	@Value("${wx.templateid.unsalableCommodity}")
	private String WXMSG_TEMPLATEID_UnsalableCommodity;

	@Value("${wx.templateid.purchasingOrderOverTime}")
	private String WXMSG_TEMPLATEID_purchasingOrderOverTime;

	@Value("${wx.templateid.inventorySheetDiff}")
	private String WXMSG_TEMPLATEID_inventorySheetDiff;

	@Value("${wx.templateid.DailyReportSummary}")
	private String WXMSG_TEMPLATEID_DailyReportSummary;

	// 获取微信的IP
	@Test
	public void testGetIP() {
		System.out.println(PUBLIC_ACCOUNT_SECRET);
		WxAccessToken token = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(token != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		String url = String.format(GET_WXIP_URL, token.getAccessToken());
		JSONObject jsonObject = WxUtils.getDataFromWxServer(url);
		System.out.println(jsonObject);

	}

	// 获取token
	@Test
	public void testToken() throws InterruptedException {
		System.out.println("appid =====" + PUBLIC_ACCOUNT_APPID + "====PUBLIC_ACCOUNT_SECRET====" + PUBLIC_ACCOUNT_SECRET + "=====" + TOKEN);

		System.out.println("====================== 正常获取到accesstoken ======================");
		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		System.out.println(accessToken.getAccessToken());
		System.out.println(accessToken.getExpiresin());

		Thread.sleep(5000);/// ...
		System.out.println("====================== 在accesstoken的有效期内，查看是否为相同的accesstoken ======================");
		WxAccessToken accessToken2 = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		System.out.println(accessToken2.getAccessToken());
		System.out.println(accessToken2.getExpiresin());

		// 验证accesstoken是否一致；预期应该是一致
		assertEquals(accessToken.getAccessToken(), accessToken2.getAccessToken());

		/**
		 * 由于该case的判断时间是在两小时失效后判断，而该值在放进缓存时已经固定无法修改，故该测试暂不使用
		 * System.out.println("======================
		 * 在accesstoken的失效后，查看是否为不相同的accesstoken ======================");
		 * accessToken.setExpiresin(1); Thread.sleep(3000);
		 * 
		 * WxAccessToken accessToken3 =
		 * WxUtils.getAccessTokenFromWxServer(PUBLIC_ACCOUNT_APPID,
		 * PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		 * System.out.println(accessToken3.getAccessToken());
		 * System.out.println(accessToken3.getExpiresin());
		 * 
		 * //验证accesstoken是否一致；预期应该是不一致 assertNotEquals(accessToken.getAccessToken(),
		 * accessToken3.getAccessToken());
		 */

	}

	// 获取code
	// @Test
	// public void getCode() {
	// // String url = String.format(GET_CODE_URL, PUBLIC_ACCOUNT_APPID,
	// // URLEncoder.encode(backUrl, "UTF-8"), "snsapi_base");
	// String newUrl = TOKEN_URL.replace("APPID",
	// PUBLIC_ACCOUNT_APPID).replace("REDIRECT_URI",
	// "http://120.77.157.129/nbr/wxBaseLogin.bx");
	// JSONObject js = WxUtils.getDataFromWxServer(newUrl);
	// System.out.println(js);
	// }

	// 判断用户是否关注公众号
	@Test
	public void testSubscribeState() {
		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		ErrorInfo.EnumErrorCode subscribeState = WxUtils.subscribeState(GET_USER_ATTENTION_URL, Shared.openid, accessToken.getAccessToken());

		switch (subscribeState) {
		case EC_NoSuchData:
			System.out.println("该用户没有关注关注号!!!");
			break;
		case EC_NoError:
			System.out.println("该用户已关注公众号!!!");
			break;
		case EC_OtherError:
		default:
			System.out.println("检查该用户是否关注公众号时发生错误!!!");
			break;
		}
	}

	// 获取模板列表
	@Test
	public void testTemplateMsgList() {

		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		JSONObject jsonObject = WxUtils.getTemplateMsgList(GET_TEMPLATEMSGLIST_URL, accessToken.getAccessToken());
		Assert.assertNotNull(jsonObject);
		System.out.println("=====" + jsonObject);
	}

	// 返回模板ID
	// @Test
	// public void testGetWXTemplateMsgId() throws ClientProtocolException,
	// IOException {
	// WxAccessToken accessToken =
	// WxUtils.getAccessTokenFromWxServer(PUBLIC_ACCOUNT_APPID,
	// PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
	// Assert.assertNotNull(accessToken);
	//
	// String url = String.format(GET_TEMPLATEMSGID_URL,
	// accessToken.getAccessToken()) ;
	//
	// JSONObject jsonObject = WxUtils.postToWxServer(url, "OPENTM402203820");
	// System.out.println(jsonObject + "====" + jsonObject.get("template_id"));
	// }

	// 发送模板信息
	@Test
	public void testSendTemplateMsg() {

		Date now = new Date();
		SimpleDateFormat f = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		//
		Map<String, Object> param = new HashMap<>();
		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("恭喜你中奖啦!!!", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData("博昕IT超级锦鲤", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData(f.format(now).toString(), WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("博昕", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("祝生意兴隆，生活愉快！ 你可知你且迷这风浪永远二十赶朝暮。 ​​​", WxTemplateData.backgroundColor));
		//
		JSONObject jsData = WxUtils.toJsonObject(param);

		// 发送消息
		sendMsg(FOLLOWSUCCESS, jsData);
	}

	@Test
	public void testSendUserBindMsg() {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("发送用户绑定消息");

		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		String date = sdf.format(new Date());
		Map<String, Object> templateParam = new HashMap<>();
		templateParam.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData(WxTemplate.Bind_FirstData, WxTemplateData.backgroundColor));
		templateParam.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(Shared.DBName_Test, WxTemplateData.backgroundColor));
		templateParam.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("店长", WxTemplateData.backgroundColor));
		templateParam.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(date, WxTemplateData.backgroundColor));
		templateParam.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData(WxTemplate.Bind_Remark, WxTemplateData.backgroundColor));
		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(templateParam);

		// 发送消息
		sendMsg(WXMSG_TEMPLATEID_BINDSUCCESS, jsData);
	}

	@Test
	public void testSendUserLoginMsg() {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("发送员工/老板登录POS上班消息");

		Map<String, Object> param = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("登录上班成功，登录消息如下:", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData("李白", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("******", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(sdf.format(new Date()), WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("如非本人操作，请前往博销宝商家后台网站修改密码", WxTemplateData.backgroundColor));
		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(param);

		// 发送消息
		sendMsg(WSMSG_TEMPLATEID_LoginSuccess, jsData);
	}

	@Test
	public void testSendUserLogoutMsg() {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("发送员工/老板登录POS交班消息");

		Map<String, Object> param = new HashMap<>();
		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】你好，今天你的店面已交班:", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(Shared.DBName_Test, WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("200.23 元", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("5 单", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("请与  李白   核对交接班情况。", WxTemplateData.backgroundColor));
		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(param);

		// 发送消息
		sendMsg(WXMSG_TEMPLATEID_LogoutSuccess, jsData);
	}

	@Test
	public void testSendWarehousingDiffMsg() {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("发送入库价与采购价不符消息");

		Map<String, Object> param = new HashMap<>();
		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】入库价与采购价不符：", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(Shared.DBName_Test, WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("杜甫", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("入库价与采购价不符", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("详细信息请前往博销宝商家后台网站查看 ", WxTemplateData.backgroundColor));
		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(param);

		// 发送消息
		sendMsg(WXMSG_TEMPLATEID_warehousingDiff, jsData);
	}

	@Test
	public void testSendSuccessCreatePurchasingOrderMsg() {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("发送采购订单创建成功后待审核消息");

		Map<String, Object> param = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】你好，你有新的采购订单需要审核", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData("李白", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("bx公司", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData(sdf.format(new Date()), WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword4(), new WxTemplateData("采购审核", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("请及时审批！", WxTemplateData.backgroundColor));
		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(param);

		// 发送消息
		sendMsg(WXMSG_TEMPLATEID_createPurchasingOrder, jsData);
	}

	@Test
	public void testSendUnsalableCommodityMsg() {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("发送商品滞销消息");

		Map<String, Object> param = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default_Chinese);
		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】你好，你有1款商品可能滞销", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData("默认仓库", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("商品A", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("2", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword4(), new WxTemplateData(sdf.format(new Date()).toString(), WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("点击查看详情", WxTemplateData.backgroundColor));
		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(param);

		// 发送消息
		sendMsg(WXMSG_TEMPLATEID_UnsalableCommodity, jsData);
	}

	@Test
	public void testSendPurchasingOrderOverTimeMsg() {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("发送采购订单超时消息");

		Map<String, Object> param = new HashMap<>();
		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】 CG123456789 采购超时", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData("NBR", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("23", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("123.123", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("该采购单已超过8天未完成入库，敬请关注或者登录博销宝商家后台网站处理", WxTemplateData.backgroundColor));
		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(param);

		// 发送消息
		sendMsg(WXMSG_TEMPLATEID_purchasingOrderOverTime, jsData);
	}

	@Test
	public void testSendInventorySheetDiffMsg() {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("发送盘点差异报告消息");

		Map<String, Object> param = new HashMap<>();
		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("【提醒】盘点单数量与系统库存不符，信息如下：", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData(Shared.DBName_Test, WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("李白", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("盘点数量与系统数量不符", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("详细信息请前往博销宝商家后台网站查看 ", WxTemplateData.backgroundColor));
		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(param);

		// 发送消息
		sendMsg(WXMSG_TEMPLATEID_inventorySheetDiff, jsData);
	}

	@Test
	public void testSendDailyReportSummaryMsg() {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("发送每日销售报表消息");

		Map<String, Object> param = new HashMap<>();
		param.put(WxTemplateData.field.getFIELD_NAME_first(), new WxTemplateData("你的商户营业额已出：", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword1(), new WxTemplateData("今日", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword2(), new WxTemplateData("2 笔", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_keyword3(), new WxTemplateData("10  元", WxTemplateData.backgroundColor));
		param.put(WxTemplateData.field.getFIELD_NAME_remark(), new WxTemplateData("点击查看详情", WxTemplateData.backgroundColor));
		// 2、封装详细信息模板
		JSONObject jsData = WxUtils.toJsonObject(param);
		
		// 发送消息
		sendMsg(WXMSG_TEMPLATEID_DailyReportSummary, jsData);
	}

	private void sendMsg(String templateID, JSONObject jsData) {
		JSONObject json = new JSONObject();
		json.put(WxTemplate.field.getFIELD_NAME_touser(), Shared.openid);// openid
		json.put(WxTemplate.field.getFIELD_NAME_template_id(), templateID);// 模板id
		json.put(WxTemplate.field.getFIELD_NAME_url(), "#");// 跳转页面
		json.put(WxTemplate.field.getFIELD_NAME_topcolor(), WxTemplateData.backgroundColor);
		json.put(WxTemplate.field.getFIELD_NAME_data(), jsData.toString());// 模板数据

		// 4、向微信端发送POST请求
		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		String tmpurl = String.format(GET_SENDTEMPLATEMSG_URL, accessToken.getAccessToken());
		JSONObject result = WxUtils.postToWxServer(tmpurl, json.toString());

		// 5、检查返回的结果
		int errcode = (int) result.get(BaseWxModel.WX_ERRCODE);
		String errmsg = (String) result.get(BaseWxModel.WX_ERRMSG);
		Assert.assertTrue(errcode == BaseWxModel.WX_ERRCODE_Success, errmsg);
	}

	// 获取行业信息
	@Test
	public void testGetWX() {

		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		String newurl = String.format(GRT_INDUSTRY_TYPE_URL, accessToken.getAccessToken());

		JSONObject jsonObject = WxUtils.getDataFromWxServer(newurl);
		Assert.assertNotNull(jsonObject);
		System.out.println(jsonObject);
	}

	// 获取所有用户openid,一次拉取调用最多拉取10000个关注者的OpenID
	@Test
	public void testGetAllUser() {
		// WxUserMapper mapper = (WxUserMapper)
		// applicationContext.getBean("wxUserMapper");

		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);

		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		String url = String.format(GET_USERLIST_URL, accessToken.getAccessToken(), "");
		JSONObject jsonObject = WxUtils.getDataFromWxServer(url);
		System.out.println(jsonObject);
		String jsString = (String) jsonObject.getString("data");
		System.out.println(jsString);
		JSONObject jsonObject2 = JSONObject.fromObject(jsString);
		System.out.println(jsonObject2.get("openid"));

		// String allOpenid =
		// "oOjkQt0IeyQrKl7wAuLtCv1b0U_I,oOjkQtyFMsC81Te4nvQWfzyqblHI,oOjkQt14vtQdFmZsG-KkPlL1h14I,oOjkQtw0M4MJv3IItiXjs4LeZ-cE,oOjkQt2iKxCMv_WVJtGeNNZFcIZc,oOjkQt6dqBVcOGKDpYiRRWos7GCM,oOjkQt24GJm6izYRfxlNhONSA6w0,oOjkQt6y70S9CbyPaHYOzL__Ol-c,oOjkQtxThBkD6V5_h_LL5k7g8sHg,oOjkQt_q4P0R8vl8DFDUfuaLePMo,oOjkQt_6eyPwCNgKWYwtkfcFu6I4,oOjkQt871oV_IF_ICTCbqyh24YdU,oOjkQt_fsW5-nR5So7QwR9gEd_Wo,oOjkQt2X71N67cz2r3KIMahnOlKg,oOjkQtxmeA6TzcysamrbxNpn6VLQ,oOjkQt2TPjIwlDQ4WC-h-I2o2sQ8,oOjkQt9pOpt1xpc0mbapajnSvyPs,oOjkQt8wfFNBxyjls0XWo466jKtI,oOjkQt6mjzwbB5aePYEkCb2ewp2M,oOjkQtzfAeUAXn6WUlYgpglD-HbY,oOjkQtwIqmp1hHVAF337Zwtskuak,oOjkQt-XJA6r5S4a_np0ALIJxsDo,oOjkQtzmJvxyQyRTiCCy9NwiU-ek,oOjkQt2vAix8arQQ7mfWGDayYe2E,oOjkQt8uMpfiKrA7vhOMr_n8-zS4,oOjkQtyBp-SVH-dbzVT9HFf-vIFg,oOjkQtwo5QEmG8UY372kNNmBfp2E,oOjkQt0CQy0UGu3a-Cd9KecREWL0,oOjkQt-X0KawB0JwjZU0x9AoWlR8,oOjkQtzJWxVy8NYWhKsqjgkDcuTc,oOjkQt0UCHL_d51oDe0DQkiOk33U,oOjkQt8Txr4S268bwt2-qts2FsjE,oOjkQt8OBEBIomPOe5EL8ZtDMwzQ,oOjkQt1cZgjFuhBT16ykMZx-wBmo,oOjkQt_VT_yG0hCO1kyGx4671Srg,oOjkQt1t43bzX04okOl9tHeuGAZc,oOjkQt-CqIPBr2qp1s3nA9hwjBJY,oOjkQt4t1cw7IuJs_lzEXaue6V9A,oOjkQt_wiHiIdYTPcsqeghHnvHwk,oOjkQtyN-tMe07vXY6hCR2jZlUPE,oOjkQt2CGM-B9uWazfscTo-yDAl4,oOjkQt_tY9wHrf-Poj_ChJOyrgvU,oOjkQt3eT9HKFIq9GVJpqLIEzn_Y,oOjkQt6qo2EFuyNVzP8NMccw2_ao,oOjkQt-SgRsPswoVL7EHEYELtKcw,oOjkQt3V3CsvJH_64mtjrAKYCKKc,oOjkQt-f77erW49h_pzFndoNzosI,oOjkQtxrJbRD4Wod8XoxMa7rzscM,oOjkQt-NaPWjJ8V0dDQo5bPEaQTc,oOjkQt0LBIocVrw56Dwd40mDbV_s,oOjkQt9lJFTtVrT2ZrzCwR4zTKKU,oOjkQt_1hkKWIeCNVWhOAu8zQ8S8,oOjkQt8wwFaWe-Q42UkWjp3yH5pU,oOjkQtyP_xhY-1yfOATYcZBkihjI,oOjkQt9BM_7ny1VM6d3FpaqMl0X0,oOjkQt46Vs8PhpVm6jQ2zVA91RK8,oOjkQt0NLGoj57E3b-swZdTa7mPE,oOjkQtx30vMScXrAebVcCyqKizwI,oOjkQt2drxca6QjL5ciypbfGETAw,oOjkQt3A44c-FQYs8J7zpewSbROk,oOjkQtya3tm-ulLGEQY1_BiTZRSE,oOjkQt2AG7wUXNFqusjm7F2iMZAw,oOjkQt8isIGJbynlWY90j98p4190,oOjkQt_rwBONgkkG2L3xsmOufHoA,oOjkQtz08nItHb_OTLsvEFymXVDI,oOjkQt1WyS_84AUyk3MJOz27qQfU,oOjkQt2BgJqsfu3TMsuwCdB4LSn0,oOjkQt0rbWMSWGc4a3k56RKzSlDc,oOjkQt2CzMxZ9-xCfzubz_X52_yw,oOjkQt3TEbdU7-2OBRANNcpVWpgI,oOjkQt-byarWuuvF1NCATerAUT54,oOjkQt-JWvGuYrieoCp-VDlikxdY,oOjkQt_pIm8aR78HvRu6I7Ugnwpo,oOjkQtzHvrv3ZcWtRDLXjB0CsUSA,oOjkQtwDGwfXINLbCojxHaU5vOvw,oOjkQt4GdLxfoC5P3GL0wrtXtpFI,oOjkQt9ftGlxo-TFZkTZSuKXJ3XE,oOjkQtzJtPHXf3xdSjjr7dEyisAA,oOjkQt6r_NfJ10yJYCIo031RXiC0,oOjkQt1hHbfizRIMCaO5VC-DHqVw,oOjkQt3BkkTlpIClbv2LrQizNOic,oOjkQt12UvwVgoKnFJJPyRAa4RVk,oOjkQt4tGUQkb17OIJVn6_7YDfOk,oOjkQt82HaxsJZIAgd8JeNjJyv1o,oOjkQt8DP_cOZ-pK2D-YAeqyYlo0,oOjkQt7jK7flE7NKLooLxT_HROYg,oOjkQt4ZrsJ0HnrJzEChyXfk5CYg,oOjkQt2r2H0ig6T17z8Ot0p3YZMk,oOjkQt_7yoznJ8bnC-KJLWpbYhvw,oOjkQt4aepHye2KVJ2jd891njUKY,oOjkQty8JCQQEHapcUxxGlPQyNic,oOjkQtxOCY7SFFkmB-SqLiA-wRwc,oOjkQtyKgb1JHnakbwIkSEYp0A_k,oOjkQtwfI0LRIGbW02tnMCdJTYOk,oOjkQt0FOaz_KEIeU75HyRil85kE,oOjkQt-WJBX--Qp4TaMkDuypNZ9E,oOjkQt66GFz89gGd24XnA_OvQnzw,oOjkQt3lDmbpd4LOVQDkfr8CBTnE,oOjkQtxkwEktnVMAoDvL3XkMMYls,oOjkQt7ELSYNJoWsqLHYZIrtPtJw,oOjkQt-sYk4qIfOoVP8QWb73i1FQ,oOjkQt25-o1z7A67LwjShqhTmSco,oOjkQt64447ObF4rZU9hRvs3caU0,oOjkQt6HGEQeZ_eiWP87x2EZbEQ8,oOjkQt-9s0CFpk2A_muMm3SZCekw,oOjkQt5WCIQa8aLIu7-lunoJtSyk,oOjkQt5mcRnwmODyXaUYx6b-47PQ,oOjkQt7WNqSHSW86R3Nn05Z2E3bA,oOjkQtyrExAJr_MZG3pPbogZrVTw,oOjkQt4D_aXyyjMDGgj9irjyxtoA,oOjkQt4ekTBKJDG0ZSGRPfjP_VcI,oOjkQtwg1Y0IlVBzqqr6L4zDT31Y,oOjkQt0bJxTxegxmgJ-mW84G58mg,oOjkQt3jWyWJjaUBpYEDOP3UHt8w,oOjkQtzQ_fGbD0G5BDq4PAkqWXV8,oOjkQty_wk1Q7vt5oVl70jH7eTIc,oOjkQt8tGiAQDZ3vn7362iD5GLJA,oOjkQt3UHERLTuB0DYaXjZnr53L8,oOjkQtxb9o5I0CWabarNs8aOr1iQ,oOjkQt1zYu0dEfgV3TYFHKqp6GzE,oOjkQt9YBmolnFt9Wabl4e1xvvVw,oOjkQt2uoaesU1j2MPPCOGpkH-yo,oOjkQt0N0AS_WxzzyEca4kBTMeMU,oOjkQtzkK5AVw-SW49rVK5lhM9g8,oOjkQt97SWOwOhZN3GiSYWcvvzSM,oOjkQt03cWuBw9x-XwcmBU-rC5-0,oOjkQt04Np8zuKQqN7RqzQ3JfiP8,oOjkQt30GQEfwFH-FNkn2QRsU1PM,oOjkQtxgTQnU10EJd6FwDtrN8NuA,oOjkQtyCGmnSbR8SqqI6LN55WaYw,oOjkQtyHNwpMVo-nQbx8vWjgZ6k4,oOjkQt4TbvnoKPqdaq6_fnpWZp5M,oOjkQt3DQt2ZDuODjRQILU2tcu1k,oOjkQt9aDMW4URVtntUe5UMyjqSo,oOjkQt0grNgKEsSot3B28wX3kb7Y,oOjkQt_NAzrQwk2ywUoplKklXwWQ,oOjkQt6MgHrpUQC-qsT0gqKhNdeY,oOjkQt8t5fknlP_wJu3lecwzgX1c,oOjkQtxl63GfNP7fVTgrtBJA8JwQ,oOjkQt5my-e_pVEmcvriZaHAEPG0,oOjkQt5XiiA7XI6x8CgVU3eSFkBY,oOjkQt9lCcR0Smh61wRLnNiPjRS0,oOjkQtwR4wU4Rt7NYRCzS8Mppy3c,oOjkQtxmQ_NtGGQ7O81EIZWTpASQ,oOjkQt09ul58yVkqqH68DEReJHcI,oOjkQt4Atn7hvG7wEmhbvJa5J5XA,oOjkQt9HwF0mcgeugECVowCM-gIA,oOjkQt17dMKMVjaoTcpSkESW5ie0,oOjkQt01rCOmRROwwt0vC4qsf4SU,oOjkQt6rSNnPbeCcWPKnmZyhoXw8,oOjkQt7L7OOK7PMyEusepH1v-EmI,oOjkQt0Lhxi4s7bE5F2zWphnyMMU,oOjkQt2QjT6rRrdB1fnUkQfnSLt0,oOjkQt-1enu4kI3w2sYD4Qfhwcwo,oOjkQtxMF9KFG09zz2IBsj_Ss6Ms,oOjkQt7Sk6bytvTn2ESkdWsh6LsU,oOjkQtw3LnNVhaR0TbtM97LdcwbI,oOjkQtynkJ2TR1oipC0yaeYf0B-c,oOjkQtya8fRxQaGsoh826coYbEJA,oOjkQt_M1N-8dMFDa7Z2bfuvCFTw";
		// String[] strarr = allOpenid.split(",");
		//
		// String token = WxUtils.getAccessTokenFromWxServer(PUBLIC_ACCOUNT_APPID,
		// PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL).getAccessToken();
		//
		// for (String s1 : strarr) {
		// WxUser wxUser = new WxUser();
		// String getUserUrl =
		// "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		// getUserUrl = getUserUrl.replace("ACCESS_TOKEN", token).replace("OPENID", s1);
		// JSONObject jsonObject3 = WxUtils.getDataFromWxServer(getUserUrl);
		//
		// String subscribe = jsonObject3.getString("subscribe");
		// wxUser.setSubscribe(subscribe);
		// String openidStr = jsonObject3.getString("openid");
		// wxUser.setOpenid(openidStr);
		// System.out.println("=========openid:" + openidStr);
		// String nickname = jsonObject3.getString("nickname");
		// // 将昵称进行编码
		// nickname = WxUtils.encodeNickName(nickname);
		// wxUser.setNickname(nickname);
		// System.out.println("=====" + WxUtils.decodeNickName(nickname));
		// String sex = jsonObject3.getString("sex");
		// wxUser.setSex(sex);
		// String language = jsonObject3.getString("language");
		// wxUser.setLanguage(language);
		// String city = jsonObject3.getString("city");
		// wxUser.setCity(city);
		// String province = jsonObject3.getString("province");
		// wxUser.setProvince(province);
		// String country = jsonObject3.getString("country");
		// wxUser.setCountry(country);
		// String headimgurl = jsonObject3.getString("headimgurl");
		// wxUser.setHeadimgurl(headimgurl);
		// String subscribe_time = jsonObject3.getString("subscribe_time");
		// wxUser.setSubscribe_time(subscribe_time);
		// String unionid = jsonObject3.getString("unionid");
		// wxUser.setUnionid(unionid);
		// String remark = jsonObject3.getString("remark");
		// wxUser.setRemark(remark);
		// String groupid = jsonObject3.getString("groupid");
		// wxUser.setGroupid(groupid);
		// String tagid_list = jsonObject3.getString("tagid_list");
		// wxUser.setTagid_list(tagid_list);
		// String subscribe_scene = jsonObject3.getString("subscribe_scene");
		// wxUser.setSubscribe_scene(subscribe_scene);
		// String qr_scene = jsonObject3.getString("qr_scene");
		// wxUser.setQr_scene(qr_scene);
		// String qr_scene_str = jsonObject3.getString("qr_scene_str");
		// wxUser.setQr_scene_str(qr_scene_str);
		//
		//
		// Map<String, Object> map = wxUser.getCreateParam(BaseBO.INVALID_CASE_ID,
		// wxUser);
		//
		// WxUser wxUserCreate = (WxUser) mapper.create(map);
		//
		// System.out.println("创建用户对象所需要的对象：" + wxUser);
		// System.out.println("创建出的用户对象：" + wxUserCreate);
		//
		// wxUser.setIgnoreIDInComparision(true);
		// if (wxUser.compareTo(wxUserCreate) != 0) {
		// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		// }
		// Assert.assertNotNull(wxUserCreate);
		// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(map.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
		// EnumErrorCode.EC_NoError);

		// Thread.sleep(1000);
		// }

	}

	// 获取用户信息
	@Test
	public void testGetUserInfo() {
		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");

		String getUserUrl = String.format(GET_USER_URL, accessToken.getAccessToken(), Shared.openid);
		JSONObject json = WxUtils.getDataFromWxServer(getUserUrl);
		String subscribe = json.getString("subscribe");
		System.out.println(subscribe);
		String openidStr = json.getString("openid");
		System.out.println(openidStr);
		String nickname = json.getString("nickname");
		System.out.println(nickname);
		String sex = json.getString("sex");
		System.out.println(sex);
		String language = json.getString("language");
		System.out.println(language);
		String city = json.getString("city");
		System.out.println(city);
		String province = json.getString("province");
		System.out.println(province);
		String country = json.getString("country");
		System.out.println(country);
		String headimgurl = json.getString("headimgurl");
		System.out.println(headimgurl);
		String subscribe_time = json.getString("subscribe_time");
		System.out.println(subscribe_time);
		if (null != json.get("unionid")) { // 返回的该字段可能为null，只有将公众号绑定到微信开放平台帐号后，才会出现该字段。
			String unionid = json.getString("unionid");
			System.out.println("unionid=" + unionid);
		}
		String remark = json.getString("remark");
		System.out.println(remark);
		String groupid = json.getString("groupid");
		System.out.println(groupid);
		String tagid_list = json.getString("tagid_list");
		System.out.println(tagid_list);
		String subscribe_scene = json.getString("subscribe_scene");
		System.out.println(subscribe_scene);
		String qr_scene = json.getString("qr_scene");
		System.out.println(qr_scene);
		String qr_scene_str = json.getString("qr_scene_str");
		System.out.println(qr_scene_str);
		System.out.println(json);

	}

	// xml转化为JSON，以及json转化为xml
	@Test
	public void testXMLToJson() throws Exception {
		// String result = "<xml><applications>" +
		// "<versions__delta>1</versions__delta>" + "<apps__hashcode></apps__hashcode>"
		// + "</applications></xml>";
		//
		// String xmlStr = "<xml> <ToUserName>< ![CDATA[qqwe] ]></ToUserName>
		// <FromUserName>< ![CDATA[qwe] ]></FromUserName>
		// <CreateTime>123456789</CreateTime> <MsgType>< ![CDATA[evqweqweent]
		// ]></MsgType> <Event>< ![CDATA[123dase] ]></Event> <EventKey><
		// ![CDATA[123adaeqwe] ]></EventKey> <Ticket>< ![CDATA[qwe123acasd] ]></Ticket>
		// </xml>";
		// JSONObject jsonObject = xmltoJson(xmlStr);
		// String s = jsonObject.toString(4);
		// System.out.println(s);
		//
		// String wxString = WxUtils.jsonToXML(s);
		// System.out.println(wxString);

		String s2 = "1434093047";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Long time = new Long(s2);
		// String d = dateFormat.format(time);
		Date date = new Date(time * 1000);
		// System.out.println(d);
		System.out.println(dateFormat.format(date).toString());
	}

	// 创建二维码
	@Test
	public void tetsCreateTempTicket() {
		// QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值
		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		String url = String.format(GET_QRCODE_URL, accessToken.getAccessToken());//

		Map<String, Object> param = new HashMap<>();
		param.put("action_name", "QR_LIMIT_STR_SCENE");
		Map<String, Object> action = new HashMap<>();
		Map<String, Object> scene = new HashMap<>();
		scene.put("scene_str", "1");
		action.put("scene", scene);
		param.put("action_info", action);

		JSONObject jsonObject = JSONObject.fromObject(param);
		Assert.assertNotNull(accessToken);
		JSONObject resultJson = WxUtils.postToWxServer(url, jsonObject.toString());

		System.out.println("===" + resultJson);
		String ticker = null;
		if (resultJson != null) {
			System.out.println("===" + resultJson.get("ticket") + "===" + resultJson.get("url"));
			ticker = (String) resultJson.get("ticket");
		} else {
			System.out.println("发送请求失败！！");
		}

		String getShowCodeURL = String.format(GET_SHOWQRCODE_URL, ticker);// 获取二维码的网址

		System.out.println(getShowCodeURL);
	}

	// 获取微信服务器IP地址
	@Test
	public void testGetWXIP() {
		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		String url = String.format(GET_WXIP_URL, accessToken.getAccessToken());
		JSONObject jsonObject = WxUtils.getDataFromWxServer(url);
		System.out.println(jsonObject);
	}

	// 网络检测
	@Test
	public void testPingWX() {
		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		String url = String.format(GET_WXPING_URL, accessToken.getAccessToken());
		JSONObject joData = new JSONObject();
		joData.put("action", "all");
		joData.put("check_operator", "DEFAULT");

		JSONObject jsonObject = WxUtils.postToWxServer(url, joData.toString());
		System.out.println(jsonObject);
	}

	// 创建自定义菜单
	@Test
	public void testCreateCustomMenu() {
		WxAccessToken accessToken = WxUtils.getAccessTokenFromWxServer(false, PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL);
		Assert.assertTrue(accessToken != null && BaseAction.ENV != EnumEnv.DEV, "请求微信Token发生错误，错误码：40164(环境不是dev的所以报错，不用修)");
		String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s", accessToken.getAccessToken());
		// 将菜单对象转换成json字符串
		WxCustomMenu wxCustomMenu = getWxCustomMenu();
		String jsonMenu = JSONObject.fromObject(wxCustomMenu).toString();
		JSONObject jsonObject = WxUtils.postToWxServer(url, jsonMenu);
		System.out.println(jsonObject);

	}

	private static WxCustomMenu getWxCustomMenu() {
		Button button = new Button();
		button.setName("绑定公司");
		button.setType("view");
		button.setKey("text");
		button.setUrl(BaseAction.DOMAIN + "wx/wxBaseLogin.bx");
		// 子菜单
		// Button subButton = new Button();
		// subButton.setName("二级菜单");
		// subButton.setType("view");
		// subButton.setKey("text");
		// subButton.setUrl("http://dev.wx.bxit.vip/wx/wxBaseLogin.bx");
		// button.setSub_button(new Button[] {subButton});
		WxCustomMenu wxCustomMenu = new WxCustomMenu();
		wxCustomMenu.setButton(new Button[] { button });
		return wxCustomMenu;
	}
}
