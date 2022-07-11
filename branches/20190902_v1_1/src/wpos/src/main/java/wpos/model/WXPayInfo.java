package wpos.model;

public class WXPayInfo extends BaseModel {
    public static final WXPayInfoField field = new WXPayInfoField();
    public static final String HTTP_WXPayInfo_MicroPay = "wxpay/microPayEx.bx";
    public static final String HTTP_WXPayInfo_Reverse = "wxpay/reverseEx.bx";
    public static final String HTTP_WXPayInfo_Refund =  "wxpay/refundEx.bx";

    //@Transient
    protected String sub_mch_id; // 子商户号

    //@Transient
    protected String nonce_str; // 随机字符串

    //@Transient
    protected String sign; // 签名

    //@Transient
    protected String body; // 商品描述

    //@Transient
    protected String out_trade_no; // 随机字符串

    //@Transient
    protected String total_fee; // 总金额

    //@Transient
    protected String spbill_create_ip; // 终端IP

    //@Transient
    protected String auth_code; // 授权码

    //@Transient
    protected String transaction_id; // 微信支付订单号

    //@Transient
    protected String refund_desc; // 退款原因

    //@Transient
    protected String out_refund_no; // 商户退款单号

    //@Transient
    protected String refund_fee; // 申请退款金额

    //@Transient
    protected String refund_id; // 微信退款单号


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

    @Override
    public String toString() {
        return "WXPayInfo [sub_mch_id=" + sub_mch_id + ", nonce_str=" + nonce_str + ", sign=" + sign + ", body=" + body + ", out_trade_no=" + out_trade_no + ", total_fee=" + total_fee + ", spbill_create_ip=" + spbill_create_ip
                + ", auth_code=" + auth_code + ", transaction_id=" + transaction_id + ", refund_desc=" + refund_desc + ", out_refund_no=" + out_refund_no + ", refund_fee=" + refund_fee + ", refund_id=" + refund_id + "]";
    }

}
