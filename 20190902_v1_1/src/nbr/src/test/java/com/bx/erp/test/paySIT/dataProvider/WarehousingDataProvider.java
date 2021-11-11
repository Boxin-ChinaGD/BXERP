package com.bx.erp.test.paySIT.dataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.Shared;
import com.bx.erp.util.GeneralUtil;

public class WarehousingDataProvider {
	protected List<BaseModel> providerList;
	protected List<BaseModel> commodityList;
	protected List<BaseModel> purchasingOrderList;

	public void setPurchasingOrderList(List<BaseModel> purchasingOrderList) {
		this.purchasingOrderList = purchasingOrderList;
	}

	public void setProviderList(List<BaseModel> providerList) {
		this.providerList = providerList;
	}

	public void setCommodityList(List<BaseModel> commodityList) {
		this.commodityList = commodityList;
	}

	public List<Warehousing> generateRandomWarehousings() throws Exception {
		List<Warehousing> list = new ArrayList<Warehousing>();
		//
		Random n = new Random();
		if (purchasingOrderList.size() > 0) {
			for (BaseModel baseModel : purchasingOrderList) {
				PurchasingOrder purchasingOrder = (PurchasingOrder) baseModel;
				if (n.nextBoolean()) { // 是否对这采购订单进行入库
					if (n.nextBoolean()) {
						// 全部入库
						Warehousing generateWarehousing = generateWarehousing(purchasingOrder);
						list.add(generateWarehousing);
					} else {
						// 部分入库
						Warehousing generateRandomWarehousing = generateRandomWarehousing(purchasingOrder);
						if (generateRandomWarehousing != null) {
							list.add(generateRandomWarehousing);
						}

					}
				}
			}
		} else { // 没有采购订单，直接入库
			int warehousingCount = n.nextInt(10) + 1;
			for (int i = 0; i < warehousingCount; i++) {
				Warehousing generateRandomWarehousing = generateRandomWarehousing(null);
				if (generateRandomWarehousing != null) {
					list.add(generateRandomWarehousing);
				}
			}
		}

		return list;
	}

	private Warehousing generateWarehousing(PurchasingOrder purchasingOrder) {
		Warehousing warehousing = new Warehousing();
		warehousing.setWarehouseID(Warehouse.DEFAULT_ID);
		warehousing.setStaffID(Shared.BossID);
		warehousing.setPurchasingOrderID(purchasingOrder.getID());
		warehousing.setProviderID(providerList.get(new Random().nextInt(providerList.size())).getID());
		//
		List<WarehousingCommodity> warehousingCommodities = new ArrayList<WarehousingCommodity>();
		for (Object object : purchasingOrder.getListSlave1()) {
			PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) object;
			//
			WarehousingCommodity wc = new WarehousingCommodity();
			wc.setCommodityID(purchasingOrderCommodity.getCommodityID());
			wc.setBarcodeID(purchasingOrderCommodity.getBarcodeID());
			wc.setNO(purchasingOrderCommodity.getCommodityNO());
			wc.setPrice(purchasingOrderCommodity.getPriceSuggestion());
			wc.setAmount(GeneralUtil.mul(wc.getPrice(), wc.getNO()));
			//
			warehousingCommodities.add(wc);
		}
		warehousing.setListSlave1(warehousingCommodities);
		//
		return warehousing;
	}

	private Warehousing generateRandomWarehousing(PurchasingOrder PurchasingOrder) { // 部分入库，有可能包含全部入库
		Warehousing warehousing = new Warehousing();
		warehousing.setWarehouseID(Warehouse.DEFAULT_ID);
		warehousing.setStaffID(Shared.BossID);
		warehousing.setPurchasingOrderID((PurchasingOrder == null ? 0 : PurchasingOrder.getID()));
		//
		warehousing.setProviderID(providerList.get(new Random().nextInt(providerList.size())).getID());
		List<?> warehousingCommodityList = generateRandomWarehousingCommodityList(PurchasingOrder);
		if (warehousingCommodityList.size() == 0) {
			return null;
		}
		warehousing.setListSlave1(warehousingCommodityList);
		//
		return warehousing;
	}

	private List<?> generateRandomWarehousingCommodityList(PurchasingOrder PurchasingOrder) {
		List<WarehousingCommodity> waCommodities = new ArrayList<WarehousingCommodity>();
		Random n = new Random();
		List<Integer> commIDList = new ArrayList<Integer>();

		if (PurchasingOrder != null) {
			for (Object object : PurchasingOrder.getListSlave1()) {
				if (n.nextBoolean()) {
					WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
					PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) object;
					warehousingCommodity.setCommodityID(purchasingOrderCommodity.getCommodityID());
					warehousingCommodity.setBarcodeID(purchasingOrderCommodity.getBarcodeID());
					warehousingCommodity.setNO((n.nextInt(purchasingOrderCommodity.getCommodityNO()) + 1) + n.nextInt(50));
					warehousingCommodity.setPrice(purchasingOrderCommodity.getPriceSuggestion());
					warehousingCommodity.setAmount(GeneralUtil.mul(warehousingCommodity.getPrice(), warehousingCommodity.getNO()));

					waCommodities.add(warehousingCommodity);
					commIDList.add(purchasingOrderCommodity.getCommodityID());
				}
			}
			// 随机增加额外的入库
			if (n.nextBoolean()) {
				generateRandomWarehousingCommoditys(n, waCommodities, commIDList);
			}
		} else {
			generateRandomWarehousingCommoditys(n, waCommodities, commIDList);
		}
		return waCommodities;
	}

	// 生成采购订单外的入库商品
	private List<WarehousingCommodity> generateRandomWarehousingCommoditys(Random n, List<WarehousingCommodity> waCommodities, List<Integer> commIDList) {
		int count = n.nextInt(20) + 1;
		for (int i = 0; i < count; i++) {
			WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
			Commodity comm = null;
			do {
				comm = (Commodity) commodityList.get(n.nextInt(commodityList.size()));
			} while (commIDList.contains(comm.getID()));

			warehousingCommodity.setCommodityID(comm.getID());
			warehousingCommodity.setBarcodeID(comm.getBarcodeID());
			warehousingCommodity.setNO(n.nextInt(50) + 1);
			warehousingCommodity.setPrice(n.nextInt((int) ((comm.getPriceRetail() < 0.000001 ? 100 : comm.getPriceRetail()) + 1)));
			warehousingCommodity.setAmount(GeneralUtil.mul(warehousingCommodity.getPrice(), warehousingCommodity.getNO()));

			waCommodities.add(warehousingCommodity);
			commIDList.add(comm.getID());
		}
		return waCommodities;
	}
}
