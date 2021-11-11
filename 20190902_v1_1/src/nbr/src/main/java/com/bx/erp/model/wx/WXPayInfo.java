package com.bx.erp.model.wx;

public class WXPayInfo {
	public static final WXPayInfoField field = new WXPayInfoField();

	// /** 服务商的APPID */
	// protected String appid;appid从配置取的

	/** 子商户号 */
	protected String sub_mch_id;

	/** 随机字符串 */
	protected String nonce_str;

	/** 签名 */
	protected String sign;

	/** 商品描述 */
	protected String body;

	/** 随机字符串 */
	protected String out_trade_no;

	/** 总金额 */
	protected String total_fee;

	/** 终端IP */
	protected String spbill_create_ip;

	/** 授权码 */
	protected String auth_code;

	/** 微信支付订单号 */
	protected String transaction_id;

	/** 退款原因 */
	protected String refund_desc;

	/** 商户退款单号 */
	protected String out_refund_no;

	/** 申请退款金额 */
	protected String refund_fee;

	/** 微信退款单号 */
	protected String refund_id;
	
	/** 微信支付时使用的优惠券码（非数据库字段） */
	protected String couponCode;

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	/** 会员卡Code */
	protected String wxVipCardCode;

	/** 会员卡ID */
	protected String wxVipCardID;

	/** 会员的积分总量 */
	protected int bonus;

	/** 增加的积分 */
	protected int addBonus;

	/** 是否进行积分变动 */
	protected int bonusIsChanged;

	protected int vipID;

	protected int wxVipID;

	protected int wxVipCardDetailID;

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getAuth_code() {
		return auth_code;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public String getSub_mch_id() {
		return sub_mch_id;
	}

	public void setSub_mch_id(String sub_mch_id) {
		this.sub_mch_id = sub_mch_id;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getRefund_desc() {
		return refund_desc;
	}

	public void setRefund_desc(String refund_desc) {
		this.refund_desc = refund_desc;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getWxVipCardCode() {
		return wxVipCardCode;
	}

	public void setWxVipCardCode(String wxVipCardCode) {
		this.wxVipCardCode = wxVipCardCode;
	}

	public String getWxVipCardID() {
		return wxVipCardID;
	}

	public void setWxVipCardID(String wxVipCardID) {
		this.wxVipCardID = wxVipCardID;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public int getAddBonus() {
		return addBonus;
	}

	public void setAddBonus(int addBonus) {
		this.addBonus = addBonus;
	}

	public int getBonusIsChanged() {
		return bonusIsChanged;
	}

	public void setBonusIsChanged(int bonusIsChanged) {
		this.bonusIsChanged = bonusIsChanged;
	}

	public int getVipID() {
		return vipID;
	}

	public void setVipID(int vipID) {
		this.vipID = vipID;
	}

	public int getWxVipID() {
		return wxVipID;
	}

	public void setWxVipID(int wxVipID) {
		this.wxVipID = wxVipID;
	}

	public int getWxVipCardDetailID() {
		return wxVipCardDetailID;
	}

	public void setWxVipCardDetailID(int wxVipCardDetailID) {
		this.wxVipCardDetailID = wxVipCardDetailID;
	}

	@Override
	public String toString() {
		return "WXPayInfo [sub_mch_id=" + sub_mch_id + ", nonce_str=" + nonce_str + ", sign=" + sign + ", body=" + body + ", out_trade_no=" + out_trade_no + ", total_fee=" + total_fee + ", spbill_create_ip=" + spbill_create_ip
				+ ", auth_code=" + auth_code + ", transaction_id=" + transaction_id + ", refund_desc=" + refund_desc + ", out_refund_no=" + out_refund_no + ", refund_fee=" + refund_fee + ", refund_id=" + refund_id + ", wxVipCardCode="
				+ wxVipCardCode + ", wxVipCardID=" + wxVipCardID + ", bonus=" + bonus + ", addBonus=" + addBonus + ", bonusIsChanged=" + bonusIsChanged + ", vipID=" + vipID + ", wxVipID=" + wxVipID + ", wxVipCardDetailID="
				+ wxVipCardDetailID + "]";
	}

}
