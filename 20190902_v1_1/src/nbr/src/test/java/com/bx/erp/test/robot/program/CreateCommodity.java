package com.bx.erp.test.robot.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CreateCommodity extends Program {
	public List<Commodity> listCommodity = new ArrayList<Commodity>();

	public List<Commodity> getListCommodity() {
		return listCommodity;
	}

	public CreateCommodity(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		super(startDatetime, endDatetime, bRunInRandomMode);
	}

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

	// @Override
	// protected boolean doLoadProgramUnit() {
	// if (!bRunInRandomMode) {
	// List<String> listRowTitle = mapBaseModels.get("商品信息").get("ID"); // 标题
	// Map<String, List<String>> mapMachineMeal = mapBaseModels.get("nbr机器餐");
	// for (List<String> rowMachineMeal : mapMachineMeal.values()) {
	// if ("商品信息".equals(rowMachineMeal.get(1))) {
	// ProgramUnit pu = new ProgramUnit();
	// List<String> comm =
	// mapBaseModels.get(rowMachineMeal.get(1)).get(rowMachineMeal.get(2));
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// for (int columnNO = 0; columnNO < listRowTitle.size(); columnNO++) {
	// if (comm.get(columnNO) == null || comm.get(columnNO).equals("null")) {
	// params.put(listRowTitle.get(columnNO), comm.get(columnNO));
	// } else {
	// params.put(listRowTitle.get(columnNO), "\"" + comm.get(columnNO) + "\"");
	// }
	// }
	// String nameReplace = ((String)
	// params.get(Commodity.field.getFIELD_NAME_name())).replaceAll("\"", "");
	// params.put(Commodity.field.getFIELD_NAME_name(), "\"" + (nameReplace +
	// System.currentTimeMillis() % 1000000) + "\"");
	// // 使用for循环遍历，找到对应商品ID的条形码
	// for (List<String> bar : mapBaseModels.get("条形码").values()) {
	// if (bar.get(1).equals(comm.get(0))) {
	// params.put(Commodity.field.getFIELD_NAME_barcodes(), bar.get(2));
	// break;
	// }
	// }
	// //
	// params.put(Commodity.field.getFIELD_NAME_operatorStaffID(), "0");
	// params.put(Commodity.field.getFIELD_NAME_syncType(), "\"\"");
	// params.put(Commodity.field.getFIELD_NAME_subCommodityInfo(), "\"\"");
	// params.put(Commodity.field.getFIELD_NAME_packageUnitName(), "\"\"");
	// params.put(Commodity.field.getFIELD_NAME_startValueRemark(), "\"\"");
	// params.put(Commodity.field.getFIELD_NAME_listSlave1(), "[]");
	// Commodity commodity = new Commodity();
	// commodity = (Commodity) commodity.parse1(params.toString());
	// commodity.setReturnObject(RETURN_OBJECT);
	// commodity.setProviderIDs("1");
	// commodity.setMultiPackagingInfo(commodity.getBarcodes() + ";" +
	// commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() +
	// ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";"
	// + commodity.getPriceWholesale() + ";" + commodity.getName() + ";");
	// pu.setBaseModelIn1(commodity);
	// pu.setNo(Integer.parseInt(rowMachineMeal.get(0)));
	// pu.setOperationType(Integer.parseInt(rowMachineMeal.get(3)));
	// queueIn.offer(pu);
	// }
	// }
	// }
	// return true;
	// }

	public static final int RETURN_OBJECT = 1;

	@Override
	public boolean run(Date currentDatetime, StringBuilder sbError, final Program[] programs) throws Exception {
		if (!bRunInRandomMode) {
			while (!queueIn.isEmpty()) {
				ProgramUnit pu = queueIn.peek();
				if (pu.getNo() == currentProgramUnitNO) {
					pu = queueIn.poll();
					Commodity commodity = new Commodity();
					Commodity com = new Commodity();
					List<Barcodes> listBarcodes = new ArrayList<Barcodes>();
					//
					switch (EnumOperationType.values()[pu.getOperationType()]) {
					case EOT_CreateCommodity:
						System.out.println("CreateCommodity");
						commodity = (Commodity) pu.getBaseModelIn1();
						com = createOrUpdateCommodity("/commoditySync/createEx.bx", commodity);
						listBarcodes = retrieveNBarcodeEx(com);
						com.setBarcodeID(listBarcodes.get(0).getID());
						//
						if (maxCommodityID == 0) {
							maxCommodityID = com.getID() - 1;
							maxBarcodesID = com.getBarcodeID() - 1;
						}
						//
						pu.setBaseModelOut1(com);
						queueOut.offer(pu);

						break;
					case EOT_UpdateCommodity:
						commodity = (Commodity) pu.getBaseModelIn1();
						// 这里set需要修改的参数
						com = createOrUpdateCommodity("/commoditySync/updateEx.bx", commodity);
						listBarcodes = retrieveNBarcodeEx(com);
						com.setBarcodeID(listBarcodes.get(0).getID());
						pu.setBaseModelOut1(com);
						queueOut.offer(pu);

						break;
					case EOT_RetrieveNExCommodity:
						commodity = (Commodity) pu.getBaseModelIn1();
						// 这里set需要模糊查询的参数，不查的set为-1
						Commodity comRNEx = retrieveNCommodityEx(commodity);
						pu.setBaseModelOut1(comRNEx);
						queueOut.offer(pu);

						break;
					case EOT_Retrieve1ExCommodity:
						commodity = (Commodity) pu.getBaseModelIn1();
						// 这里set需要查询的商品ID
						com = retrieve1CommodityEx(commodity);
						listBarcodes = retrieveNBarcodeEx(com);
						com.setBarcodeID(listBarcodes.get(0).getID());
						pu.setBaseModelOut1(com);
						queueOut.offer(pu);

						break;
					default:
						deleteCommodity((Commodity) pu.getBaseModelIn1());

						break;
					}
					// counter++;
				} else {
					break;
				}
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static List<Barcodes> retrieveNBarcodeEx(Commodity commodity) {
		Map<String, String> params1 = new HashMap<>();
		params1.put(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commodity.getID()));
		String responseBarcodes = OkHttpUtil.get("/barcodes/retrieveNEx.bx", params1);
		//
		JSONObject objectBarcodes = JSONObject.fromObject(responseBarcodes);
		JSONArray barJSONArray = objectBarcodes.getJSONArray("barcodesList");
		Barcodes barcodes1 = new Barcodes();
		List<Barcodes> listBarcodes = (List<Barcodes>) barcodes1.parseN(barJSONArray);
		return listBarcodes;
	}

	public static Commodity createOrUpdateCommodity(String url, Commodity commodity) {
		// if (commodity.getType() == 0) {
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
		if(url.equals("/commoditySync/updateEx.bx")) {
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

	public static Commodity retrieveNCommodityEx(Commodity commodity) {
		Map<String, String> params = new HashMap<>();
		params.put(Commodity.field.getFIELD_NAME_status(), String.valueOf(commodity.getStatus()));
		params.put(Commodity.field.getFIELD_NAME_type(), String.valueOf(commodity.getType()));
		params.put(Commodity.field.getFIELD_NAME_NO(), String.valueOf(commodity.getNO()));
		params.put(Commodity.field.getFIELD_NAME_categoryID(), String.valueOf(commodity.getCategoryID()));
		params.put(Commodity.field.getFIELD_NAME_brandID(), String.valueOf(commodity.getBrandID()));
		params.put(Commodity.field.getFIELD_NAME_queryKeyword(), commodity.getQueryKeyword());
		//
		String response = OkHttpUtil.post("/commodity/retrieveNEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		JSONArray commJSONArray = object.getJSONArray(BaseAction.KEY_ObjectList);
		JSONObject json = commJSONArray.getJSONObject(0);
		Commodity commodity1 = new Commodity();
		commodity1 = (Commodity) commodity1.parse1(json.getString("commodity"));
		//
		return commodity1;
	}

	/** 查询一个商品 */
	public static Commodity retrieve1CommodityEx(Commodity commodity) {
		Map<String, String> params = new HashMap<>();
		params.put(Commodity.field.getFIELD_NAME_ID(), String.valueOf(commodity.getID()));
		// retrieve1不返回对象
		String response = OkHttpUtil.get("/commodity/retrieve1Ex.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Commodity commodity1 = new Commodity();
		commodity1 = (Commodity) commodity1.parse1(object.getString(BaseAction.KEY_Object));
		//
		return commodity1;
	}

	/** 找到ID最大的零售单 */
	public static RetailTrade retrieveRetailTradeWithMaxID() {
		Map<String, String> params = new HashMap<>();
		params.put(RetailTrade.field.getFIELD_NAME_queryKeyword(), "");
		//
		String response = OkHttpUtil.post("/retailTrade/retrieveNEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		RetailTrade retailTrade = new RetailTrade();
		List<BaseModel> retailTradeList = (List<BaseModel>) retailTrade.parseN(object.getString(BaseAction.KEY_ObjectList));
		//
		return (RetailTrade) retailTradeList.get(0);
	}

	public static void deleteCommodity(Commodity commodity) {
		Map<String, String> params = new HashMap<>();
		params.put(Commodity.field.getFIELD_NAME_ID(), String.valueOf(commodity.getID()));
		OkHttpUtil.get("/commoditySync/deleteEx.bx", params);
	}

	@Override
	protected void generateReport() {

	}

	@Override
	protected boolean canRunNow(Date d) {
		return true;
	}

}
