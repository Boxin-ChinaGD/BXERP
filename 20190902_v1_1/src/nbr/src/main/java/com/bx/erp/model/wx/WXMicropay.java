package com.bx.erp.model.wx;

public class WXMicropay extends BaseWXPay{
   public static final WXMicropay FIELD = new WXMicropay();
	/*商品描述*/
	private String body;

	/*商品详情*/
	private String detail;
	
	/*附加数据*/
	private String attach;
	
	/*客户订单号*/
	private String out_trade_no;
	/*总金额*/
	private String fee_type;
	/*终端IP*/
	private String spbill_create_ip;
	/*订单优惠标记*/
	private String goods_tag;
	/*指定支付方式*/
	private String limit_pay;
	/*交易起始时间*/
	private String time_start;
	/*交易结束时间*/
	private String time_expire;
	/*授权码*/
	private String auth_code;
	@Override
	public String toString() {
		return "WXMicropay [body=" + body + ", detail=" + detail + ", attach=" + attach + ", out_trade_no=" + out_trade_no + ", fee_type=" + fee_type + ", spbill_create_ip=" + spbill_create_ip + ", goods_tag=" + goods_tag + ", limit_pay="
				+ limit_pay + ", time_start=" + time_start + ", time_expire=" + time_expire + ", auth_code=" + auth_code + ", scene_info=" + scene_info + "]";
	}
	/*场景信息*/
	private String scene_info;
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getGoods_tag() {
		return goods_tag;
	}
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}
	public String getLimit_pay() {
		return limit_pay;
	}
	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getTime_expire() {
		return time_expire;
	}
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public String getScene_info() {
		return scene_info;
	}
	public void setScene_info(String scene_info) {
		this.scene_info = scene_info;
	}
}
