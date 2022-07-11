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
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONObject;

public class CreateReturnCommoditySheet extends Program {
	/** 记录机器人本次测试的所有采购退货 */
	protected List<ReturnCommoditySheet> returnCommodityList = new ArrayList<>();

	public List<ReturnCommoditySheet> getListReturnCommodity() {
		return returnCommodityList;
	}

	/** 记录当前周已经采购退货了多少次 */
	private int iReturnCommoditySheetNOOfThisWeek;

	protected int providerID = 1;

	/** 每一个顾客一次采购退货的商品的最大种类数。一个种类由一个商品ID标识 */
	private final int MAX_NOOfCommodityPerReturnCommoditySheet = 20;

	/** 每一个店员一次采购退货的单个商品的最大数目 */
	private final int MAX_NOPerCommodity = 10;

	/** 每一个店员一次采购退货的单个商品的最大采购价 */
	private final double MAX_pricePerCommodity = 10.000000d;

	public void setMaxReturnCommoditySheetPerWeek(int maxReturnCommoditySheetPerWeek) {
		this.maxReturnCommoditySheetPerWeek = maxReturnCommoditySheetPerWeek;
	}

	/** 每个礼拜最多采购退货多少次 */
	protected int maxReturnCommoditySheetPerWeek = 6;

	/** 第几周 */
	protected int weekNO = 1;

	@Override
	protected boolean canRunNow(Date currentDatetime) {
		if (DatetimeUtil.isPastWeek(currentDatetime, startDatetime, weekNO)) {
			weekNO++;
			iReturnCommoditySheetNOOfThisWeek = 0;
		}
		// 频率是N天一次。所以要判断是否过了N天并且当前采购数量不大于最多采购数目
		if (iReturnCommoditySheetNOOfThisWeek < maxReturnCommoditySheetPerWeek) {
			if (iReturnCommoditySheetNOOfThisWeek == 0) {
				Random r = new Random();
				nextScheduledRunDatetime = DatetimeUtil.getDays(currentDatetime, r.nextInt(4));
				return true;
			}
			if (currentDatetime.getTime() >= nextScheduledRunDatetime.getTime()) {
				return true;
			}
		}
		return false;
	}

	public CreateReturnCommoditySheet(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		super(startDatetime, endDatetime, bRunInRandomMode);
	}

	@Override
	protected void generateReport() {

	}

	private static int INDEX = 0;

	public enum EnumOperationType {
		EOT_CreateReturnCommoditySheet("Create ReturnCommoditySheet", INDEX++), //
		EOT_ApproveReturnCommoditySheet("Approve ReturnCommoditySheet", INDEX++), //
		EOT_UpdateReturnCommoditySheet("Update ReturnCommoditySheet", INDEX++), //
		EOT_RetrieveNExReturnCommoditySheet("RetrieveNEx ReturnCommoditySheet", INDEX++), //
		EOT_Retrieve1ExReturnCommoditySheet("Retrieve1Ex ReturnCommoditySheet", INDEX++); //

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
//		return true;
//	}

