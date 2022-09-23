package com.bx.erp.model.wx;

import java.util.Map;

public class WxTemplate {

	public static final WxTemplateField field = new WxTemplateField();

	protected String template_id;// 模板ID

	protected String touser;// 目标客户

	protected String url;// 用户点击模板信息的跳转页面

	protected String topcolor;// 字体颜色

	protected Map<String, WxTemplateData> data;// 模板里的数据

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTopcolor() {
		return topcolor;
	}

	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}

	public Map<String, WxTemplateData> getData() {
		return data;
	}

	public void setData(Map<String, WxTemplateData> data) {
		this.data = data;
	}
	

//	// ...详细模板ID
//	/** 关注成功模板 */
//	public static final String FOLLOWSUCCESS = "FV4EimBojkImCFRQYhu9gwsab9NYYpQexuFbh3Nq97Y";
//	/** 登录成功模板 */
//	public static final String Login_Success = "brwFNvL_Snpt-HQka9tTB5xxYVK9cdXVFFZLwet8X0w";
//	/** 交班成功模板 */
//	public static final String Logout_Success = "7-WUaRMwHlCKp5MNzp0pZBDbAeLU1yta3hJXCiZVbPM";
//	/** 审核入库单，入库价与采购价不符*/
//	public static final String ApproveWarehousing = "JXxIiG804HdJRi2F49SqVHeYzjjBWRECO9W1KjJE_AA";
//	/** 绑定门店成功模板 */
//	public static final String BINDSUCCESS = "W3YiAk5bBuEi3h6xI_Dmz2NR1-KDXzNYa_PKcfhzDto"; 
//	/** 创建采购订单成功消息模板 */
//	public static final String Success_CreatePurchasingOrder = "mkPy3WTLd41Z7FtYTSvJbwIYHs2AKDw6TNMq6hun0ck"; 
//	/** 商品滞销提醒消息模板 */
//	public static final String UnsalableCommodity = "aX9C5oX51To2UkkcrS1NYVDwU_rM_w5kmF32Dn73lew";
	
	
	public static final String Bind_FirstData = "你好，已经成功为你绑定。";
	public static final String Bind_Remark = "感谢你的使用！";
}
