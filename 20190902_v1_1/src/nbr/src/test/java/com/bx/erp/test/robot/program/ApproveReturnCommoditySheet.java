package com.bx.erp.test.robot.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONObject;

public class ApproveReturnCommoditySheet extends Program {
	protected List<ReturnCommoditySheet> approveReturnCommoditySheetList = new ArrayList<ReturnCommoditySheet>();

	public List<ReturnCommoditySheet> getApproveReturnCommoditySheetList() {
		return approveReturnCommoditySheetList;
	}

	protected List<ReturnCommoditySheet> returnCommodityList = new ArrayList<>();

	public void setReturnCommodityList(List<ReturnCommoditySheet> returnCommodityList) {
		this.returnCommodityList = returnCommodityList;
	}

	@Override
	protected boolean canRunNow(Date currentDatetime) {
		if (returnCommodityList == null || returnCommodityList.size() == 0) {
			System.out.println("仓管退货单列表为空！");
			return false;
		}
		return true;
	}

	public ApproveReturnCommoditySheet(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		super(startDatetime, endDatetime, bRunInRandomMode);
	}

	@Override
	protected void generateReport() {

	}

	@Override
	public boolean run(Date currentDatetime, StringBuilder sbError, final Program[] programs) {
		if (bRunInRandomMode) {
			// 判断是否可以审核
			if (canRunNow(currentDatetime)) {
				// 使用随机数判断是否审核退货
				List<ReturnCommoditySheet> removeListReturnCommoditySheet = new ArrayList<ReturnCommoditySheet>();
				for (ReturnCommoditySheet rcs : returnCommodityList) {
					Random r = new Random();
					if (r.nextBoolean()) {
						System.out.println("本次随机到不审核这张采购退货单");
						continue;
					}
					// 审核一张采购退货单。成功时,add进..approveReturnCommoditySheetList
					try {
						ReturnCommoditySheet approveRCS = approveReturnCommoditySheet(rcs);
						approveReturnCommoditySheetList.add(approveRCS);
					} catch (Exception e) {
						e.printStackTrace();
						sbError.append("审核失败！采购退货单ID=" + rcs.getID());
						System.out.println("审核采购退货单失败！采购退货单ID=" + rcs.getID());
					}
					removeListReturnCommoditySheet.add(rcs);
				}
				// 审核一张采购退货单。成功时returnCommodityList.remove。
				for (ReturnCommoditySheet returnCommoditySheet : removeListReturnCommoditySheet) {
					returnCommodityList.remove(returnCommoditySheet);
				}
			}
		}
		return true;
	}

	protected ReturnCommoditySheet approveReturnCommoditySheet(ReturnCommoditySheet returnCommoditySheet) throws Exception {
		StringBuilder commIDs = new StringBuilder();
		StringBuilder rcscNOs = new StringBuilder();
		StringBuilder rcscSpecifications = new StringBuilder();
		StringBuilder barcodeIDs = new StringBuilder();
		StringBuilder commPrices = new StringBuilder();
		for (Object object : returnCommoditySheet.getListSlave1()) {
			ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) object;
			commIDs.append(rcsc.getCommodityID() + ",");
			rcscNOs.append(rcsc.getNO() + ",");
			commPrices.append(rcsc.getPurchasingPrice() + ",");
			rcscSpecifications.append(rcsc.getSpecification() + ",");
			barcodeIDs.append(rcsc.getBarcodeID() + ",");
		}
		// 审核退货单
		Map<String, String> params = new HashMap<>();
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(returnCommoditySheet.getID()));
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(returnCommoditySheet.getProviderID()));
		params.put(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), String.valueOf(returnCommoditySheet.getbReturnCommodityListIsModified()));
		params.put("commIDs", String.valueOf(commIDs));
		params.put("rcscNOs", String.valueOf(rcscNOs));
		params.put("commPrices", String.valueOf(commPrices));
		params.put("rcscSpecifications", String.valueOf(rcscSpecifications));
		params.put("barcodeIDs", String.valueOf(barcodeIDs));
		String response = OkHttpUtil.post("/returnCommoditySheet/approveEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		ReturnCommoditySheet returnCommoditySheet1 = new ReturnCommoditySheet();
		ReturnCommoditySheet returnCommoditySheet2 = (ReturnCommoditySheet) returnCommoditySheet1.parse1(object.getString(BaseAction.KEY_Object));
		return returnCommoditySheet2;
	}
}
