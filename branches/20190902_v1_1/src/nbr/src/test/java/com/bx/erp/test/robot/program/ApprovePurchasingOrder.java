package com.bx.erp.test.robot.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.bx.erp.action.BaseAction;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.test.robot.protocol.BaseBuffer;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONObject;

public class ApprovePurchasingOrder extends Program {
	/** 和采购活动共享的采购定单。其中可能有各种状态的采购订单 */
	protected List<PurchasingOrder> purchasingOrderList;

	// 已审核的采购订单。
	protected List<PurchasingOrder> approvePurchasingOrderList;

	public List<PurchasingOrder> getApprovePurchasingOrderList() {
		return approvePurchasingOrderList;
	}

	public void setPurchasingOrderList(List<PurchasingOrder> purchasingOrderList) {
		this.purchasingOrderList = purchasingOrderList;
	}

	
	public List<PurchasingOrder> getPurchasingOrderList() {
		return purchasingOrderList;
	}

	public void setApprovePurchasingOrderList(List<PurchasingOrder> approvePurchasingOrderList) {
		this.approvePurchasingOrderList = approvePurchasingOrderList;
	}

	@Override
	protected boolean canRunNow(Date currentDatetime) {
		// 当purchasingOrderList.size 等于0 false。
		if (purchasingOrderList.size() > 0) {
			return true;
		}
		System.out.println("采购订单列表为空！");
		return false;
	}

	public ApprovePurchasingOrder(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		super(startDatetime, endDatetime, bRunInRandomMode);
		purchasingOrderList = new ArrayList<PurchasingOrder>();
		approvePurchasingOrderList = new ArrayList<PurchasingOrder>();
	}

	@Override
	protected void generateReport() {
	}
	
	public ApprovePurchasingOrder() {
		
	}

	@Override
	public boolean run(Date currentDatetime, StringBuilder sbError, final Program[] programs) throws Exception {
		if (bRunInRandomMode) {
			if (canRunNow(currentDatetime)) {
				// 获取生成采购订单
				List<PurchasingOrder> removeListPurchasingOrder = new ArrayList<PurchasingOrder>();
				for (PurchasingOrder purchasingOrder : purchasingOrderList) {
					Random r = new Random();
					if (r.nextBoolean()) {
						System.out.println("本次随机到不审核这张采购订单");
						continue;
					}
					if (purchasingOrder == null) {
						System.out.println("当前采购订单为空！不需要审核");
						return false;
					}
					System.out.println("待审核的采购订单：" + purchasingOrder);
					try {
						if (purchasingOrder.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex() || purchasingOrder.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex()
								|| purchasingOrder.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex()) {
							System.out.println("该采购订单已审核，无需重复审核！");
							continue;
						}
						// 审核一张采购订单。成功时,add进..approvePurchasingOrderList
						PurchasingOrder approvePO = approvePurchasingOrder(purchasingOrder);
						approvePurchasingOrderList.add(approvePO);
					} catch (Exception e) {
						e.printStackTrace();
						sbError.append("审核失败！采购订单ID=" + purchasingOrder.getID());
						System.out.println("审核采购订单失败！退货单ID=" + purchasingOrder.getID());
					}
					removeListPurchasingOrder.add(purchasingOrder);
				}
				// 审核一张采购订单。成功时purchasingOrderList.remove。
				for (PurchasingOrder purchasingOrder : removeListPurchasingOrder) {
					purchasingOrderList.remove(purchasingOrder);
				}
			}
		}
		return true;
	}

	private PurchasingOrder approvePurchasingOrder(PurchasingOrder purchasingOrder) throws Exception {
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
		params.put(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID()));
		params.put(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()));
		params.put(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark());
		params.put("commIDs", String.valueOf(commIDs));
		params.put("NOs", String.valueOf(NOs));
		params.put("priceSuggestions", String.valueOf(priceSuggestions));
		params.put("barcodeIDs", String.valueOf(barcodeIDs));
		String response = OkHttpUtil.post("/purchasingOrder/approveEx.bx", params);
		JSONObject object = JSONObject.fromObject(response);
		PurchasingOrder purchasingOrder1 = new PurchasingOrder();
		PurchasingOrder purchasingOrder2 = (PurchasingOrder) purchasingOrder1.parse1(object.getString(BaseAction.KEY_Object));
		return purchasingOrder2;
	}

	public String toJson(ApprovePurchasingOrder apo) {
		String str = JSON.toJSONString(apo);
		return str;
	}

	public BaseBuffer fromJson(String json) {
		ApprovePurchasingOrder apo = JSON.parseObject(json, ApprovePurchasingOrder.class);
		return apo;
	}
}
