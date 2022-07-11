package com.bx.erp.test.robot.program;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONObject;

//...将来重构成，看哪些商品销量好，就优先采购哪些
public class CreatePurchasingOrder extends Program {
	/** 记录当前周已经采购了多少次 */
	private int iPurchasingNOOfThisWeek;

	public CreatePurchasingOrder(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		super(startDatetime, endDatetime, bRunInRandomMode);
	}

	/** 和采购活动共享的采购定单。其中可能有各种状态的采购订单 */
	protected List<Commodity> commodityList = new ArrayList<Commodity>();

	public void setCommodityList(List<Commodity> commodityList) {
		this.commodityList = commodityList;
	}

	protected int providerID = 1;

	/** 记录机器人本次测试的所有采购 */
	public List<PurchasingOrder> listPO = new ArrayList<PurchasingOrder>();

	/** 每一个顾客一次采购的商品的最大种类数。一个种类由一个商品ID标识 */
	private final int MAX_NOOfCommodityPerPurchasing = 20;

	/** 每一个店员一次采购的单个商品的最大数目 */
	private final int MAX_NOPerCommodity = 10;

	/** 每一个店员一次采购的单个商品的最大采购价 */
	private final double MAX_pricePerCommodity = 10.000000d;

	public void setMaxPurchasingPerWeek(int maxPurchasingPerWeek) {
		this.maxPurchasingPerWeek = maxPurchasingPerWeek;
	}

	/** 每个礼拜最多采购多少次 */
	protected int maxPurchasingPerWeek = 10;

	@Override
	protected void generateReport() {

	}

	public List<PurchasingOrder> getListPO() {
		return listPO;
	}

	private static int INDEX = 0;

	public enum EnumOperationType {
		EOT_CreatePurchasingOrder("Create PurchasingOrder", INDEX++), //
		EOT_ApprovePurchasingOrder("Approve PurchasingOrder", INDEX++), //
		EOT_UpdatePurchasingOrder("Update PurchasingOrder", INDEX++), //
		EOT_RetrieveNExPurchasingOrder("RetrieveNEx PurchasingOrder", INDEX++), //
		EOT_Retrieve1ExPurchasingOrder("Retrieve1Ex PurchasingOrder", INDEX++), //
		EOT_DeletePurchasingOrder("Delete PurchasingOrder", INDEX++); //

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

//	@Override
//	protected boolean doLoadProgramUnit() {
//		if (!bRunInRandomMode) {
//			List<String> listRowTitle = mapBaseModels.get("采购订单主表").get("ID"); // 标题
//			Map<String, List<String>> mapMachineMeal = mapBaseModels.get("nbr机器餐");
//			for (List<String> rowMachineMeal : mapMachineMeal.values()) {
//				if ("采购订单主表".equals(rowMachineMeal.get(1))) {
//					ProgramUnit pu = new ProgramUnit();
//					List<String> po = mapBaseModels.get(rowMachineMeal.get(1)).get(rowMachineMeal.get(2));
//
//					Map<String, Object> params = new HashMap<String, Object>();
//					for (int columnNO = 0; columnNO < listRowTitle.size(); columnNO++) {
//						if (po.get(columnNO) == null || po.get(columnNO).equals("null")) {
//							params.put(listRowTitle.get(columnNO), po.get(columnNO));
//						} else {
//							params.put(listRowTitle.get(columnNO), "\"" + po.get(columnNO) + "\"");
//						}
//					}
//					List<String> listFromTableTitle = mapBaseModels.get("采购从表").get("ID"); // 标题
//					List<String> listPOC = new ArrayList<String>();
//					for (List<String> poc : mapBaseModels.get("采购从表").values()) {
//						if (poc.get(1).equals(po.get(0))) {
//							Map<String, Object> mapFromTable = new HashMap<String, Object>();
//							for (int columnNO = 0; columnNO < poc.size(); columnNO++) {
//								if (poc.get(columnNO) == null || poc.get(columnNO).equals("null")) {
//									mapFromTable.put(listFromTableTitle.get(columnNO), poc.get(columnNO));
//								} else {
//									mapFromTable.put(listFromTableTitle.get(columnNO), "\"" + poc.get(columnNO) + "\"");
//								}
//							}
//							listPOC.add(mapFromTable.toString());
//						}
//					}
//					params.put(PurchasingOrder.field.getFIELD_NAME_listSlave1(), listPOC);
//					PurchasingOrder purchasingOrder = new PurchasingOrder();
//					purchasingOrder = (PurchasingOrder) purchasingOrder.parse1(params.toString());
//					pu.setBaseModelIn1(purchasingOrder);
//					pu.setNo(Integer.parseInt(rowMachineMeal.get(0)));
//					pu.setOperationType(Integer.parseInt(rowMachineMeal.get(3)));
//					queueIn.offer(pu);
//				}
//			}
//		}
//		return true;
//	}

