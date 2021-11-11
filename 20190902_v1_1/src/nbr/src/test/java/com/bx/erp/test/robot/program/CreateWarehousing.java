package com.bx.erp.test.robot.program;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.Shared;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONObject;

public class CreateWarehousing extends Program {
	protected List<Warehousing> warehousingList = new ArrayList<>();

	public List<Warehousing> getWarehousingList() {
		return warehousingList;
	}

	public CreateWarehousing(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		super(startDatetime, endDatetime, bRunInRandomMode);
	}

	/** 和采购活动共享的采购定单。其中可能有各种状态的采购订单 */
	protected List<PurchasingOrder> purchasingOrderList = new ArrayList<PurchasingOrder>();

	public void setPurchasingOrderList(List<PurchasingOrder> purchasingOrderList) {
		this.purchasingOrderList = purchasingOrderList;
	}

	@Override
	protected void generateReport() {
	}

	private static final int RETURN_OBJECT = 1;
	private static int INDEX = 0;

	public enum EnumOperationType {
		EOT_CreateWarehousing("Create Warehousing", INDEX++), //
		EOT_ApproveWarehousing("Approve Warehousing", INDEX++), //
		EOT_UpdateWarehousing("Update Warehousing", INDEX++), //
		EOT_RetrieveNExByFieldsExWarehousing("RetrieveNEx Warehousing", INDEX++), //
		EOT_Retrieve1ExWarehousing("Retrieve1Ex Warehousing", INDEX++), //
		EOT_DeleteWarehousing("Delete Warehousing", INDEX++); //

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
//			List<String> listRowTitle = mapBaseModels.get("入库单主表").get("ID"); // 标题
//			Map<String, List<String>> mapMachineMeal = mapBaseModels.get("nbr机器餐");
//			for (List<String> rowMachineMeal : mapMachineMeal.values()) {
//				if ("入库单主表".equals(rowMachineMeal.get(1))) {
//					ProgramUnit pu = new ProgramUnit();
//					List<String> ws = mapBaseModels.get(rowMachineMeal.get(1)).get(rowMachineMeal.get(2));
//
//					Map<String, Object> params = new HashMap<String, Object>();
//					for (int columnNO = 0; columnNO < listRowTitle.size(); columnNO++) {
//						if (ws.get(columnNO) == null || ws.get(columnNO).equals("null")) {
//							params.put(listRowTitle.get(columnNO), ws.get(columnNO));
//						} else {
//							params.put(listRowTitle.get(columnNO), "\"" + ws.get(columnNO) + "\"");
//						}
//					}
//					List<String> listFromTableTitle = mapBaseModels.get("入库单从表").get("ID"); // 标题
//					List<String> listWC = new ArrayList<String>();
//					for (List<String> wc : mapBaseModels.get("入库单从表").values()) {
//						if (wc.get(1).equals(ws.get(0))) {
//							Map<String, Object> mapFromTable = new HashMap<String, Object>();
//							for (int columnNO = 0; columnNO < wc.size(); columnNO++) {
//								if (wc.get(columnNO) == null || wc.get(columnNO).equals("null")) {
//									mapFromTable.put(listFromTableTitle.get(columnNO), wc.get(columnNO));
//								} else {
//									mapFromTable.put(listFromTableTitle.get(columnNO), "\"" + wc.get(columnNO) + "\"");
//								}
//							}
//							listWC.add(mapFromTable.toString());
//						}
//					}
//					params.put(Warehousing.field.getFIELD_NAME_listSlave1(), listWC);
//					params.put(Warehousing.field.getFIELD_NAME_messageID(), "0");
//					Warehousing warehousing = new Warehousing();
//					warehousing = (Warehousing) warehousing.parse1(params.toString());
//					pu.setBaseModelIn1(warehousing);
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
					Warehousing warehousing = new Warehousing();
					pu = queueIn.poll();
					//
					switch (EnumOperationType.values()[pu.getOperationType()]) {
					case EOT_CreateWarehousing:
						System.out.println("CreateWarehousing");
						warehousing = (Warehousing) pu.getBaseModelIn1();
						warehousing.setReturnObject(RETURN_OBJECT);
						warehousing.setPurchasingOrderID(warehousing.getPurchasingOrderID() + maxPurchasingOrderID);
						for (Object o : warehousing.getListSlave1()) {
							WarehousingCommodity wc = (WarehousingCommodity) o;
							wc.setCommodityID(wc.getCommodityID() + maxCommodityID);
							wc.setBarcodeID(wc.getBarcodeID() + maxBarcodesID);
						}
						Warehousing createW = warehousingCUA("/warehousing/createEx.bx", warehousing);
						if (maxWarehousingID == 0) {
							maxWarehousingID = createW.getID() - 1;
							WarehousingCommodity wc = (WarehousingCommodity) createW.getListSlave1().get(0);
							maxWarehousingCommodityID = wc.getID() - 1;
						}
						pu.setBaseModelOut1(createW);
						queueOut.add(pu);

						break;
					case EOT_ApproveWarehousing:
						System.out.println("ApproveWarehousing");
						warehousing = (Warehousing) pu.getBaseModelIn1();
						warehousing.setID(warehousing.getID() + maxWarehousingID);
						for (Object o : warehousing.getListSlave1()) {
							WarehousingCommodity wc = (WarehousingCommodity) o;
							wc.setCommodityID(wc.getCommodityID() + maxCommodityID);
							wc.setBarcodeID(wc.getBarcodeID() + maxBarcodesID);
						}
						Warehousing approveW = warehousingCUA("/warehousing/approveEx.bx", warehousing);
						pu.setBaseModelOut1(approveW);
						queueOut.add(pu);

						break;
					case EOT_UpdateWarehousing:
						warehousing = (Warehousing) pu.getBaseModelOut1();
						// 这里set需要更改的参数
						Warehousing updateW = warehousingCUA("/warehousing/updateEx.bx", warehousing);
						pu.setBaseModelOut1(updateW);
						queueOut.add(pu);
						// 下面是审核入库单的
						pu.setNo(2);
						pu.setOperationType(EnumOperationType.EOT_ApproveWarehousing.getIndex());
						feed(pu, programs[EnumProgramType.EPT_CreateWarehousing.getIndex()]);

						break;
					case EOT_RetrieveNExByFieldsExWarehousing:
						warehousing = (Warehousing) pu.getBaseModelOut1();
						// 这里set需要模糊查询的参数，不需要查询的参数set为-1或者null
						List<BaseModel> listW = warehousingRNByFieldsEx(warehousing);
						pu.setListBaseModelOut1(listW);
						queueOut.add(pu);

						break;
					case EOT_Retrieve1ExWarehousing:
						warehousing = (Warehousing) pu.getBaseModelOut1();
						// 这里set需要更改的参数
						Warehousing retrieve1ExW = warehousingR1Ex(warehousing);
						pu.setBaseModelOut1(retrieve1ExW);
						queueOut.add(pu);

						break;
					default:
						warehousing = (Warehousing) pu.getBaseModelOut1();
						warehousingD(warehousing);

						break;
					}
					// 
//					ServerHandler.nextActivity(currentProgramUnitNO);
				} else {
					break;
				}
			}
		} else {
			Random r = new Random();
			if (canRunNow(currentDatetime)) {
				// 1、找出一个可以入库的采购订单
				List<PurchasingOrder> removeListPurchasingOrder = new ArrayList<PurchasingOrder>();
				for (PurchasingOrder po : purchasingOrderList) {
					if (r.nextBoolean()) { // 使用随机判断是否入库，不是每次活动都必须做创建入库单的
						System.out.println("本次随机到不创建这张入库单");
						continue;
					}
					// 2、判断该采购订单是否已经全部入库，是则记录下来，在循环结束后删除
					PurchasingOrder purchasingOrder = new PurchasingOrder();
					try {
						purchasingOrder = retrieve1ExPurchasingOrder(po);
						po.setStatus(purchasingOrder.getStatus());
					} catch (Exception e) {
						e.printStackTrace();
						sbError.append("\r\n查询采购订单失败！采购订单ID=" + po.getID());
						System.out.println("查询采购订单失败！采购订单ID=" + po.getID());
						return false;
					}
					if (po.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex()) {
						removeListPurchasingOrder.add(po);
						continue;
					}
					if (po.getWarehousingNO() < PurchasingOrder.MAX_WarehousingNO // 一张采购订单不能无限次入库。不能让所有采购订单都全部入库，因为夜间任务有一个采购超时检查需要生成采购超时的消息
							&& (po.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex() || po.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex())) {
						// 3、对其进行入库，入库的商品和数量随机。
						List<WarehousingCommodity> listWC = getWarehousingCommoidtyList(r.nextBoolean(), po);
						if (listWC == null || listWC.size() == 0) {
							System.out.println("入库商品不存在，这张入库单对应的采购订单是:" + po);
							continue;
						}
						//
						Warehousing warehousing = new Warehousing();
						warehousing.setListSlave1(listWC);
						warehousing.setStaffID(Shared.BossID);
						warehousing.setPurchasingOrderID(po.getID());
						warehousing.setWarehouseID(1); // TODO 仓库就只有一个，是否需要设shared的全局常量？
						warehousing.setProviderID(1);
						warehousing.setReturnObject(1);
						//
						try {
							Warehousing warehousing1 = warehousingCUA("/warehousing/createEx.bx", warehousing);
							// 4、记录入库到一个列表中，将来用于审核、生成report
							warehousingList.add(warehousing1);// ...
							// 5、更新此采购订单的入库次数
							po.setWarehousingNO(po.getWarehousingNO() + 1);
						} catch (Exception e) {
							e.printStackTrace();
							sbError.append("\r\n创建入库单失败！入库单=" + warehousing);
							System.out.println("创建入库单失败！入库单=" + warehousing);
							return false;
						}
					}
				}
				// 删除已全部入库的采购订单
				for (PurchasingOrder purchasingOrder : removeListPurchasingOrder) {
					purchasingOrderList.remove(purchasingOrder);
				}
			}
		}
		return true;
	}

	/** 必须存在已经审批或部分入库的采购订单。 */
	@Override
	protected boolean canRunNow(Date currentDatetime) {
		if (purchasingOrderList == null || purchasingOrderList.size() == 0) {
			return false;
		}
		boolean bExist = false;// 存在可以入库的采购订单
		for (PurchasingOrder po : purchasingOrderList) {
			if (po.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex() || po.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex()) {
				bExist = true;
				break;
			}
		}
		if (!bExist) {
			return false;
		}

		return true;
	}

	protected PurchasingOrder retrieve1ExPurchasingOrder(PurchasingOrder po) throws Exception, UnsupportedEncodingException {
		Map<String, String> params = new HashMap<>();
		params.put(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID()));
		String response = OkHttpUtil.get("/purchasingOrder/retrieve1Ex.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		PurchasingOrder purchasingOrder1 = new PurchasingOrder();
		purchasingOrder1 = (PurchasingOrder) purchasingOrder1.parse1(object.getString(BaseAction.KEY_Object));

		return purchasingOrder1;
	}

	/** 创建、修改、审核 */
	public static Warehousing warehousingCUA(String url, Warehousing warehousing) throws Exception {
		StringBuilder commIDs = new StringBuilder();
		StringBuilder commNOs = new StringBuilder();
		StringBuilder commPrices = new StringBuilder();
		StringBuilder amounts = new StringBuilder();
		StringBuilder barcodeIDs = new StringBuilder();
		for (Object object : warehousing.getListSlave1()) {
			WarehousingCommodity wc = (WarehousingCommodity) object;
			commIDs.append(wc.getCommodityID() + ",");
			commNOs.append(wc.getNO() + ",");
			commPrices.append(wc.getPrice() + ",");
			amounts.append(wc.getAmount() + ",");
			barcodeIDs.append(wc.getBarcodeID() + ",");
		}
		Map<String, String> params = new HashMap<>();
		if (!url.equals("/warehousing/createEx.bx")) {
			params.put(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()));
		}
		params.put(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID()));
		params.put(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(warehousing.getProviderID()));
		params.put(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID()));
		params.put("commIDs", String.valueOf(commIDs));
		params.put("commNOs", String.valueOf(commNOs));
		params.put("commPrices", String.valueOf(commPrices));
		params.put("amounts", String.valueOf(amounts));
		params.put("barcodeIDs", String.valueOf(barcodeIDs));
		String response = OkHttpUtil.post(url, params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Warehousing warehousing1 = new Warehousing();
		warehousing1 = (Warehousing) warehousing1.parse1(object.getString(BaseAction.KEY_Object));
		return warehousing1;
	}

