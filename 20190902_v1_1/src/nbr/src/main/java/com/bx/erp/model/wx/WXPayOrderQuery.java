package com.bx.erp.model.wx;

public class WXPayOrderQuery  extends BaseWXPay{
	
	public static final WXPayOrderQueryField FIELD = new WXPayOrderQueryField();
      /*微信订单号*/
	private  String transaction_id	;
	/*商户订单号*/
	private  String out_trade_no	;
	@Override
	public String toString() {
		return "WXPayOrderQuery [transaction_id=" + transaction_id + ", out_trade_no=" + out_trade_no + "]";
	}
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
	
}