	@Override
	public boolean run(Date currentDatetime, StringBuilder sbError, final Program[] programs) throws Exception {
		if (!bRunInRandomMode) {
			while (!queueIn.isEmpty()) {
				ProgramUnit pu = queueIn.peek();
				if (pu.getNo() == currentProgramUnitNO) {
					PurchasingOrder purchasingOrder = new PurchasingOrder();
					PurchasingOrder po = new PurchasingOrder();
					pu = queueIn.poll();
					//
					switch (EnumOperationType.values()[pu.getOperationType()]) {
					case EOT_CreatePurchasingOrder:
						System.out.println("CreatePurchasingOrder");
						purchasingOrder = (PurchasingOrder) pu.getBaseModelIn1();
						for (Object o : purchasingOrder.getListSlave1()) {
							PurchasingOrderCommodity poc = (PurchasingOrderCommodity) o;
							poc.setCommodityID(poc.getCommodityID() + maxCommodityID);
							poc.setBarcodeID(poc.getBarcodeID() + maxBarcodesID);
						}
						po = purchasingOrderCUA("/purchasingOrder/createEx.bx", purchasingOrder);
						if (maxPurchasingOrderID == 0) {
							maxPurchasingOrderID = po.getID() - 1;
							PurchasingOrderCommodity poc = (PurchasingOrderCommodity) po.getListSlave1().get(0);
							maxPurchasingOrderCommodityID = poc.getID() - 1;
						}
						pu.setBaseModelOut1(po);
						queueOut.add(pu);

						break;
					case EOT_ApprovePurchasingOrder:
						System.out.println("ApprovePurchasingOrder");
						purchasingOrder = (PurchasingOrder) pu.getBaseModelIn1();
						purchasingOrder.setID(purchasingOrder.getID() + maxPurchasingOrderID);
						for (Object o : purchasingOrder.getListSlave1()) {
							PurchasingOrderCommodity poc = (PurchasingOrderCommodity) o;
							poc.setCommodityID(poc.getCommodityID() + maxCommodityID);
							poc.setBarcodeID(poc.getBarcodeID() + maxBarcodesID);
						}
						//
						po = purchasingOrderCUA("/purchasingOrder/approveEx.bx", purchasingOrder);
						pu.setBaseModelOut1(po);
						queueOut.add(pu);

						break;
					case EOT_UpdatePurchasingOrder:
						purchasingOrder = (PurchasingOrder) pu.getBaseModelIn1();
						// 这里set需要更改的参数（目前没有参数，所以先这样）
						po = purchasingOrderCUA("/purchasingOrder/updateEx.bx", purchasingOrder);
						pu.setBaseModelOut1(po);
						queueOut.add(pu);

						break;
					case EOT_RetrieveNExPurchasingOrder:
						purchasingOrder = (PurchasingOrder) pu.getBaseModelIn1();
						// 这里set需要模糊查询的参数，不需要查询的参数set为-1或者null
						List<BaseModel> listPO = purchasingOrderRNEx(purchasingOrder);
						pu.setListBaseModelOut1(listPO);
						queueOut.add(pu);

						break;
					case EOT_Retrieve1ExPurchasingOrder:
						purchasingOrder = (PurchasingOrder) pu.getBaseModelIn1();
						// 这里set需要查询的采购订单ID
						po = purchasingOrderR1Ex(purchasingOrder);
						pu.setBaseModelOut1(po);
						queueOut.add(pu);

						break;
					default:
						purchasingOrder = (PurchasingOrder) pu.getBaseModelOut1();
						purchasingOrdeD(purchasingOrder);

						break;
					}
//					counter++;
//					if(counter == 4) {
//						Header header = new Header();
//						header.setActivitySequence(4);
//						header.setCommand(EnumCommandType.ECT_SyncData.getIndex());
//						header.setBodyLength(0);
//						StringBuilder sb = new StringBuilder();
//						sb.append(header.toBufferString());
//						ShopRobotTest.clientSession.write(sb.toString());
//					}
//					ServerHandler.nextActivity(currentProgramUnitNO);
				} else {
					break;
				}
			}
		} else {
			Random r = new Random();
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
			System.out.println("当前采购数量：" + iPurchasingNOOfThisWeek + "    时间为：" + sdf.format(currentDatetime));
			if (canRunNow(currentDatetime)) {
				if (r.nextBoolean()) {
					System.out.println("本次不创建采购订单，时间：" + sdf.format(currentDatetime));
					return true;
				}
				List<Commodity> listComm = Shared.getCommodityList(MAX_NOOfCommodityPerPurchasing, sbError);
				if (listComm == null || listComm.size() == 0) {
					sbError.append("查找商品失败！");
					return false;
				}

				PurchasingOrder purchasingOrder = new PurchasingOrder();
				purchasingOrder.setProviderID(providerID);
				purchasingOrder.setStaffID(Shared.BossID);// ...
				List<PurchasingOrderCommodity> purchasingOrderCommodities = new ArrayList<PurchasingOrderCommodity>();
				purchasingOrderCommodities = getPurchasingOrderCommodity(listComm, MAX_NOPerCommodity, MAX_pricePerCommodity);
				if (purchasingOrderCommodities == null) {
					sbError.append("获取采购商品失败！");
					return false;
				}

				purchasingOrder.setListSlave1(purchasingOrderCommodities);

				PurchasingOrder purchasingOrder1;
				try {
					purchasingOrder1 = purchasingOrderCUA("/purchasingOrder/createEx.bx", purchasingOrder);
				} catch (Exception e) {
					e.printStackTrace();
					sbError.append("创建采购订单失败");
					return false;
				}

				if (purchasingOrder1 != null) {
					listPO.add(purchasingOrder1);
					System.out.println("现在采购的日期为：" + sdf.format(currentDatetime));
					iPurchasingNOOfThisWeek++;
					nextScheduledRunDatetime = DatetimeUtil.getDays(currentDatetime, r.nextInt(2));
				}
			}
		}
		return true;
	}