	@Override
	public boolean run(Date currentDatetime, StringBuilder sbError, final Program[] programs) throws Exception {
		if (!bRunInRandomMode) {
			while (!queueIn.isEmpty()) {
				ProgramUnit pu = queueIn.peek();
				// if (pu.getNo() == xxx) {
				ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
				ReturnCommoditySheet rcs = new ReturnCommoditySheet();
				pu = queueIn.poll();
				//
				switch (EnumOperationType.values()[pu.getOperationType()]) {
				case EOT_CreateReturnCommoditySheet:
					Commodity com = (Commodity) pu.getBaseModelIn1();
					List<Commodity> listComm = new ArrayList<Commodity>();
					listComm.add(com);
					//
					rcs = returnCommoditySheetCUA("/returnCommoditySheet/createEx.bx", returnCommoditySheet);
					pu.setBaseModelOut1(rcs);

					break;
				case EOT_ApproveReturnCommoditySheet:
					returnCommoditySheet = (ReturnCommoditySheet) pu.getBaseModelIn1();
					//
					rcs = returnCommoditySheetCUA("/returnCommoditySheet/approveEx.bx", returnCommoditySheet);
					pu.setBaseModelOut1(rcs);
					queueOut.add(pu);

					break;
				case EOT_UpdateReturnCommoditySheet:
					returnCommoditySheet = (ReturnCommoditySheet) pu.getBaseModelIn1();
					// 这里set需要更改的参数（目前没有参数，所以先这样）
					rcs = returnCommoditySheetCUA("/returnCommoditySheet/updateEx.bx", returnCommoditySheet);
					pu.setBaseModelOut1(rcs);
					queueOut.add(pu);

					break;
				case EOT_RetrieveNExReturnCommoditySheet:
					returnCommoditySheet = (ReturnCommoditySheet) pu.getBaseModelIn1();
					// 这里set需要模糊查询的参数，不需要查询的参数set为-1或者null
					List<BaseModel> listPO = returnCommoditySheetRNEx(returnCommoditySheet);
					pu.setListBaseModelOut1(listPO);
					queueOut.add(pu);

					break;
				default:
					returnCommoditySheet = (ReturnCommoditySheet) pu.getBaseModelOut1();
					// 这里set需要查询的采购退货单ID
					rcs = returnCommoditySheetR1Ex(returnCommoditySheet);
					pu.setBaseModelOut1(rcs);
					queueOut.add(pu);

					break;
				}
				// } else {
				// break;
				// }
			}
		} else

		{
			Random r = new Random();
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
			System.out.println("当前采购退货数量：" + iReturnCommoditySheetNOOfThisWeek + "    时间为：" + sdf.format(currentDatetime));
			if (canRunNow(currentDatetime)) {
				if (r.nextBoolean()) {
					System.out.println("本次不创建采购订单，时间：" + sdf.format(currentDatetime));
					return true;
				}
				List<Commodity> listComm = Shared.getCommodityList(MAX_NOOfCommodityPerReturnCommoditySheet, sbError);
				if (listComm == null || listComm.size() == 0) {
					sbError.append("查找商品失败！");
					return false;
				}

				ReturnCommoditySheet returnCommoditySteet = new ReturnCommoditySheet();
				returnCommoditySteet.setProviderID(providerID);
				returnCommoditySteet.setStaffID(Shared.BossID);// ...
				List<ReturnCommoditySheetCommodity> listReturnCommoditySheetCommodity = getReturnCommoditySheetCommodity(listComm, MAX_NOPerCommodity, MAX_pricePerCommodity);
				if (listReturnCommoditySheetCommodity == null) {
					sbError.append("获取采购退货商品失败！");
					return false;
				}
				returnCommoditySteet.setListSlave1(listReturnCommoditySheetCommodity);
				try {
					ReturnCommoditySheet returnCommoditySteet1 = returnCommoditySheetCUA("/returnCommoditySheet/createEx.bx", returnCommoditySteet);
					if (returnCommoditySteet1 != null) {
						returnCommodityList.add(returnCommoditySteet1);
						System.out.println("现在采购退货的日期为：" + sdf.format(currentDatetime));
						iReturnCommoditySheetNOOfThisWeek++;
						nextScheduledRunDatetime = DatetimeUtil.getDays(currentDatetime, r.nextInt(2));
					}
				} catch (Exception e) {
					e.printStackTrace();
					sbError.append("创建采购退货单失败");
					return false;
				}

			}
		}

		return true;
	}

