package com.bx.erp.model.wx;

import java.util.HashMap;
import java.util.Map;

import com.bx.erp.model.BaseModel;

//本类的字段命名必须和WX的一致
public class WxUser extends BaseModel {

	private static final long serialVersionUID = 1117102214784104717L;
	public static final WxUserField field = new WxUserField();

	protected String subscribe; // 是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息

	protected String openid;// 用户的唯一标识

	protected String nickname;// 用户昵称

	protected String sex;// 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知

	protected String language;// 用户的语言，简体中文为zh_CN

	protected String province;// 用户个人资料填写的省份

	protected String city;// 普通用户个人资料填写的城市

	protected String country;// 国家

	protected String headimgurl; // 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。

	protected String subscribe_time;// 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间

	protected String unionid;// 只有将公众号绑定到微信开放平台帐号后，才会出现该字段。

	protected String remark;// 对粉丝的备注

	protected String groupid;// 用户所在的分组ID

	protected String tagid_list;// 用户被打上的标签ID列表

	protected String subscribe_scene;// 返回用户关注的渠道来源

	protected String qr_scene;// 二维码扫码场景（开发者自定义）

	protected String qr_scene_str;// 二维码扫码场景描述（开发者自定义）

	// 尚未确定该值是否需要，先做保留
	// protected String privilege;// 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	// protected String FIELD_NAME_privilege;
	//
	// public String getFIELD_NAME_privilege() {
	// return "privilege";
	// }

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	// public String getPrivilege() {
	// return privilege;
	// }
	//
	// public void setPrivilege(String privilege) {
	// this.privilege = privilege;
	// }

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(String subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getTagid_list() {
		return tagid_list;
	}

	public void setTagid_list(String tagid_list) {
		this.tagid_list = tagid_list;
	}

	public String getSubscribe_scene() {
		return subscribe_scene;
	}

	public void setSubscribe_scene(String subscribe_scene) {
		this.subscribe_scene = subscribe_scene;
	}

	public String getQr_scene() {
		return qr_scene;
	}

	public void setQr_scene(String qr_scene) {
		this.qr_scene = qr_scene;
	}

	public String getQr_scene_str() {
		return qr_scene_str;
	}

	public void setQr_scene_str(String qr_scene_str) {
		this.qr_scene_str = qr_scene_str;
	}

	@Override
	public String toString() {
		return "WxUser [subscribe=" + subscribe + ", openid=" + openid + ", nickname=" + nickname + ", sex=" + sex + ", language=" + language + ", province=" + province + ", city=" + city + ", country=" + country + ", headimgurl="
				+ headimgurl + ", subscribe_time=" + subscribe_time + ", unionid=" + unionid + ", remark=" + remark + ", groupid=" + groupid + ", tagid_list=" + tagid_list + ", subscribe_scene=" + subscribe_scene + ", qr_scene=" + qr_scene
				+ ", qr_scene_str=" + qr_scene_str + "]";
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		WxUser wxUser = (WxUser) bm;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(field.getFIELD_NAME_subscribe(), wxUser.getSubscribe() == null ? "" : wxUser.getSubscribe());
		param.put(field.getFIELD_NAME_openid(), wxUser.getOpenid() == null ? "" : wxUser.getOpenid());
		param.put(field.getFIELD_NAME_nickname(), wxUser.getNickname());
		param.put(field.getFIELD_NAME_sex(), wxUser.getSex());
		param.put(field.getFIELD_NAME_language(), wxUser.getLanguage());
		param.put(field.getFIELD_NAME_city(), wxUser.getCity());
		param.put(field.getFIELD_NAME_province(), wxUser.getProvince());
		param.put(field.getFIELD_NAME_country(), wxUser.getCountry());
		param.put(field.getFIELD_NAME_headimgurl(), wxUser.getHeadimgurl());
		param.put(field.getFIELD_NAME_subscribe_time(), wxUser.getSubscribe_time());
		param.put(field.getFIELD_NAME_unionid(), wxUser.getUnionid());
		param.put(field.getFIELD_NAME_remark(), wxUser.getRemark() == null ? "" : wxUser.getRemark());
		param.put(field.getFIELD_NAME_groupid(), wxUser.getGroupid());
		param.put(field.getFIELD_NAME_tagid_list(), wxUser.getTagid_list());
		param.put(field.getFIELD_NAME_subscribe_scene(), wxUser.getSubscribe_scene());
		param.put(field.getFIELD_NAME_qr_scene(), wxUser.getQr_scene());
		param.put(field.getFIELD_NAME_qr_scene_str(), wxUser.getQr_scene_str() == null ? "" : wxUser.getQr_scene_str());
		// param.put(getFIELD_NAME_privilege(), wxUser.getPrivilege());

		return param;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, BaseModel bm) {
		checkParameterInput(bm);

		WxUser wxUser = (WxUser) bm;
		Map<String, Object> param = new HashMap<>();
		param.put(field.getFIELD_NAME_openid(), wxUser.getOpenid() == null ? "" : wxUser.getOpenid());

		return param;
	}

	@Override
	public int compareTo(BaseModel arg0) {
		if (arg0 == null) {
			return -1;
		}

		WxUser wxUser = (WxUser) arg0;
		if ((ignoreIDInComparision == true ? true : (wxUser.getID() == ID && printComparator(field.getFIELD_NAME_ID()))) //
				&& wxUser.getNickname().equals(nickname) && printComparator(field.getFIELD_NAME_nickname()) //
		) {
			return 0;
		}
		return -1;
	}

}
