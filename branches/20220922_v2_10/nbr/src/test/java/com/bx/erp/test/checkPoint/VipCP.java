package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.BonusRule;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.VipSource;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.test.BaseBonusRuleTest;
import com.bx.erp.test.BaseVipCardCodeTest;
import com.bx.erp.test.BaseVipSourceTest;
import com.bx.erp.test.Shared;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class VipCP {
	/** 1、检查会员A普通缓存是否创建。 2、检查数据库T_Vip是否创建了会员A的数据。 */
	public static void verifyCreate(MvcResult mr, Vip vip, String dbName) throws Exception {
		Vip vipClone = (Vip) vip.clone();
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Vip vipInDB = new Vip();
		vipInDB = (Vip) vipInDB.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(vipInDB != null, "解析异常");
		// 1、检查会员A普通缓存是否创建。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Vip).read1(vipInDB.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm != null, "普通缓存不存在创建的会员");
		// 2、检查数据库T_Vip是否创建了会员A的数据。
		vipClone.setIgnoreIDInComparision(true);
		int bonus = vipClone.getBonus();
		vipClone.setBonus(vipInDB.getBonus());// 创建会员时积分会自己查询，所以会与DB中的数据不一样。这里是为了能通过compareTo()
		assertTrue(vipClone.compareTo(vipInDB) == 0, "创建会员数据异常");
		vipClone.setBonus(bonus);
		//
		VipSource vipSource = new VipSource();
		vipSource.setPageIndex(BaseAction.PAGE_StartIndex);
		vipSource.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> vipSourcesList = BaseVipSourceTest.retrieveNViaMapper(vipSource, Shared.DBName_Test);
		boolean hasCreateVipSource = false;
		for (BaseModel bm2 : vipSourcesList) {
			VipSource vipSourceBm = (VipSource) bm2;
			if (vipSourceBm.getVipID() == vipInDB.getID()) {
				hasCreateVipSource = true;
				break;
			}
		}
		Assert.assertTrue(hasCreateVipSource, "创建VIP的时候，没有创建VipSource");
		//
		VipCardCode vipCardCode = new VipCardCode();
		vipCardCode.setPageIndex(BaseAction.PAGE_StartIndex);
		vipCardCode.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		vipCardCode.setVipID(vipInDB.getID());
		List<BaseModel> vipCardCodeList = BaseVipCardCodeTest.retrieveNViaMapper(vipCardCode, Shared.DBName_Test);
		Assert.assertTrue(vipCardCodeList != null && vipCardCodeList.size() > 0, "创建VIP的时候，没有创建VipCardCode");
		// 检查积分
		if (vipClone.getIsImported() == EnumBoolean.EB_NO.getIndex()) {
			BonusRule bonusRule = new BonusRule();
			bonusRule.setID(BaseBonusRuleTest.DEFAULT_BonusRule_ID);
			BonusRule bonusRuleInDB = (BonusRule) BaseBonusRuleTest.retrieve1ViaMapper(bonusRule, Shared.DBName_Test); // 获取的是nbr的积分规则
			Assert.assertTrue(bonusRuleInDB.getInitIncreaseBonus() == vipInDB.getBonus(), "新会员的默认积分和积分规则中的初始积分不一致");
		} else {
			Assert.assertTrue(vipClone.getBonus() == vipInDB.getBonus(), "导入商家数据进行创建会员,会员的初始积分和导入 数据的积分不一致");
		}
	}

	/** 1、检查会员A普通缓存是否修改。 2、检查数据库T_VIP是否修改了会员A的数据。 */
	public static void verifyUpdate(MvcResult mr, Vip vip, String dbName) throws Exception {
		Vip vipClone = (Vip) vip.clone();
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Vip vip2 = new Vip();
		vip2 = (Vip) vip2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(vip2 != null, "解析异常");
		// 1、检查会员A普通缓存是否修改。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Vip).read1(vip2.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm != null, "普通缓存不存在修改后的会员");
		// 3、检查数据库T_VIP是否修改了会员A的数据。
		Assert.assertTrue(vipClone.compareTo(vip2) == 0, "修改数据异常");
	}
}