	protected ReturnCommoditySheet returnCommoditySheetCUA(String url, ReturnCommoditySheet returnCommoditySheet) throws Exception {
		StringBuilder commIDs = new StringBuilder();
		StringBuilder rcscNOs = new StringBuilder();
		StringBuilder commPrices = new StringBuilder();
		StringBuilder rcscSpecifications = new StringBuilder();
		StringBuilder barcodeIDs = new StringBuilder();
		for (Object object : returnCommoditySheet.getListSlave1()) {
			ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) object;
			commIDs.append(rcsc.getCommodityID() + ",");
			rcscNOs.append(rcsc.getNO() + ",");
			commPrices.append(rcsc.getPurchasingPrice() + ",");
			rcscSpecifications.append(rcsc.getSpecification() + ",");
			barcodeIDs.append(rcsc.getBarcodeID() + ",");
		}
		//
		Map<String, String> params = new HashMap<>();
		if (url.equals("/returnCommoditySheet/approveEx.bx")) {
			params.put(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), String.valueOf(returnCommoditySheet.getbReturnCommodityListIsModified()));
		}
		if (!url.equals("/returnCommoditySheet/createEx.bx")) {
			params.put(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(returnCommoditySheet.getID()));
		}
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(returnCommoditySheet.getProviderID()));
		params.put("commIDs", String.valueOf(commIDs));
		params.put("rcscNOs", String.valueOf(rcscNOs));
		params.put("commPrices", String.valueOf(commPrices));
		params.put("rcscSpecifications", String.valueOf(rcscSpecifications));
		params.put("barcodeIDs", String.valueOf(barcodeIDs));
		String response = OkHttpUtil.post(url, params);
		//
		JSONObject object = JSONObject.fromObject(response);
		ReturnCommoditySheet returnCommoditySheet1 = new ReturnCommoditySheet();
		ReturnCommoditySheet returnCommoditySheet2 = (ReturnCommoditySheet) returnCommoditySheet1.parse1(object.getString(BaseAction.KEY_Object));
		return returnCommoditySheet2;
	}

	@SuppressWarnings("unchecked")
	protected List<BaseModel> returnCommoditySheetRNEx(ReturnCommoditySheet returnCommoditySheet) {
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		String date1 = sdf3.format(returnCommoditySheet.getDate1());
		String date2 = sdf3.format(returnCommoditySheet.getDate2());
		//
		Map<String, String> params = new HashMap<>();
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(returnCommoditySheet.getStatus()));
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), returnCommoditySheet.getQueryKeyword());
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_staffID(), String.valueOf(returnCommoditySheet.getStaffID()));
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(returnCommoditySheet.getProviderID()));
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_date1(), date1);
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_date2(), date2);
		//
		String response = OkHttpUtil.post("returnCommoditySheet/retrieveNEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		//
		ReturnCommoditySheet returnCommoditySheet1 = new ReturnCommoditySheet();
		List<BaseModel> listReturnCommoditySheet = (List<BaseModel>) returnCommoditySheet1.parseN(object.getString(BaseAction.KEY_ObjectList));
		//
		return listReturnCommoditySheet;
	}

	protected ReturnCommoditySheet returnCommoditySheetR1Ex(ReturnCommoditySheet returnCommoditySheet) {
		Map<String, String> params = new HashMap<>();
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(returnCommoditySheet.getID()));
		String response = OkHttpUtil.get("/returnCommoditySheet/retrieve1Ex.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		ReturnCommoditySheet returnCommoditySheet1 = new ReturnCommoditySheet();
		ReturnCommoditySheet returnCommoditySheet2 = (ReturnCommoditySheet) returnCommoditySheet1.parse1(object.getString(BaseAction.KEY_Object));
		return returnCommoditySheet2;
	}

	/** 购买商品随机
	 *
	 * @param listComm
	 *            采购退货的商品的列表
	 * @param returnNO
	 *            在随机机器人时为采购退货一个商品的最大数量， 在特定机器人时为采购退货一个商品的数量
	 * @param purchasingPrice
	 *            在随机机器人时为采购退货一个商品的最大采购价， 在特定机器人时为采购退货一个商品的采购价
	 * @throws Exception
	 */
	public static List<ReturnCommoditySheetCommodity> getReturnCommoditySheetCommodity(List<Commodity> listComm, int returnNO, double purchasingPrice) throws Exception {
		List<ReturnCommoditySheetCommodity> returnCommoditySheetCommodityList = new ArrayList<ReturnCommoditySheetCommodity>();
		//
		for (Commodity commodity : listComm) {
			ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
			returnCommoditySheetCommodity.setCommodityID(commodity.getID());
			returnCommoditySheetCommodity.setCommodityName(commodity.getName());
			returnCommoditySheetCommodity.setNO(returnNO);
			returnCommoditySheetCommodity.setSpecification(commodity.getSpecification());
			returnCommoditySheetCommodity.setBarcodeID(commodity.getBarcodeID());
			returnCommoditySheetCommodity.setPurchasingPrice(purchasingPrice);
			//
			returnCommoditySheetCommodityList.add(returnCommoditySheetCommodity);
		}

		return returnCommoditySheetCommodityList;
	}

}
