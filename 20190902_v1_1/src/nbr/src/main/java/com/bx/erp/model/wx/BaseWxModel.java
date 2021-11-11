package com.bx.erp.model.wx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class BaseWxModel extends BaseModel {
	private static final long serialVersionUID = 105418236548L;

	/** 解析 JSON/XML 内容错误 */
	public static final int ERRCODE_47701 = 47701;

	/** 微信相关的错误信息 */
//	public static final String WX_ERRMSG_WXCard_PictureEmpty = "上传文件为空";
//	public static final String WX_ERRMSG_WXCard_PictureSize = "图片大小不能超过1MB";
//	public static final String WX_ERRMSG_WXCard_PictureType = "图片类型不符合，类型必须为JPG或PNG";

	/** 微信支付返回数据key值 */
	public static final String WXPay_RETURN = "return_code";
	public static final String WXPay_RESULT = "result_code";
	public static final String WXPAY_ERR_CODE = "err_code";
	public static final String WXPAY_ERR_CODE_DES = "err_code_des";
	public static final String WXPAY_TRADE_STATE = "trade_state";

	/** 微信支付返回数据value值 */
	public static final String WXPay_SUCCESS = "SUCCESS";
	public static final String WXPay_FAIL = "FAIL";

	/** WX文档中的错误码，代表Token无效。一旦发现Token无效，需要重新申请 */
	public static final int WX_ERRCODE_InvalidToken = 40001;
	/** Wx文档中，表示发送消息成功，返回该errcode */
	public static final int WX_ERRCODE_Success = 0;
//	public static final int WX_ERRCODE_InvalidMerchantID = 40139;
	/** 该errcode在测试中使用，判断是否有发送消息的异常情况 */
	public static final int WXTest_ERRCODE_Failed = -1;

	public static final String WX_ERRCODE = "errcode";
	public static final String WX_ERRMSG = "errmsg";

	/** 解析微信的特定字段 */
	public static final String WX_XML = "xml";
//	public static final String WX_FROMUSERNAME = "FromUserName";
	public static final String WX_MSGTYPE = "MsgType";
	public static final String WX_EVENT = "event";
	public static final String WX_EVENTS = "Event";
	public static final String WX_Subscribe = "subscribe"; // 用户是否关注
//	public static final String WX_CardMerchantCheckResult = "card_merchant_check_result"; // 子商户审核结果
//	public static final String WX_CustomerGetCard = "user_get_card"; // 卡券领取事件推送
//	public static final String WX_CustomerGiftingCard = "user_gifting_card"; // 卡券转赠事件推送
	public static final String WX_CODE = "code"; // 使用静默授权或页面授权后微信返回的值,使用
	public static final String WX_REFRESH_TOKEN = "refresh_token";
	public static final String WX_SIGNATURE = "signature"; // 建立微信服务器与NBR服务器的安全信道时，微信返回的加密的签名
	public static final String WX_TIMESTAMP = "timestamp"; // 建立微信服务器与NBR服务器的安全信道时，微信返回的时间戳
	public static final String WX_NONCE = "nonce"; // 建立微信服务器与NBR服务器的安全信道时，微信返回的随机值
	public static final String WX_ECHOSTR = "echostr"; // 建立微信服务器与NBR服务器的安全信道时，微信返回的随机字符串
//	public static final String WX_CustomerDeleteCard = "user_del_card";

	/** 门店老板的微信openid */
//	public static final String WX_OPENID_1 = "oHodU5xfaydSpb7go64g_gH4gtbk"; // 将来从数据库中读取配置

	/** 推送消息模板templateid */
//	public static final String WX_TEMPLATEID_1 = "mxmO307FONmnRYqm9uokpGoMC1PF8zKf7XtHUpjmfQ4"; // 推送日销售报表

	/** 消息详情的URL */
	public static final String WX_URL_1 = BaseAction.DOMAIN + "/wx/wxBaseLogin.bx"; // 推送日销售报表详情URL

	/** 小程序appid */
//	public static final String MiniProgram_APPID = "wxd79390687916a534"; // 小程序APPID

	/** 小程序secret */
//	public static final String MiniProgram_SECRET = "c39cc43fdbda74ef81e4d0dd93458507"; // 小程序SRCRET

//	public final static String WXCard = "card";
//	public final static String WXCard_type = "card_type";
//	public final static String WXCard_baseInfo = "base_info";
//	public final static String WXCard_createTime = "create_time";

//	public final static String WXCardType_GROUPON = "GROUPON";
//	public final static String WXCardType_DISCOUNT = "DISCOUNT";
//	public final static String WXCardType_GIFT = "GIFT";
//	public final static String WXCardType_CASH = "CASH";
//	public final static String WXCardType_GENERAL_COUPON = "GENERAL_COUPON";
//	public final static String WXCardType_MEMBER_CARD = "MEMBER_CARD";
//	public final static String WXCardType_SCENIC_TICKET = "SCENIC_TICKET";
//	public final static String WXCardType_MOVIE_TICKET = "MOVIE_TICKET";
//	public final static String WXCardType_BOARDING_PASS = "BOARDING_PASS";
//	public final static String WXCardType_MEETING_TICKET = "MEETING_TICKET";
//	public final static String WXCardType_BUS_TICKET = "BUS_TICKET";

//	public static final int OFFSET_Default = 1;
//	public static final int COUNT_Default = 10;

	public JSONObject toWxJson(BaseWxModel bwm) {
		throw new RuntimeException("尚未实现toJson()方法！");
	}

	public JSONArray toWxJson(List<BaseWxModel> bmList) {
		throw new RuntimeException("尚未实现toJson()方法！");
	}

	@Override
	protected void checkParameterInput(BaseModel bm) {
		super.checkParameterInput(bm);
	}

	public Map<String, Object> getHttpCreateParam(int iUseCaseID, BaseWxModel bwm) {
		throw new RuntimeException("尚未实现getHttpCreateParam()方法！");
	}

	public JSONArray getHttpCreateParam(int iUseCaseID, List<BaseWxModel> bwmList) {
		throw new RuntimeException("尚未实现getHttpCreateParam()方法！");
	}

	public Map<String, Object> getHttpRetrieve1Param(int iUseCaseID, BaseWxModel bwm) {
		throw new RuntimeException("尚未实现getHttpRetrieve1Param()方法！");
	}

	public Map<String, Object> getHttpRetrieveNParam(int iUseCaseID, BaseWxModel bwm) {
		throw new RuntimeException("尚未实现getHttpRetrieveNParam()方法！");
	}

	public Map<String, Object> getHttpUpdateParam(int iUseCaseID, BaseWxModel bwm) {
		throw new RuntimeException("尚未实现getHttpUpdateParam()方法！");
	}

	public Map<String, Object> getHttpDeleteParam(int iUseCaseID, BaseWxModel bwm) {
		throw new RuntimeException("尚未实现getHttpDeleteParam()方法！");
	}

	/** 删除json对象中value值为null和""的K-V对 */
	public void cleanInvalidJSONValue(JSONObject jsonObject) {
		Iterator<?> it = jsonObject.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = jsonObject.getString(key);

			if (jsonObject.get(key) instanceof JSONObject) {
				JSONObject subjson = (JSONObject) jsonObject.get(key);

				cleanInvalidJSONValue(subjson);
			} else {
				if ("".equals(value) || "null".equals(value) || "0".equals(value) || "[]".equals(value)) {
					jsonObject.discard(key);
					it = jsonObject.keys();
				}
			}
		}
	}

	public List<?> parseN(int iUseCaseID, String s) {
		throw new RuntimeException("该函数尚未实现！");
	}

	public List<?> parseN(int iUseCaseID, JSONArray jsonArray) {
		List<BaseModel> bmList = new ArrayList<>();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (this.parse1(jsonObject.toString()) == null) {
					return null;
				}
				bmList.add(this.clone());
			}
			return bmList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public BaseModel parse1(int iUseCaseID, String s) {
		try {
			return doParse1(iUseCaseID, JSONObject.fromObject(s));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected BaseModel doParse1(int iUseCaseID, JSONObject jo) {
		throw new RuntimeException("尚未实现doParse1()方法！");
	}

//	public enum EnumWxCardAndCouponType {
//		EWCACT_GROUPON("GROUPON", 0), // 团购券类型
//		EWCACT_CASH("CASH", 1), // 代金券类型
//		EWCACT_DISCOUNT("DISCOUNT", 2), // 折扣券类型
//		EWCACT_GIFT("GIFT", 3), // 兑换券类型
//		EWCACT_GENERAL_COUPON("GENERAL_COUPON", 4), // 优惠券类型
//		EWCACT_MEMBER_CARD("MEMBER_CARD", 5); // 会员卡类型
//
//		private String name;
//		private int index;
//
//		private EnumWxCardAndCouponType(String name, int index) {
//			this.name = name;
//			this.index = index;
//		}
//
//		public static String getName(int index) {
//			for (EnumWxCardAndCouponType c : EnumWxCardAndCouponType.values()) {
//				if (c.getIndex() == index) {
//					return c.name;
//				}
//			}
//			return null;
//		}
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public int getIndex() {
//			return index;
//		}
//
//		public void setIndex(int index) {
//			this.index = index;
//		}
//	}

//	public enum EnumCardAndCouponStatus {
//		EWCACS_CARD_STATUS_NOT_VERIFY("CARD_STATUS_NOT_VERIFY", 0), // 待审核
//		EWCACS_CARD_STATUS_VERIFY_FAIL("CARD_STATUS_VERIFY_FAIL", 1), // 审核失败
//		EWCACS_CARD_STATUS_VERIFY_OK("CARD_STATUS_VERIFY_OK", 2), // 通过审核
//		EWCACS_CARD_STATUS_DELETE("CARD_STATUS_DELETE", 3), // 卡券被商户删除
//		EWCACS_CARD_STATUS_DISPATCH("CARD_STATUS_DISPATCH", 4); // 在公众平台投放过的卡券
//
//		private String name;
//		private int index;
//
//		private EnumCardAndCouponStatus(String name, int index) {
//			this.name = name;
//			this.index = index;
//		}
//
//		public static String getName(int index) {
//			for (EnumCardAndCouponStatus c : EnumCardAndCouponStatus.values()) {
//				if (c.getIndex() == index) {
//					return c.name;
//				}
//			}
//			return null;
//		}
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public int getIndex() {
//			return index;
//		}
//
//		public void setIndex(int index) {
//			this.index = index;
//		}
//		
//		public static boolean contains(String value) {
//			for (EnumCardAndCouponStatus status : EnumCardAndCouponStatus.values()) {
//				if (status.getName().equals(value)) {
//					return true;
//				}
//			}
//			return false;
//		}
//	}

//	public enum EnumCardAndCouponDateInfoType{
//		EWCACD_DATE_TYPE_PERMANENT("DATE_TYPE_PERMANENT", 0), // 永久
//		EWCACD_DATE_TYPE_FIX_TIME_RANGE("DATE_TYPE_FIX_TIME_RANGE", 1), // 表示固定日期区间
//		EWCACD_DATE_TYPE_FIX_TERM("DATE_TYPE FIX_TERM", 2); // 表示固定时长（自领取后按天算）
//		
//		private String name;
//		private int index;
//
//		private EnumCardAndCouponDateInfoType(String name, int index) {
//			this.name = name;
//			this.index = index;
//		}
//
//		public static String getName(int index) {
//			for (EnumCardAndCouponDateInfoType c : EnumCardAndCouponDateInfoType.values()) {
//				if (c.getIndex() == index) {
//					return c.name;
//				}
//			}
//			return null;
//		}
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public int getIndex() {
//			return index;
//		}
//
//		public void setIndex(int index) {
//			this.index = index;
//		}
//	
//	}
}