	/** 第几周 */
	protected int weekNO = 1;

	@Override
	protected boolean canRunNow(Date currentDatetime) {
		if (DatetimeUtil.isPastWeek(currentDatetime, startDatetime, weekNO)) {
			weekNO++;
			iPurchasingNOOfThisWeek = 0;
		}
		// 频率是N天一次。所以要判断是否过了N天并且当前采购数量不大于最多采购数目
		if (iPurchasingNOOfThisWeek < maxPurchasingPerWeek) {
			if (iPurchasingNOOfThisWeek == 0) {
				Random r = new Random();
				nextScheduledRunDatetime = DatetimeUtil.getDays(currentDatetime, r.nextInt(3));
				return true;
			}
			if (currentDatetime.getTime() >= nextScheduledRunDatetime.getTime()) {
				return true;
			}
		}
		return false;
	}

	/** 适用于创建、修改、审核 */
	public static PurchasingOrder purchasingOrderCUA(String url, PurchasingOrder purchasingOrder) throws Exception {
		StringBuilder commIDs = new StringBuilder();
		StringBuilder NOs = new StringBuilder();
		StringBuilder priceSuggestions = new StringBuilder();
		StringBuilder barcodeIDs = new StringBuilder();
		for (Object object : purchasingOrder.getListSlave1()) {
			PurchasingOrderCommodity poc = (PurchasingOrderCommodity) object;
			commIDs.append(poc.getCommodityID() + ",");
			NOs.append(poc.getCommodityNO() + ",");
			priceSuggestions.append(poc.getPriceSuggestion() + ",");
			barcodeIDs.append(poc.getBarcodeID() + ",");
		}
		//
		Map<String, String> params = new HashMap<>();
		if (!url.equals("/purchasingOrder/createEx.bx")) {
			params.put(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID()));
		}
		params.put(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark());
		params.put(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()));
		params.put("commIDs", String.valueOf(commIDs));
		params.put("NOs", String.valueOf(NOs));
		params.put("priceSuggestions", String.valueOf(priceSuggestions));
		params.put("barcodeIDs", String.valueOf(barcodeIDs));
		String response = OkHttpUtil.post(url, params);
		//
		JSONObject object = JSONObject.fromObject(response);
		PurchasingOrder purchasingOrder1 = new PurchasingOrder();
		purchasingOrder1 = (PurchasingOrder) purchasingOrder1.parse1(object.getString(BaseAction.KEY_Object));
		//
		return purchasingOrder1;
	}

	/** 适用于模糊查询 */
	public static List<BaseModel> purchasingOrderRNEx(PurchasingOrder purchasingOrder) throws Exception {
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		String date1 = sdf3.format(purchasingOrder.getDate1());
		String date2 = sdf3.format(purchasingOrder.getDate2());
		//
		Map<String, String> params = new HashMap<>();
		params.put(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(purchasingOrder.getStatus()));
		params.put(PurchasingOrder.field.getFIELD_NAME_staffID(), String.valueOf(purchasingOrder.getStaffID()));
		params.put(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID()));
		params.put(PurchasingOrder.field.getFIELD_NAME_date1(), date1);
		params.put(PurchasingOrder.field.getFIELD_NAME_date2(), date2);
		params.put(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), purchasingOrder.getQueryKeyword());
		//
		String response = OkHttpUtil.post("/purchasingOrder/retrieveNEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);

		PurchasingOrder purchasingOrder1 = new PurchasingOrder();
		List<BaseModel> listPurchasingOrder = (List<BaseModel>) purchasingOrder1.parseN(object.getString(BaseAction.KEY_ObjectList));
		//
		return listPurchasingOrder;
	}

	/** 适用于删除 */
	public static void purchasingOrdeD(PurchasingOrder purchasingOrder) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID()));
		//
		OkHttpUtil.get("/purchasingOrder/deleteEx.bx", params);
	}

	/** 适用于查询一条采购订单 */
	public static PurchasingOrder purchasingOrderR1Ex(PurchasingOrder purchasingOrder) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID()));
		//
		String response = OkHttpUtil.get("/purchasingOrder/retrieve1Ex.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		PurchasingOrder purchasingOrder1 = new PurchasingOrder();
		purchasingOrder1 = (PurchasingOrder) purchasingOrder1.parse1(object.getString(BaseAction.KEY_Object));
		//
		return purchasingOrder1;
	}

	/** 购买商品随机
	 *
	 * @param listComm
	 *            采购的商品的列表
	 * @param purchasingNO
	 *            在随机机器人时为采购一个商品的最大数量， 在特定机器人时为采购一个商品的数量
	 * @param purchasingPrice
	 *            在随机机器人时为采购一个商品的最大采购价， 在特定机器人时为采购一个商品的采购价
	 * @throws Exception
	 */
	public static List<PurchasingOrderCommodity> getPurchasingOrderCommodity(List<Commodity> listComm, int purchasingNO, double purchasingPrice) {
		Random r = new Random();
		List<PurchasingOrderCommodity> purchasingOrderCommodityList = new ArrayList<PurchasingOrderCommodity>();
		double max = GeneralUtil.round(purchasingPrice);
		double min = 1.000000d;
		for (Commodity commodity : listComm) {
			int purchasingOrderCommodityNO = r.nextInt(purchasingNO) + 1;
			double priceSuggestion = GeneralUtil.round(GeneralUtil.sum(min, GeneralUtil.mul(r.nextDouble(), (GeneralUtil.sub(max, min)))), 2); // 大于等于min小于max
			PurchasingOrderCommodity purchasingOrderCommodity = new PurchasingOrderCommodity();
			purchasingOrderCommodity.setCommodityID(commodity.getID());
			purchasingOrderCommodity.setCommodityName(commodity.getName());
			purchasingOrderCommodity.setCommodityNO(purchasingOrderCommodityNO);
			purchasingOrderCommodity.setPriceSuggestion(priceSuggestion);
			purchasingOrderCommodity.setBarcodeID(commodity.getBarcodeID());
			//
			purchasingOrderCommodityList.add(purchasingOrderCommodity);
		}

		return purchasingOrderCommodityList;
	}
}
