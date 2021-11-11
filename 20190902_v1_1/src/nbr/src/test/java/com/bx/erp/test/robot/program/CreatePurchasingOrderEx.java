package com.bx.erp.test.robot.program;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrder.EnumStatusPurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.test.BasePurchasingOrderTest;
import com.bx.erp.test.Shared;

public class CreatePurchasingOrderEx extends ProgramEx {

	public static final int STAFF_ID3 = 3;

	private static int INDEX = 0;

	// 用于保存已审核的采购订单
	public static List<PurchasingOrder> purchasingOrderCreatedList = new ArrayList<>();
	// 用于保存已创建的采购订单
	public static List<PurchasingOrder> purchasingOrderApprovedList = new ArrayList<>();

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

	@Override
	public boolean run() throws Exception {

		purchasingOrderProgram();

		return false;
	}

	private void purchasingOrderProgram() throws Exception {
		// 随机CRUD采购订单活动
		switch (EnumOperationType.values()[new Random().nextInt(5)]) {
		case EOT_CreatePurchasingOrder:
			createPurchasingOrder();
			break;
		case EOT_ApprovePurchasingOrder:
			approvePurchasingOrder();
			break;
		case EOT_UpdatePurchasingOrder:
			updatePurchasingOrder();
			break;
		case EOT_RetrieveNExPurchasingOrder:
			retrieveNExPurchasingOrder();
			break;
		case EOT_Retrieve1ExPurchasingOrder:
			retrieve1ExPurchasingOrder();
			break;
		case EOT_DeletePurchasingOrder:
			deletePurchasingOrder();
			break;
		default:
			break;
		}
	}

	private void deletePurchasingOrder() throws Exception {
		Shared.caseLog("删除采购订单");
		if (purchasingOrderCreatedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(purchasingOrderCreatedList.size());
			PurchasingOrder purchasingOrderCreated = purchasingOrderCreatedList.get(ramdomIndex);
			CreatePurchasingOrder.purchasingOrdeD(purchasingOrderCreated);
			System.out.println("删除采购订单: " + purchasingOrderCreated);
			purchasingOrderCreatedList.remove(ramdomIndex);
		}
	}

	private void retrieve1ExPurchasingOrder() throws Exception {
		Shared.caseLog("R1Ex采购订单");
		switch (EnumStatusPurchasingOrder.values()[new Random().nextInt(2)]) {
		case ESPO_ToApprove:
			if (purchasingOrderCreatedList.size() > 0) {
				int ramdomIndex = new Random().nextInt(purchasingOrderCreatedList.size());
				PurchasingOrder purchasingOrderCreated = purchasingOrderCreatedList.get(ramdomIndex);
				PurchasingOrder po = CreatePurchasingOrder.purchasingOrderR1Ex(purchasingOrderCreated);
				System.out.println("r1ex未审核采购单成功: " + po);
			}
			break;
		case ESPO_Approved:
			if (purchasingOrderApprovedList.size() > 0) {
				int ramdomIndex = new Random().nextInt(purchasingOrderApprovedList.size());
				PurchasingOrder purchasingOrderCreated = purchasingOrderApprovedList.get(ramdomIndex);
				PurchasingOrder po = CreatePurchasingOrder.purchasingOrderR1Ex(purchasingOrderCreated);
				System.out.println("r1ex已审核采购单成功: " + po);
			}
			break;
		default:
			break;
		}
	}

	private void retrieveNExPurchasingOrder() throws Exception {
		Shared.caseLog("RN采购订单");
		if (purchasingOrderCreatedList.size() > 0 || purchasingOrderApprovedList.size() > 0) {
			PurchasingOrder purchasingOrder = new PurchasingOrder();
			// 查询创建时间在一天内的
			Calendar cal = Calendar.getInstance();
			Date date2 = new Date();
			cal.setTime(date2);
			cal.add(Calendar.DATE, -1);
			Date date1 = cal.getTime();
			purchasingOrder.setDate1(date1);
			purchasingOrder.setDate2(date2);
			List<BaseModel> listPurchasingOrder = CreatePurchasingOrder.purchasingOrderRNEx(purchasingOrder);
			System.out.println("RNEx采购订单成功: " + listPurchasingOrder);
		}
	}

	private void updatePurchasingOrder() throws Exception {
		Shared.caseLog("更新采购订单");
		if (purchasingOrderCreatedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(purchasingOrderCreatedList.size());
			PurchasingOrder purchasingOrderCreated = purchasingOrderCreatedList.get(ramdomIndex);
			for (int i = 0; i < purchasingOrderCreated.getListSlave1().size(); i++) {
				PurchasingOrderCommodity poc = (PurchasingOrderCommodity) purchasingOrderCreated.getListSlave1().get(i);
				poc.setCommodityNO(new Random().nextInt(100) + 1);
				poc.setPriceSuggestion(new Random().nextInt(100) + 1);
			}
			PurchasingOrder purchasingOrder = CreatePurchasingOrder.purchasingOrderCUA("/purchasingOrder/updateEx.bx", purchasingOrderCreated);
			purchasingOrderCreatedList.remove(ramdomIndex);
			purchasingOrderCreatedList.add(purchasingOrder);
			Shared.caseLog("更新采购订单成功：" + purchasingOrder);
		}

	}

	private void approvePurchasingOrder() throws Exception {
		Shared.caseLog("审核采购订单");
		// 有创建过采购订单才能审核
		if (purchasingOrderCreatedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(purchasingOrderCreatedList.size());
			PurchasingOrder purchasingOrderCreated = purchasingOrderCreatedList.get(ramdomIndex);
			PurchasingOrder purchasingOrderApprove = CreatePurchasingOrder.purchasingOrderCUA("/purchasingOrder/approveEx.bx", purchasingOrderCreated);
			purchasingOrderCreatedList.remove(ramdomIndex);
			purchasingOrderApprovedList.add(purchasingOrderApprove);
			System.out.println("审核采购订单成功：" + purchasingOrderApprove);
		}
	}

	private void createPurchasingOrder() throws Exception {
		Shared.caseLog("创建采购订单");
		if (CreateCommodityEx.normalCommodityCreatedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(CreateCommodityEx.normalCommodityCreatedList.size());
			Commodity commodityCreate = CreateCommodityEx.normalCommodityCreatedList.get(ramdomIndex);
			PurchasingOrder purchasingOrder = new PurchasingOrder();
			List<PurchasingOrderCommodity> pOrderCommList = new ArrayList<>();
			PurchasingOrderCommodity purchasingOrderCommodity = BasePurchasingOrderTest.DataInput.getPurchasingOrderCommodity();
			// 查询商品条形码
			List<Barcodes> barcodeList = CreateCommodity.retrieveNBarcodeEx(commodityCreate);
			int barcodeIdInDB = barcodeList.get(0).getID();
			purchasingOrderCommodity.setCommodityID(commodityCreate.getID());
			purchasingOrderCommodity.setBarcodeID(barcodeIdInDB);
			pOrderCommList.add(purchasingOrderCommodity);
			purchasingOrder.setListSlave1(pOrderCommList);
			purchasingOrder.setProviderID(1);
			PurchasingOrder purchasingOrderCreate = CreatePurchasingOrder.purchasingOrderCUA("/purchasingOrder/createEx.bx", purchasingOrder);
			purchasingOrderCreatedList.add(purchasingOrderCreate);
			System.out.println("创建采购订单成功：" + purchasingOrderCreate);
		}
	}
}
