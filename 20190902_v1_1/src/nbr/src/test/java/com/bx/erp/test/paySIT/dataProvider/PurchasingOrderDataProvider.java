package com.bx.erp.test.paySIT.dataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.test.Shared;

public class PurchasingOrderDataProvider {
	protected List<BaseModel> providerList;
	protected List<BaseModel> commodityList;

	public void setProviderList(List<BaseModel> providerList) {
		this.providerList = providerList;
	}

	public void setCommodityList(List<BaseModel> commodityList) {
		this.commodityList = commodityList;
	}

	public List<PurchasingOrder> generateRandomPurchasingOrders() throws Exception {
		List<PurchasingOrder> list = new ArrayList<PurchasingOrder>();
		//
		Random n = new Random();
		int count = n.nextInt(10) + 1;
		for (int i = 0; i < count; i++) {
			list.add(generateRandomPurchasingOrder());
		}
		return list;
	}

	private PurchasingOrder generateRandomPurchasingOrder() throws Exception {
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		Random n = new Random();
		purchasingOrder.setProviderID(providerList.get(n.nextInt(providerList.size())).getID());
		purchasingOrder.setStaffID(Shared.BossID); // ... 操作人随机
		purchasingOrder.setRemark("");
		purchasingOrder.setListSlave1(generateRandomPurchasingOrderCommoditys(purchasingOrder.getID()));
		return purchasingOrder;
	}

	private List<PurchasingOrderCommodity> generateRandomPurchasingOrderCommoditys(int purchasingOrderID) throws Exception {
		List<PurchasingOrderCommodity> list = new ArrayList<PurchasingOrderCommodity>();
		List<Integer> commIDList = new ArrayList<Integer>();
		Random n = new Random();
		//
		int count = n.nextInt(20) + 1;
		for (int i = 0; i < count; i++) {
			PurchasingOrderCommodity purchasingOrderCommodity = new PurchasingOrderCommodity();
			purchasingOrderCommodity.setPurchasingOrderID(purchasingOrderID);
			//
			Commodity comm = null;
			do {
				comm = (Commodity) commodityList.get(n.nextInt(commodityList.size()));
			} while (commIDList.contains(comm.getID()));
			commIDList.add(comm.getID());
			//
			purchasingOrderCommodity.setCommodityID(comm.getID());
			purchasingOrderCommodity.setCommodityNO(n.nextInt(200) + 1);
			purchasingOrderCommodity.setBarcodeID(comm.getBarcodeID());
			purchasingOrderCommodity.setPriceSuggestion(n.nextInt((int) ((comm.getPriceRetail() < 0.000001 ? 100 : comm.getPriceRetail()) + 1)));
			//
			list.add(purchasingOrderCommodity);
		}
		return list;
	}
}
