package com.bx.erp.test.paySIT.dataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.test.Shared;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONObject;

public class CommodityDataProvider {
	public static final String[] realCommodityName = { "达能牛奶香脆饼干", "欣欣杯朱古力", "达能甜趣清甜饼干", "米老头蛋黄煎饼", "滨崎小熊灌心饼", "格力高百奇饼干", "奥利奥草莓夹心饼干", "旺旺仙贝", "188双层雨衣", "爱莎小女学生三角裤", //
			"顺达鸣笛水壶", "锦尚眼部活肤精华", "百味佳苹果果酱", "盆子", "莎影洗发水", "合川桃片", "华锦天鹅绒短袜", "闪电香水", "蜂胶", "新兴清洁球", "金太阳扑克", "上海硫磺皂", "中宝黑电炒铲", "酸菜鱼佐料", "绣心毛巾", "小倩妹童袜", //
			"润心毛巾豆豆项围", "统一双拼面桶装", "三地睫毛膏", "卫生棉签", "越南咖啡", "快洁洗衣粉", "黑珍珠防晒水晶", "彩色灯泡", "千壶客酒", "洽旺荔枝", "白象红烧面", "朵彩防晒露", "老上海老酒", "信翔床垫", "泳圈", "北京锅巴", "大布豆童袜", //
			"美的微波炉", "相思鸟", "贵州茅台镇国宴", "珍意乐白砂糖", "琪祥饭勺", "花雨伞女棉袜", "手工挂面", "若男担担面", "酱拌碗面", "中秋月饼铁盒", "晨月手工礼盒面", "果仁多", "婴儿袜子", "博友小刀", "甜誜荞头", "3051中性笔", "安全汤匙", //
			"水巾", "佳婷卫生巾", "采珂洗发水", "五香豆", "运财肉味王", "华尔蒙斯糖", "富丽基洗发露", "老冰棍", "普洱茶", "心连心棉签", "花雨伞男棉袜", "记事本", "绅宝饮水杯", "龙头鱼", "优邦名片包", "一星仙潭小仙酒", "皇源香米", //
			"护眼灯", "平安灯", "赛车", "方巾", "洽旺桂圆", "夜灯心形", "小熊", "大豆蛋白", "华榕钙奶糖", "喜乐多洗碗巾", "马头香蕉片", "蜂花护发素", "洗发水", "舒爽护理", "梅花美味黄瓜", "众志内裤", "双株活力乳", "楚河鱼面", "浪莎平脚裤", //
			"三江粉丝", "龙口粉丝", "交通卡" };

	protected List<BaseModel> packageUnitList;
	protected List<BaseModel> brandList;
	protected List<BaseModel> categoryList;
	protected List<BaseModel> providerList;
	protected List<BaseModel> nomalCommodityList;

	public void setPackageUnitList(List<BaseModel> packageUnitList) {
		this.packageUnitList = packageUnitList;
	}

	public void setBrandList(List<BaseModel> brandList) {
		this.brandList = brandList;
	}

	public void setCategoryList(List<BaseModel> categoryList) {
		this.categoryList = categoryList;
	}

	public void setProviderList(List<BaseModel> providerList) {
		this.providerList = providerList;
	}

	public void setSimpleCommodityList(List<BaseModel> nomalCommodityList) {
		this.nomalCommodityList = nomalCommodityList;
	}

	public List<Commodity> generateRandomCommodities() throws Exception {
		List<Commodity> list = new ArrayList<Commodity>();
		//
		Random n = new Random();
		int count = n.nextInt(15) + 1;
		for (int i = 0; i < count; i++) {
			list.add(generateRandomCommodity());
		}
		return list;
	}

	public Commodity generateRandomCommodity() throws Exception {
		Commodity commodityInput = new Commodity();
		setCommonProperty(commodityInput);

		Random n = new Random();
		int commodityType = n.nextInt(EnumCommodityType.values().length);
		switch (EnumCommodityType.values()[commodityType]) {
		case ECT_Normal:
			setNomalProperty(commodityInput);
			break;
		case ECT_Combination:
			setCombinationProperty(commodityInput);
			break;
		case ECT_Service:
			setServiceProperty(commodityInput);
			break;
		case ECT_MultiPackaging:
		default:
			setMultiPackagingProperty(commodityInput);
			break;
		}

		return (Commodity) commodityInput.clone();
	}

