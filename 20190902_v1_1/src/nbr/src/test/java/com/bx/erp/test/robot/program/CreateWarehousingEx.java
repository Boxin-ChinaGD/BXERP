package com.bx.erp.test.robot.program;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.Warehousing.EnumStatusWarehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.Shared;

public class CreateWarehousingEx extends ProgramEx {

	// 用于保存已审核的采购订单
	public static List<Warehousing> warehousingCreatedList = new ArrayList<>();
	// 用于保存已创建的采购订单
	public static List<Warehousing> WarehousingApprovedList = new ArrayList<>();

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

	@Override
	public boolean run() throws Exception {

		warehousingProgram();
		return false;
	}

	private void warehousingProgram() throws Exception {
		// 随机CRUD入库单活动
		switch (EnumOperationType.values()[new Random().nextInt(5)]) {
		case EOT_CreateWarehousing:
			createWarehousing();
			break;
		case EOT_ApproveWarehousing:
			approveWarehousing();
			break;
		case EOT_UpdateWarehousing:
			updateWarehousing();
			break;
		case EOT_RetrieveNExByFieldsExWarehousing:
			retrieveNExByFieldsExWarehousing();
			break;
		case EOT_Retrieve1ExWarehousing:
			retrieve1ExWarehousing();
			break;
		case EOT_DeleteWarehousing:
			deleteWarehousing();
			break;
		default:
			break;
		}
	}

	private void deleteWarehousing() throws Exception {
		Shared.caseLog("删除入库单");
		if (warehousingCreatedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(warehousingCreatedList.size());
			Warehousing warehousingCreated = warehousingCreatedList.get(ramdomIndex);
			CreateWarehousing.warehousingD(warehousingCreated);
			warehousingCreatedList.remove(ramdomIndex);
			System.out.println("删除入库单成功：" + warehousingCreated);
		}
	}

	private void retrieve1ExWarehousing() throws Exception {
		Shared.caseLog("R1入库单");
		switch (EnumStatusWarehousing.values()[new Random().nextInt(2)]) {
		case ESW_ToApprove:
			if (warehousingCreatedList.size() > 0) {
				int ramdomIndex = new Random().nextInt(warehousingCreatedList.size());
				Warehousing warehousingCreated = warehousingCreatedList.get(ramdomIndex);
				Warehousing retrieve1ExWarehousing = CreateWarehousing.warehousingR1Ex(warehousingCreated);
				System.out.println("R1未审核入库单成功" + retrieve1ExWarehousing);
			}
			break;
		case ESW_Approved:
			if (WarehousingApprovedList.size() > 0) {
				int ramdomIndex = new Random().nextInt(WarehousingApprovedList.size());
				Warehousing warehousingApproved = WarehousingApprovedList.get(ramdomIndex);
				Warehousing retrieve1ExWarehousing = CreateWarehousing.warehousingR1Ex(warehousingApproved);
				System.out.println("R1已审核入库单成功" + retrieve1ExWarehousing);
			}
			break;
		default:
			break;
		}
	}

	private void retrieveNExByFieldsExWarehousing() {
		Shared.caseLog("RN入库单");
		if (warehousingCreatedList.size() > 0 || WarehousingApprovedList.size() > 0) {
			Warehousing warehousing = new Warehousing();
			// 查询创建时间在一天内的
			Calendar cal = Calendar.getInstance();
			Date date2 = new Date();
			cal.setTime(date2);
			cal.add(Calendar.DATE, -1);
			Date date1 = cal.getTime();
			warehousing.setDate1(date1);
			warehousing.setDate2(date2);
			warehousing.setStatus(BaseAction.INVALID_ID);
			List<BaseModel> listWarehousing = CreateWarehousing.warehousingRNByFieldsEx(warehousing);
			System.out.println("RN入库单成功：" + listWarehousing);
		}
	}

	private void updateWarehousing() throws Exception {
		Shared.caseLog("更新入库单");
		if (warehousingCreatedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(warehousingCreatedList.size());
			Warehousing warehousingCreated = warehousingCreatedList.get(ramdomIndex);
			for (int i = 0; i < warehousingCreated.getListSlave1().size() - 1; i++) {
				WarehousingCommodity warehousingCommodity = (WarehousingCommodity) warehousingCreated.getListSlave1().get(i);
				warehousingCommodity.setNO(new Random().nextInt(100) + 1);
				warehousingCommodity.setPrice(new Random().nextInt(100) + 1);
			}
			Warehousing updateWarehousing = CreateWarehousing.warehousingCUA("/warehousing/updateEx.bx", warehousingCreated);
			warehousingCreatedList.remove(ramdomIndex);
			warehousingCreatedList.add(updateWarehousing);
			System.out.println("更新入库单成功：" + updateWarehousing);
		}
	}

	private void approveWarehousing() throws Exception {
		Shared.caseLog("审核入库单");
		if (warehousingCreatedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(warehousingCreatedList.size());
			Warehousing warehousingCreated = warehousingCreatedList.get(ramdomIndex);
			Warehousing warehousingApproved = CreateWarehousing.warehousingCUA("/warehousing/approveEx.bx", warehousingCreated);
			warehousingCreatedList.remove(ramdomIndex);
			WarehousingApprovedList.add(warehousingApproved);
			System.out.println("审核入库单成功:" + warehousingApproved);
		}

	}

	private void createWarehousing() throws Exception {
		Shared.caseLog("创建入库单");
		// 审核了采购订单才能入库
		if (CreatePurchasingOrderEx.purchasingOrderApprovedList.size() > 0) {
			int ramdomIndex = new Random().nextInt(CreatePurchasingOrderEx.purchasingOrderApprovedList.size());
			PurchasingOrder purchasingOrderApproved = CreatePurchasingOrderEx.purchasingOrderApprovedList.get(ramdomIndex);
			List<WarehousingCommodity> warehousingCommList = new ArrayList<>();
			for (int i = 0; i < purchasingOrderApproved.getListSlave1().size(); i++) {
				PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) purchasingOrderApproved.getListSlave1().get(i);
				WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
				warehousingCommodity.setCommodityID(purchasingOrderCommodity.getCommodityID());
				warehousingCommodity.setBarcodeID(purchasingOrderCommodity.getBarcodeID());
				// TODO 随机入库：全部、部分入库
				warehousingCommodity.setNO(purchasingOrderCommodity.getCommodityNO());
				warehousingCommodity.setPrice(purchasingOrderCommodity.getPriceSuggestion());
				// TODO 浮点数相乘
				warehousingCommodity.setAmount(warehousingCommodity.getPrice() * warehousingCommodity.getNO());
				warehousingCommList.add(warehousingCommodity);
			}
			Warehousing warehousing = new Warehousing();
			warehousing.setListSlave1(warehousingCommList);
			warehousing.setPurchasingOrderID(purchasingOrderApproved.getID());
			warehousing.setProviderID(1);
			warehousing.setWarehouseID(1);
			warehousing.setStaffID(4);// 15854320895手机号登录
			CreatePurchasingOrderEx.purchasingOrderApprovedList.remove(ramdomIndex);
			Warehousing warehousingCreated = CreateWarehousing.warehousingCUA("/warehousing/createEx.bx", warehousing);
			warehousingCreatedList.add(warehousingCreated);
			System.out.println("创建入库单成功" + warehousingCreated);
		}
	}

}
