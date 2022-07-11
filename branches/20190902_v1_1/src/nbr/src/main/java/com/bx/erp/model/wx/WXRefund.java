package com.bx.erp.model.wx;


public class WXRefund  extends BaseWXPay{
	public static final WXRefundField field = new WXRefundField();
	
    /*微信订单号*/
	private  String transaction_id; 
	/*商户订单号*/
	private  String out_trade_no;
   /*商户退款单号*/
	private  String out_refund_no	;
	/*订单金额*/
	private  String total_fee;
	/*申请退款金额*/
	private  String refund_fee;
	/*退款货币种类*/
	private  String refund_fee_type;
	/*退款原因*/
	private  String refund_desc;
	/*退款资金来源*/
	private  String refund_account;
	/*退款结果通知URL*/
	private  String notify_url;
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getOut_refund_no() {
		return out_refund_no;
	}
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}
	public String getRefund_fee_type() {
		return refund_fee_type;
	}
	public void setRefund_fee_type(String refund_fee_type) {
		this.refund_fee_type = refund_fee_type;
	}
	public String getRefund_desc() {
		return refund_desc;
	}
	public void setRefund_desc(String refund_desc) {
		this.refund_desc = refund_desc;
	}
	public String getRefund_account() {
		return refund_account;
	}
	public void setRefund_account(String refund_account) {
		this.refund_account = refund_account;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
}