//	@SuppressWarnings("unchecked")
	public static List<BaseModel> warehousingRNByFieldsEx(Warehousing warehousing) {
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		String date1 = sdf3.format(warehousing.getDate1());
		String date2 = sdf3.format(warehousing.getDate2());
		//
		Map<String, String> params = new HashMap<>();
		params.put(Warehousing.field.getFIELD_NAME_queryKeyword(), warehousing.getQueryKeyword());
		params.put(Warehousing.field.getFIELD_NAME_status(), String.valueOf(warehousing.getStatus()));
		params.put(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID()));
		params.put(Warehousing.field.getFIELD_NAME_staffID(), String.valueOf(warehousing.getStaffID()));
		params.put(Warehousing.field.getFIELD_NAME_date1(), date1);
		params.put(Warehousing.field.getFIELD_NAME_date2(), date2);
		String response = OkHttpUtil.post("/warehousing/retrieveNByFieldsEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Warehousing warehousing1 = new Warehousing();
		List<BaseModel> listWarehousing = (List<BaseModel>) warehousing1.parseN(object.getString("warehousingList"));
		//
		return listWarehousing;
	}

	public static Warehousing warehousingR1Ex(Warehousing warehousing) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()));
		params.put(Warehousing.field.getFIELD_NAME_status(), String.valueOf(warehousing.getStatus()));
		String response = OkHttpUtil.get("/warehousing/retrieve1Ex.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Warehousing warehousing1 = new Warehousing();
		warehousing1 = (Warehousing) warehousing1.parse1(object.getString(BaseAction.KEY_Object));
		return warehousing1;
	}

	public static void warehousingD(Warehousing warehousing) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()));
		//
		OkHttpUtil.get("/warehousing/deleteEx.bx", params);
	}

	/** 从采购订单purchasingOrderInfo中随机选取商品及其数量
	 * 
	 * @param bAllWarehousing
	 *            true,把剩下的所有商品及其剩下的所有数量全部入库 */
	public List<WarehousingCommodity> getWarehousingCommoidtyList(boolean bAllWarehousing, PurchasingOrder purchasingOrder) {
		Random r = new Random();
		List<WarehousingCommodity> warehousingCommodities = new ArrayList<WarehousingCommodity>();

		for (Object o : purchasingOrder.getListSlave1()) {
			PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) o;
			WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
			warehousingCommodity.setShelfLife(1);// ....保质期
			if (bAllWarehousing) { // 全部入库
				warehousingCommodity.setNO(getRemainingNO(purchasingOrderCommodity.getCommodityID(), purchasingOrder, warehousingList));//
				warehousingCommodity.setPrice(purchasingOrderCommodity.getPriceSuggestion() + (r.nextBoolean() ? 1.000000f : 0.000000f));// 入库价格变动消息
			} else {
				warehousingCommodity.setNO(r.nextInt(purchasingOrderCommodity.getCommodityNO()) + 2);// 入库数量可以大于采购数量,+ 2(或加其它>2的数)可以令这种情况低概率发生
				warehousingCommodity.setPrice(purchasingOrderCommodity.getPriceSuggestion());// 入库价格变动消息
			}

			if (warehousingCommodity.getNO() != 0) {
				warehousingCommodity.setCommodityID(purchasingOrderCommodity.getCommodityID());
				warehousingCommodity.setBarcodeID(purchasingOrderCommodity.getBarcodeID());
				warehousingCommodity.setAmount(purchasingOrderCommodity.getCommodityNO() * purchasingOrderCommodity.getPriceSuggestion());
				warehousingCommodity.setCommodityName(purchasingOrderCommodity.getCommodityName());
				warehousingCommodity.setCommodityID(purchasingOrderCommodity.getCommodityID());
				warehousingCommodity.setPrice(purchasingOrderCommodity.getPriceSuggestion());// ..该入库的价格暂与采购价保持一致
				warehousingCommodities.add(warehousingCommodity);
			} else {
				return null;
			}
		}

		return warehousingCommodities;
	}

	/** 计算一个商品还剩下多少数量未入库
	 *
	 * @param purchasingOrder
	 *            采购订单信息
	 * @param listWarehousing
	 *            所有入库信息 */
	private int getRemainingNO(int commodityID, PurchasingOrder purchasingOrder, List<Warehousing> listWarehousing) {
		int remainingNO = 0;
		for (Object o : purchasingOrder.getListSlave1()) {
			PurchasingOrderCommodity poc = (PurchasingOrderCommodity) o;
			if (poc.getCommodityID() == commodityID) {
				remainingNO = poc.getCommodityNO();
			}
		}

		int totalWarehousingNO = 0;
		for (Warehousing w : listWarehousing) {
			if (w == null) {
				System.out.println("该入库单为空！");
				continue;
			}
			if (w.getPurchasingOrderID() == purchasingOrder.getID()) {
				for (Object o : w.getListSlave1()) {
					WarehousingCommodity wc = (WarehousingCommodity) o;
					if (wc.getID() == commodityID) {
						totalWarehousingNO += wc.getNO();
					}
				}

			}
		}
		return remainingNO - totalWarehousingNO;
	}
}
