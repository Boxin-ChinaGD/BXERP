package com.bx.erp.test.robot.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONObject;

public class CreateCommodityEx extends ProgramEx {

	public static String barcodeEx = "4548293894545";
	public static String normalCommodityEx = "机器人随机创建的单品";
	public static String combinationCommodityEx = "机器人随机创建的组合商品";
	public static String serviceCommodityEx = "机器人随机创建的服务商品";
	public static String commodityExA = "机器人随机创建的多包装商品A";
	public static String commodityExB = "机器人随机创建的多包装商品B";
	public static String commodityExC = "机器人随机创建的多包装商品C";

	// 用于保存已创建的单品
	public static List<Commodity> normalCommodityCreatedList = new ArrayList<>();
	// 用于保存已创建的多包装商品
	public static List<Commodity> multiPackagingCommodityCreatedList = new ArrayList<>();
	// 用于保存已创建的组合商品
	public static List<Commodity> combinationCommodityCreatedList = new ArrayList<>();
	// 用于保存已创建的服务商品
	public static List<Commodity> serviceCommodityCreatedList = new ArrayList<>();

	private static int INDEX = 0;

	public enum EnumOperationType {
		EOT_CreateCommodity("Create Commodity", INDEX++), //
		EOT_UpdateCommodity("Update Commodity", INDEX++), //
		EOT_RetrieveNExCommodity("RetrieveNEx Commodity", INDEX++), //
		EOT_Retrieve1ExCommodity("Retrieve1Ex Commodity", INDEX++), //
		EOT_DeleteCommodity("Delete Commodity", INDEX++); //

		private String name;
		private int index;

		private EnumOperationType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumOperationType ept : EnumOperationType.values()) {
				if (ept.getIndex() == index) {
					return ept.name;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}
	}

	@Override
	public boolean run() throws CloneNotSupportedException, InterruptedException {
		//
		commodityProgramEx();
		return false;
	}

	private void commodityProgramEx() throws CloneNotSupportedException, InterruptedException {
		// 随机CRUD商品活动
		switch (EnumOperationType.values()[new Random().nextInt(5)]) {
		case EOT_CreateCommodity:
			createCommodity();
			break;
		case EOT_UpdateCommodity:
			updateCommodity();
			break;
		case EOT_RetrieveNExCommodity:
			retrieveNExCommodity();
			break;
		case EOT_Retrieve1ExCommodity:
			retrieve1ExCommodity();
			break;
		case EOT_DeleteCommodity:
			deleteCommodity();
			break;
		default:
			break;
		}
	}

	private void retrieve1ExCommodity() {
		Shared.caseLog("r1Ex商品");
		switch (EnumCommodityType.values()[new Random().nextInt(4)]) {
		case ECT_Normal:
			if (normalCommodityCreatedList.size() > 0) {
				int ramdomIndex = new Random().nextInt(normalCommodityCreatedList.size());
				Commodity commRetrieve1ExCondition = normalCommodityCreatedList.get(ramdomIndex);
				System.out.println(commRetrieve1ExCondition.getID());
				// 这里set需要查询的商品ID
				Commodity commodity = CreateCommodity.retrieve1CommodityEx(commRetrieve1ExCondition);
				System.out.println("r1Ex普通商品成功：" + commodity);
			}
			break;
		case ECT_Combination:
			if (combinationCommodityCreatedList.size() > 0) {
				int ramdomIndex = new Random().nextInt(combinationCommodityCreatedList.size());
				Commodity commRetrieve1ExCondition = combinationCommodityCreatedList.get(ramdomIndex);
				System.out.println(commRetrieve1ExCondition.getID());
				// 这里set需要查询的商品ID
				Commodity commodity = CreateCommodity.retrieve1CommodityEx(commRetrieve1ExCondition);
				System.out.println("r1Ex组合商品成功：" + commodity);
			}
			break;
		case ECT_MultiPackaging:
			if (multiPackagingCommodityCreatedList.size() > 0) {
				int ramdomIndex = new Random().nextInt(multiPackagingCommodityCreatedList.size());
				Commodity commRetrieve1ExCondition = multiPackagingCommodityCreatedList.get(ramdomIndex);
				System.out.println(commRetrieve1ExCondition.getID());
				// 这里set需要查询的商品ID
				Commodity commodity = CreateCommodity.retrieve1CommodityEx(commRetrieve1ExCondition);
				System.out.println("r1Ex多包装商品成功：" + commodity);
			}
			break;
		case ECT_Service:
			if (serviceCommodityCreatedList.size() > 0) {
				int ramdomIndex = new Random().nextInt(serviceCommodityCreatedList.size());
				Commodity commRetrieve1ExCondition = serviceCommodityCreatedList.get(ramdomIndex);
				System.out.println(commRetrieve1ExCondition.getID());
				// 这里set需要查询的商品ID
				Commodity commodity = CreateCommodity.retrieve1CommodityEx(commRetrieve1ExCondition);
				System.out.println("r1Ex服务商品成功：" + commodity);
			}
			break;
		default:
			break;
		}

	}

