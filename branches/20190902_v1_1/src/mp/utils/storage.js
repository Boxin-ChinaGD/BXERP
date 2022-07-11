// 小程序用到的所有缓存的KEY
const AUTHORISED = "authorise"; // 0代表用户未授权，1代表用户已授权
const WX_USER_Info =  "storageUserInfo";// ：获取到的用户信息
const WX_USER_Mobile = "storageMobile";//：获取到的用户手机号码
const WX_USER_UnionID = "storageUnionID";//：获取到的用户unionID
const WX_USER_Openid = "storageOpenid";//：获取到的用户openid
const NBR_Session = "storageHeader";//：保存的会话

const DEFAULT_VIP_INFO = "storageVipInfo"; //：获取到的会员信息，与DEFAULT_Company对应
const VIP_VipList = "VIP_VipList"; // : 顾客的多会员信息列表，一个会员信息对应一个商家。与VIP_CompanyList一一对应

const DEFAULT_Company = "storageCompanyInfo";//：获取到的公司信息
const VIP_CompanyList = "VIP_CompanyList";//会员的所有公司列表
const Company_Address = "company_Address";//门店地址

module.exports = {
  AUTHORISED: AUTHORISED,
  WX_USER_Info: WX_USER_Info,
  WX_USER_Mobile: WX_USER_Mobile,
  WX_USER_UnionID: WX_USER_UnionID,
  WX_USER_Openid: WX_USER_Openid,
  NBR_Session: NBR_Session,
  DEFAULT_VIP_INFO: DEFAULT_VIP_INFO,
  VIP_VipList: VIP_VipList,
  DEFAULT_Company: DEFAULT_Company,
  VIP_CompanyList: VIP_CompanyList,
  Company_Address:Company_Address
}