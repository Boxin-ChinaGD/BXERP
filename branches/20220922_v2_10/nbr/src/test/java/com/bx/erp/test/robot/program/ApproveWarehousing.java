package com.bx.erp.test.robot.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONObject;

public class ApproveWarehousing extends Program {
	// ...参考ApprovePurchasingOrder
	// 入库单列表
	protected List<Warehousing> warehousingList = new ArrayList<>();

	// 已审核的入库单列表
	protected List<Warehousing> approveWarehousingList = new ArrayList<>();

	public void setWarehousingList(List<Warehousing> warehousingList) {
		this.warehousingList = warehousingList;
	}

	public List<Warehousing> getApproveWarehousingList() {
		return approveWarehousingList;
	}

	@Override
	protected boolean canRunNow(Date currentDatetime) {
		if (warehousingList.size() > 0) {
			return true;
		}
		System.out.println("入库单列表为空！");
		return false;
	}

	public ApproveWarehousing(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		super(startDatetime, endDatetime, bRunInRandomMode);
	}

	@Override
	protected void generateReport() {

	}

//	@Override
//	protected boolean doLoadProgramUnit() {
//		return true;
//	}

	@Override
	public boolean run(Date currentDatetime, StringBuilder sbError, final Program[] programs) throws Exception {
		if (bRunInRandomMode) {
			if (canRunNow(currentDatetime)) {
				if (warehousingList == null || warehousingList.size() == 0) {
					System.out.println("当前入库单列表为空！不需要审核");
					return false;
				}
				List<Warehousing> removeListWarehousing = new ArrayList<Warehousing>();
				for (Warehousing warehousing : warehousingList) {
					Random r = new Random();
					if (r.nextBoolean()) {
						System.out.println("本次随机到不审核这张入库单");
						continue;
					}
					try {
						if (warehousing == null) {
							System.out.println("审核入库单为空");
							break;
						}
						System.out.println("待审核的入库单：" + warehousing);
						Warehousing approveWS = approveWarehousingSheet(warehousing);
						approveWarehousingList.add(approveWS);
					} catch (Exception e) {
						e.printStackTrace();
						sbError.append("审核入库单失败！");
					}
					removeListWarehousing.add(warehousing);
					// sbError.append("审核入库单成功");
				}
				for (Warehousing ws : removeListWarehousing) {
					warehousingList.remove(ws);
				}
			}
		}
		return true;
	}

	public Warehousing approveWarehousingSheet(Warehousing warehousing) throws Exception {
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
		//
		Map<String, String> params = new HashMap<>();
		params.put(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID()));
		params.put(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()));
		params.put(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID()));
		params.put(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(warehousing.getProviderID()));
		params.put(Warehousing.field.getFIELD_NAME_isModified(), String.valueOf(warehousing.getIsModified()));
		params.put("commIDs", String.valueOf(commIDs));
		params.put("commNOs", String.valueOf(commNOs));
		params.put("commPrices", String.valueOf(commPrices));
		params.put("amounts", String.valueOf(amounts));
		params.put("barcodeIDs", String.valueOf(barcodeIDs));
		String response = OkHttpUtil.post("/warehousing/approveEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Warehousing warehousing1 = new Warehousing();
		Warehousing warehousing2 = (Warehousing) warehousing1.parse1(object.getString(BaseAction.KEY_Object));
		return warehousing2;
	}
}