	private void deleteCommodity() {
		Shared.caseLog("删除商品");
		if (normalCommodityCreatedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(normalCommodityCreatedList.size());
			Commodity commDeleteCondition = normalCommodityCreatedList.get(ramdomIndex);
			CreateCommodity.deleteCommodity(commDeleteCondition);
			normalCommodityCreatedList.remove(ramdomIndex);
		}
	}

	private void updateCommodity() throws InterruptedException {
		Shared.caseLog("更新商品");
		// 组合不能update
		if (normalCommodityCreatedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(normalCommodityCreatedList.size());
			Commodity commodity = normalCommodityCreatedList.get(ramdomIndex);
			commodity.setMultiPackagingInfo(
					commodity.getBarcodes() + Shared.generateStringByTime(2) + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + Shared.generateStringByTime(2) + ";"
							+ commodity.getPriceVIP() + Shared.generateStringByTime(2) + ";" + commodity.getPriceWholesale() + Shared.generateStringByTime(2) + ";" + commodity.getName() + Shared.generateStringByTime(2) + ";");
			commodity.setProviderIDs("1");
			commodity.setReturnObject(EnumBoolean.EB_Yes.getIndex());
			Commodity com = CreateCommodity.createOrUpdateCommodity("/commoditySync/updateEx.bx", commodity);
			normalCommodityCreatedList.remove(ramdomIndex);
			normalCommodityCreatedList.add(com);
		}
	}

	private void retrieveNExCommodity() {
		Shared.caseLog("rnEx商品");
		if (normalCommodityCreatedList.size() > 0 || multiPackagingCommodityCreatedList.size() > 0 || combinationCommodityCreatedList.size() > 0 || serviceCommodityCreatedList.size() > 0) {
			Commodity commRetrieveNCondition = new Commodity();
			commRetrieveNCondition.setQueryKeyword("机器人随机");
			commRetrieveNCondition.setType(BaseAction.INVALID_ID); // 查询全部类型
			commRetrieveNCondition.setStatus(BaseAction.INVALID_ID);
			commRetrieveNCondition.setCategoryID(BaseAction.INVALID_ID);
			commRetrieveNCondition.setBrandID(BaseAction.INVALID_ID);
			Commodity commRNEx = CreateCommodity.retrieveNCommodityEx(commRetrieveNCondition);
			System.out.println("rnEx商品成功：" + commRNEx);
		}
	}