	private void setCommonProperty(Commodity commodityInput) throws InterruptedException {
		Random n = new Random();
		PackageUnit packageUnit = (PackageUnit) packageUnitList.get(n.nextInt(packageUnitList.size()));

		commodityInput.setStatus(EnumStatusCommodity.ESC_Normal.getIndex());
		commodityInput.setName(realCommodityName[n.nextInt(realCommodityName.length)] + Shared.generateStringByTime(6));
		commodityInput.setShortName("商品");
		commodityInput.setSpecification("个");
		commodityInput.setPackageUnitID(packageUnit.getID());
		commodityInput.setPurchasingUnit(packageUnit.getName());
		commodityInput.setBrandID(brandList.get(n.nextInt(brandList.size())).getID());
		commodityInput.setCategoryID(brandList.get(n.nextInt(brandList.size())).getID());
		commodityInput.setProviderIDs(String.valueOf(providerList.get(n.nextInt(providerList.size())).getID()));
		commodityInput.setMnemonicCode(GeneralUtil.generateMnemonicCode(commodityInput.getName(), Commodity.Default_MnemonicCode));
		commodityInput.setPricingType(1);
		commodityInput.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		commodityInput.setPriceRetail(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.sum(n.nextInt(500), n.nextDouble())))); // 零售价
		commodityInput.setPriceVIP(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.sum(n.nextInt(300), n.nextDouble())))); // 会员价
		commodityInput.setPriceWholesale(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.sum(n.nextInt(100), n.nextDouble())))); // 批发价
		commodityInput.setCanChangePrice(n.nextInt(1));
		commodityInput.setRuleOfPoint(1);
		commodityInput.setPicture("url=116843435555");
		commodityInput.setShelfLife(n.nextInt(18) + 1);
		commodityInput.setReturnDays(n.nextInt(18) + 1);
		commodityInput.setPurchaseFlag(n.nextInt(1) + 1);//
		commodityInput.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
		commodityInput.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
		commodityInput.setTag("111");
		commodityInput.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commodityInput.setOperatorStaffID(Shared.BossID);
		commodityInput.setPropertyValue1("自定义属性1");
		commodityInput.setPropertyValue2("自定义属性2");
		commodityInput.setPropertyValue3("自定义属性3");
		commodityInput.setPropertyValue4("自定义属性4");
		commodityInput.setReturnObject(EnumBoolean.EB_Yes.getIndex()); // 返回对象的值
		commodityInput.setnOStart(Commodity.NO_START_Default); // ...常量
		commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default); // ...常量
		commodityInput.setStartValueRemark("");
		commodityInput.setBarcodes("asdkjvhi" + Shared.generateStringByTime(9));
		commodityInput.setMultiPackagingInfo(commodityInput.getBarcodes() + ";" + commodityInput.getPackageUnitID() + ";" + commodityInput.getRefCommodityMultiple() + ";" + commodityInput.getPriceRetail() + ";"
				+ commodityInput.getPriceVIP() + ";" + commodityInput.getPriceWholesale() + ";" + commodityInput.getName() + ";");
	}

	private void setNomalProperty(Commodity commodityInput) {
		commodityInput.setType(EnumCommodityType.ECT_Normal.getIndex());
		// 随机是否是期初商品
		Random n = new Random();
		boolean isStart = (n.nextInt(5) == 1);
		if (isStart) {
			commodityInput.setnOStart(n.nextInt(200) + 1);
			commodityInput.setPurchasingPriceStart(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.sum(n.nextInt(100), n.nextDouble()))));
		}
	}

	private void setMultiPackagingProperty(Commodity commodityInput) throws InterruptedException {
		commodityInput.setType(EnumCommodityType.ECT_Normal.getIndex());

		StringBuffer barcodesStringBuffer = new StringBuffer();
		barcodesStringBuffer.append(commodityInput.getBarcodes());
		//
		StringBuffer packageUnitIDStringBuffer = new StringBuffer();
		packageUnitIDStringBuffer.append(commodityInput.getPackageUnitID());
		//
		StringBuffer refCommodityMultipleStringBuffer = new StringBuffer();
		refCommodityMultipleStringBuffer.append(commodityInput.getRefCommodityMultiple());
		//
		StringBuffer priceRetailStringBuffer = new StringBuffer();
		priceRetailStringBuffer.append(commodityInput.getPriceRetail());
		//
		StringBuffer priceVIPStringBuffer = new StringBuffer();
		priceVIPStringBuffer.append(commodityInput.getPriceVIP());
		//
		StringBuffer priceWholesaleStringBuffer = new StringBuffer();
		priceWholesaleStringBuffer.append(commodityInput.getPriceWholesale());
		//
		StringBuffer nameStringBuffer = new StringBuffer();
		nameStringBuffer.append(commodityInput.getName());

		Random n = new Random();
		int multiPackageNumber = n.nextInt(5) + 1;
		for (int i = 0; i < multiPackageNumber; i++) {
			barcodesStringBuffer.append(",asdkwrsx" + Shared.generateStringByTime(9));
			packageUnitIDStringBuffer.append("," + packageUnitList.get(n.nextInt(packageUnitList.size())).getID());
			refCommodityMultipleStringBuffer.append("," + (n.nextInt(5) + 2));
			priceRetailStringBuffer.append("," + Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.sum(n.nextInt(500), n.nextDouble()))));
			priceVIPStringBuffer.append("," + Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.sum(n.nextInt(300), n.nextDouble()))));
			priceWholesaleStringBuffer.append("," + Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.sum(n.nextInt(100), n.nextDouble()))));
			Thread.sleep(1);
			nameStringBuffer.append("," + realCommodityName[n.nextInt(realCommodityName.length)] + Shared.generateStringByTime(6));
		}

		commodityInput.setMultiPackagingInfo(
				barcodesStringBuffer + ";" + packageUnitIDStringBuffer + ";" + refCommodityMultipleStringBuffer + ";" + priceRetailStringBuffer + ";" + priceVIPStringBuffer + ";" + priceWholesaleStringBuffer + ";" + nameStringBuffer + ";");
		System.out.println("多包装信息为" + commodityInput.getMultiPackagingInfo());
	}

	private void setCombinationProperty(Commodity commodityInput) {
		commodityInput.setType(EnumCommodityType.ECT_Combination.getIndex());
		commodityInput.setRuleOfPoint(0);
		commodityInput.setShelfLife(0);
		commodityInput.setPurchaseFlag(0);//
		//
		List<SubCommodity> subCommodities = new ArrayList<SubCommodity>();
		Commodity nomalCommodity = null;
		List<Integer> subCommIDList = new ArrayList<Integer>();
		Random n = new Random();
		int subCommodityCount = n.nextInt(5) + 2;
		for (int i = 0; i < subCommodityCount; i++) {
			do {
				nomalCommodity = (Commodity) nomalCommodityList.get(n.nextInt(nomalCommodityList.size()));
			} while (subCommIDList.contains(nomalCommodity.getID())); // 避免取到那种重复的子商品
			int no = n.nextInt((nomalCommodity.getNO() <= 0 ? 1 : nomalCommodity.getNO()));

			SubCommodity subCommodity = new SubCommodity();
			subCommodity.setSubCommodityNO(no == 0 ? 1 : no);
			subCommodity.setSubCommodityID(nomalCommodity.getID());
			subCommodity.setPrice(Double.valueOf(GeneralUtil.formatToCalculate(GeneralUtil.sum(n.nextInt(200), n.nextDouble()))));
			subCommodities.add(subCommodity);
			subCommIDList.add(nomalCommodity.getID());
		}
		commodityInput.setListSlave1(subCommodities);

		String subCommInfoJson = JSONObject.fromObject(commodityInput).toString();
		commodityInput.setSubCommodityInfo(subCommInfoJson);
	}

	private void setServiceProperty(Commodity commodityInput) {
		commodityInput.setType(EnumCommodityType.ECT_Service.getIndex());
		commodityInput.setShelfLife(0);
		commodityInput.setPurchasingUnit("");
		commodityInput.setPurchaseFlag(0);
	}
}
