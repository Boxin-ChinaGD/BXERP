// 请求方式
const GET = 'GET';
const POST = 'POST';

const DEV_SERVERURL = 'https://dev.wx.bxit.vip/';
const SIT_SERVERURL = 'https://sit.wx.bxit.vip/';
const UAT_SERVERURL = 'https://uat.wx.bxit.vip/';
const PROD_SERVERURL = 'https://account.prosalesbox.cn/';
var serverURL = PROD_SERVERURL; //换不同的场，需要更改此字段的值

// TODO 以下IP地址将来要改为Prod的
const LoginURL = serverURL + 'miniprogram/loginEx.bx';
const selectMyCompanyURL = serverURL + 'miniprogram/selectMyCompanyEx.bx';
const vipCardR1URL = serverURL + 'vipCard/retrieve1Ex.bx';
const vipR1URL = serverURL + 'vip/retrieve1Ex.bx';
const vipUpdateURL = serverURL + 'vip/updateEx.bx';
const couponRNURL = serverURL + 'coupon/retrieveNEx.bx';
const getCouponURL = serverURL + 'couponCode/createEx.bx';
const myCouponRNURL = serverURL + 'couponCode/retrieveNByVipIDEx.bx';
const couponTotalURL = serverURL + 'couponCode/retrieveNTotalByVipIDEx.bx';
const retailtradeRNURL = serverURL + 'retailTrade/retrieveNEx.bx';
const bonusHistoryRNURL = serverURL + 'bonusConsumeHistory/retrieveNEx.bx';

module.exports = {
  server: serverURL,
  GET: GET,
  POST: POST,
  login: LoginURL,
  selectMyCompany: selectMyCompanyURL,
  vipCardR1: vipCardR1URL,
  vipR1: vipR1URL,
  vipUpdate: vipUpdateURL,
  couponRN: couponRNURL,
  getCoupon: getCouponURL,
  myCouponRN: myCouponRNURL,
  couponTotal: couponTotalURL,
  retailtradeRN: retailtradeRNURL,
  bonusHistoryRN: bonusHistoryRNURL
}