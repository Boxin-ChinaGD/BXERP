//package com.bx.erp.action.wx.wxopen;
//
//import java.util.Date;
//import java.util.Map;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.bx.erp.action.wx.wxopen.aes.WXBizMsgCrypt;
//import com.github.wxpay.sdk.WXPayUtil;
//
//
//@Controller
//@RequestMapping("/wx3rdPartyMessage")
//@Scope("prototype")
//public class Wx3rdPartyMessageAction extends WxOpenAction {
//	private static Log logger = LogFactory.getLog(Wx3rdPartyMessageAction.class);
//	
//	@Value("${third.party.appid}")
//	private String ComponentAppID;
//	
//	@Value("${third.party.token}")
//	private String TOKEN;
//	
//	@Value("${third.party.encodingAESKey}")
//	private String EncodingAesKey;
//	
//	/**
//	 * 消息回复格式： <br/>
//	 * ToUserName：消息接收方ID <br/>
//	 * FromUserName：消息发送方ID <br/>
//	 * MsgType：消息类型 <br/>
//	 * Content：消息内容
//	 */
//	private static final String xmlFormat = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>" + new Date().getTime() + "</CreateTime><MsgType><![CDATA[%3$s]]></MsgType><Content><![CDATA[%4$s]]></Content></xml>";
//	
//	private static final String EVENT = "event";
//	private static final String TEXT = "text";
//	private static final String IMAGE = "image";
//	private static final String VOICE = "voice";
//	private static final String VIDEO = "video";
//	private static final String SHORTVIDEO = "shortvideo";
//	private static final String LOCATION = "location";
//	private static final String LINK = "link";
//	
//	
//	
//	/** 公众号消息与事件接收URL*/
//	@ResponseBody
//	@RequestMapping(value = "/{appid}/callback")
//	public String callback(@RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("msg_signature") String msgSignature, @PathVariable String appid, @RequestBody String postData) {
//		logger.info("===================== 公众号消息与事件接收处理   =====================");
//		
//		try {
//			// 对xml消息进行解密
//			WXBizMsgCrypt pc = new WXBizMsgCrypt(TOKEN, EncodingAesKey, ComponentAppID);
//			String resultData = pc.decryptMsg(msgSignature, timestamp, nonce, postData.toString(), true);
//			logger.info("消息解密内容: \n" + resultData);
//			//
//			Map<String, String> resultMap = WXPayUtil.xmlToMap(resultData);
//			String msgType = resultMap.get("MsgType"); // 消息类型
//			String content = "";
//			//
//			switch (msgType) {
//			case EVENT:
//				handleEvent(resultMap);
//				
//				
//				break;
//			case TEXT:
//				content = "文本消息";
//				
//				return replyMsg(pc, resultMap, content);
//			case IMAGE:
//				content = "图片消息";
//				
//				break;
//			case VOICE:
//				content = "语音消息";
//				
//				break;
//			case VIDEO:
//				content = "视频消息";
//				
//				break;
//			case SHORTVIDEO:
//				content = "短视频消息";
//				
//				break;
//			case LOCATION:
//				content = "位置消息";
//				
//				break;
//			case LINK:
//				content = "链接消息";
//				
//				break;
//			default:
//				throw new RuntimeException("接收消息与事件异常...");
//			}
//			
//		} catch (Exception e) {
//			logger.info("其它异常：" + e.getMessage());
//		}
//		
//		return SUCCESS;
//	}
//	
//	/** 处理事件消息 */
//	private void handleEvent(Map<String, String> resultMap) {
//		logger.info("事件消息：" + resultMap);
//	}
//
//	/**
//	 * 公众号被动回复消息
//	 * @return encrptMsg
//	 * @throws Exception
//	 */
//	private String replyMsg(WXBizMsgCrypt pc, Map<String, String> resultMap, String content) throws Exception {
//		String platformID = resultMap.get("ToUserName"); // 公众号id
//		String userOpenID = resultMap.get("FromUserName"); // 用户openid
//		String msgType = resultMap.get("MsgType"); // 消息类型
//		
//		String replyMsgXML = String.format(xmlFormat, userOpenID, platformID, msgType, content); // 注意：此时公众号为发送方，用户为接收方
//		String afterEncrpt = pc.encryptMsg(replyMsgXML, String.valueOf(new Date().getTime()), WXPayUtil.generateNonceStr());
//		
//		return afterEncrpt;
//	}
//	
//}