	private void createCommodity() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("创建商品");
		// 单品、多包装、组合、服务
		switch (EnumCommodityType.values()[new Random().nextInt(4)]) {
		case ECT_Normal:
			createNormalCommodity();
			break;
		case ECT_Combination:
			createCombinationCommodity();
			break;
		case ECT_MultiPackaging:
			createMultiPackagingCommodity();
			break;
		case ECT_Service:
			createServiceCommodity();
			break;
		default:
			break;
		}
	}

	private void createServiceCommodity() throws CloneNotSupportedException, InterruptedException {
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodity.setPurchasingUnit("");
		commodity.setShelfLife(0);
		commodity.setName(serviceCommodityEx + Shared.generateStringByTime(8));
		commodity.setMultiPackagingInfo(barcodeEx + ";3;10;1;8;8;" + serviceCommodityEx + Shared.generateStringByTime(8) + ";");
		Commodity commodityCreate = CreateCommodity.createOrUpdateCommodity("/commoditySync/createEx.bx", commodity);
		serviceCommodityCreatedList.add(commodityCreate);
		System.out.println("创建服务商品成功：" + commodityCreate);
	}

	private void createMultiPackagingCommodity() throws CloneNotSupportedException, InterruptedException {
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity.setName(normalCommodityEx + Shared.generateStringByTime(8));
		commodity.setProviderIDs("7");
		commodity.setMultiPackagingInfo(barcodeEx + ";1;1;1;8;8;" + normalCommodityEx + Shared.generateStringByTime(8) + ";");
		Commodity commodityCreate = CreateCommodity.createOrUpdateCommodity("/commoditySync/createEx.bx", commodity);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(normalCommodityEx + Shared.generateStringByTime(8));
		commUpdate.setID(commodityCreate.getID());
		commUpdate.setProviderIDs("1");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + commUpdate.getName() + Shared.generateStringByTime(8) + "," + commodityExA + Shared.generateStringByTime(8) + "," + commodityExB + Shared.generateStringByTime(8) + ","
				+ commodityExC + "新增多包装" + Shared.generateStringByTime(8) + ";");

		Commodity multiPackagingCommodityCreate = CreateCommodity.createOrUpdateCommodity("/commoditySync/createEx.bx", commUpdate);
		multiPackagingCommodityCreatedList.add(multiPackagingCommodityCreate);
		System.out.println("创建多包装商品成功：" + multiPackagingCommodityCreate);
	}

	private void createCombinationCommodity() throws CloneNotSupportedException, InterruptedException {
		// TODO 组合商品的子商品写死了，考虑随机......
		Commodity subCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		if(normalCommodityCreatedList.size() > 1) {
			for(int i=0; i < 2; i++) {
				SubCommodity subCommodity1 = (SubCommodity) subCommodity.getListSlave1().get(i);
				subCommodity1.setCommodityID(normalCommodityCreatedList.get(i).getID());
			}
		}
		subCommodity.setShelfLife(BaseAction.INVALID_ID);
		subCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity.setProviderIDs("7");
		subCommodity.setName(combinationCommodityEx + Shared.generateStringByTime(8));
		subCommodity.setMultiPackagingInfo(barcodeEx + ";1;1;1;8;8;" + combinationCommodityEx + Shared.generateStringByTime(8) + ";");//
		Commodity commodityCreate = CreateCommodity.createOrUpdateCommodity("/commoditySync/createEx.bx", subCommodity);
		combinationCommodityCreatedList.add(commodityCreate);
		System.out.println("创建组合商品成功：" + commodityCreate);
	}

	private void createNormalCommodity() throws CloneNotSupportedException, InterruptedException {
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodity.setName(normalCommodityEx + Shared.generateStringByTime(8));
		commodity.setProviderIDs("7");
		commodity.setMultiPackagingInfo(barcodeEx + ";1;1;1;8;8;" + normalCommodityEx + Shared.generateStringByTime(8) + ";");
		Commodity commodityCreate = CreateCommodity.createOrUpdateCommodity("/commoditySync/createEx.bx", commodity);
		normalCommodityCreatedList.add(commodityCreate);
		System.out.println("创建单品成功：" + commodityCreate);
	}

	public static Commodity createOrUpdateCommodity(String url, Commodity commodity) {
		Map<String, String> params = new HashMap<>();
		params.put(Commodity.field.getFIELD_NAME_multiPackagingInfo(), commodity.getMultiPackagingInfo());
		params.put(Commodity.field.getFIELD_NAME_name(), commodity.getName());
		params.put(Commodity.field.getFIELD_NAME_shortName(), commodity.getShortName());
		params.put(Commodity.field.getFIELD_NAME_specification(), commodity.getSpecification());
		params.put(Commodity.field.getFIELD_NAME_packageUnitID(), String.valueOf(commodity.getPackageUnitID()));
		params.put(Commodity.field.getFIELD_NAME_purchasingUnit(), commodity.getPurchasingUnit());
		params.put(Commodity.field.getFIELD_NAME_brandID(), String.valueOf(commodity.getBrandID()));
		params.put(Commodity.field.getFIELD_NAME_categoryID(), String.valueOf(commodity.getCategoryID()));
		params.put(Commodity.field.getFIELD_NAME_mnemonicCode(), commodity.getMnemonicCode());
		params.put(Commodity.field.getFIELD_NAME_pricingType(), String.valueOf(commodity.getPricingType()));
		params.put(Commodity.field.getFIELD_NAME_priceRetail(), String.valueOf(commodity.getPriceRetail()));
		params.put(Commodity.field.getFIELD_NAME_priceVIP(), String.valueOf(commodity.getPriceVIP()));
		params.put(Commodity.field.getFIELD_NAME_priceWholesale(), String.valueOf(commodity.getPriceWholesale()));
		params.put(Commodity.field.getFIELD_NAME_canChangePrice(), String.valueOf(commodity.getCanChangePrice()));
		params.put(Commodity.field.getFIELD_NAME_ruleOfPoint(), String.valueOf(commodity.getRuleOfPoint()));
		params.put(Commodity.field.getFIELD_NAME_picture(), commodity.getPicture());
		params.put(Commodity.field.getFIELD_NAME_shelfLife(), String.valueOf(commodity.getShelfLife()));
		params.put(Commodity.field.getFIELD_NAME_returnDays(), String.valueOf(commodity.getReturnDays()));
		params.put(Commodity.field.getFIELD_NAME_purchaseFlag(), String.valueOf(commodity.getPurchaseFlag()));
		params.put(Commodity.field.getFIELD_NAME_refCommodityID(), String.valueOf(commodity.getRefCommodityID()));
		params.put(Commodity.field.getFIELD_NAME_refCommodityMultiple(), String.valueOf(commodity.getRefCommodityMultiple()));
		params.put(Commodity.field.getFIELD_NAME_tag(), commodity.getTag());
		params.put(Commodity.field.getFIELD_NAME_NO(), String.valueOf(commodity.getNO()));
		params.put(Commodity.field.getFIELD_NAME_type(), String.valueOf(commodity.getType()));
		params.put(Commodity.field.getFIELD_NAME_nOStart(), commodity.getnOStart() == 0 ? String.valueOf(Commodity.NO_START_Default) : String.valueOf(commodity.getnOStart()));
		params.put(Commodity.field.getFIELD_NAME_purchasingPriceStart(),
				Math.abs(GeneralUtil.sub(commodity.getPurchasingPriceStart(), 0)) < BaseModel.TOLERANCE ? String.valueOf(Commodity.PURCHASING_PRICE_START_Default) : String.valueOf(commodity.getPurchasingPriceStart()));
		params.put(Commodity.field.getFIELD_NAME_returnObject(), String.valueOf(commodity.getReturnObject()));
		params.put(Commodity.field.getFIELD_NAME_multiPackagingInfo(), commodity.getMultiPackagingInfo());
		params.put(Commodity.field.getFIELD_NAME_providerIDs(), commodity.getProviderIDs());
		params.put(Commodity.field.getFIELD_NAME_propertyValue1(), commodity.getPropertyValue1());
		params.put(Commodity.field.getFIELD_NAME_propertyValue2(), commodity.getPropertyValue2());
		params.put(Commodity.field.getFIELD_NAME_propertyValue3(), commodity.getPropertyValue3());
		params.put(Commodity.field.getFIELD_NAME_propertyValue4(), commodity.getPropertyValue4());
		params.put(Commodity.field.getFIELD_NAME_returnObject(), String.valueOf(commodity.getReturnObject()));
		// 组合商品
		if (commodity.getType() == 1) {
			String json = JSONObject.fromObject(commodity).toString();
			params.put(Commodity.field.getFIELD_NAME_subCommodityInfo(), json);
		}
		if (url.equals("/commoditySync/updateEx.bx")) {
			params.put(Commodity.field.getFIELD_NAME_ID(), String.valueOf(commodity.getID()));
		}
		String response = OkHttpUtil.post(url, params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Commodity commodity1 = new Commodity();
		commodity1 = (Commodity) commodity1.parse1(object.getString(BaseAction.KEY_Object));
		//
		return commodity1;
		// }else if(commodity.getType() == 1){
		//// throw new RuntimeException("该商品为组合商品");
		// // 创建组合商品
		// Commodity commodityCombine = new Commodity();
		// return commodityCombine;
		// }else if(commodity.getType() == 2){
		// throw new RuntimeException("该商品为多包装商品");
		// }else {
		// throw new RuntimeException("未知商品");
		// }
	}
}
