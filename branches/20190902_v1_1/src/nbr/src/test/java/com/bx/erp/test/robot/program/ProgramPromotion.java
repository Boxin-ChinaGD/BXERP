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
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONObject;

public class ProgramPromotion extends Program {

	public ProgramPromotion(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		super(startDatetime, endDatetime, bRunInRandomMode);
	}

	private static int INDEX = 0;

	public enum EnumOperationType {
		EOT_CreatePromotion("Create Promotion", INDEX++), //
		EOT_RetrieveNExPromotion("RetrieveNEx Promotion", INDEX++), //
		EOT_DeletePromotion("Delete Promotion", INDEX++); //

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
	public boolean run(Date currentDatetime, StringBuilder sbError, Program[] programs) throws Exception {
		if (!bRunInRandomMode) {
			while (!queueIn.isEmpty()) {
				ProgramUnit pu = queueIn.peek();
				// if (pu.getNo() == xxx) {
				pu = queueIn.poll();
				Promotion promotion = new Promotion();
				Promotion p = new Promotion();
				//
				switch (EnumOperationType.values()[pu.getOperationType()]) {
				case EOT_CreatePromotion:
					promotion = getPromotion(1);
					Commodity com = (Commodity) pu.getBaseModelOut1();
					PromotionScope ps = new PromotionScope();
					ps.setCommodityID(com.getID());
					List<PromotionScope> listPromotionScope = new ArrayList<PromotionScope>();
					listPromotionScope.add(ps);
					promotion.setListSlave1(listPromotionScope);
					p = promotionCreateEx(promotion);
					pu.setBaseModelOut1(p);
					queueOut.offer(pu);

					break;
				case EOT_RetrieveNExPromotion:
					promotion = (Promotion) pu.getBaseModelIn1();
					// 这里set需要模糊查询的参数，不查的set为-1
					List<BaseModel> listCom = (List<BaseModel>) promotionRetrieveNEx();
					pu.setListBaseModelOut1(listCom);
					queueOut.offer(pu);

					break;
				default:
					promotionDelete((Promotion) pu.getBaseModelIn1());

					break;
				}
				// } else {
				// break;
				// }
			}
		}
		return true;
	}

	private static Promotion promotionCreateEx(Promotion promotion) {
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		String date1 = sdf3.format(promotion.getDatetimeStart());
		String date2 = sdf3.format(promotion.getDatetimeEnd());
		//
		StringBuilder commIDs = new StringBuilder();
		for (Object object : promotion.getListSlave1()) {
			PromotionScope ps = (PromotionScope) object;
			commIDs.append(ps.getCommodityID() + ",");
		}
		//
		Map<String, String> params = new HashMap<>();
		params.put(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10减1");
		// params.put(Promotion.field.getFIELD_NAME_status(),
		// String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()));
		params.put(Promotion.field.getFIELD_NAME_type(), String.valueOf(promotion.getType()));
		params.put(Promotion.field.getFIELD_NAME_datetimeStart(), date1);
		params.put(Promotion.field.getFIELD_NAME_datetimeEnd(), date2);
		params.put(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(promotion.getExcecutionThreshold()));
		params.put(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(promotion.getExcecutionAmount()));
		params.put(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(promotion.getExcecutionDiscount()));
		params.put(Promotion.field.getFIELD_NAME_scope(), String.valueOf(promotion.getScope()));
		// params.put(Promotion.field.getFIELD_NAME_staff(), "1");
		params.put(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(promotion.getReturnObject()));
		params.put(Promotion.field.getFIELD_NAME_commodityIDs(), String.valueOf(commIDs));
		String response = OkHttpUtil.post("/promotionSync/createEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Promotion promotion1 = new Promotion();
		promotion1 = (Promotion) promotion1.parse1(object.getString(BaseAction.KEY_Object));
		//
		return promotion1;
	}

	private static List<BaseModel> promotionRetrieveNEx() {
		Map<String, String> params = new HashMap<>();
		String response = OkHttpUtil.get("/promotionSync/retrieveNEx.bx", params);
		//
		JSONObject object = JSONObject.fromObject(response);
		Promotion promotion = new Promotion();
		List<BaseModel> listPromotion = (List<BaseModel>) promotion.parseN(object.getString(BaseAction.KEY_ObjectList));
		//
		return listPromotion;
	}

	private void promotionDelete(Promotion promotion) {
		Map<String, String> params = new HashMap<>();
		params.put(Promotion.field.getFIELD_NAME_ID(), String.valueOf(promotion.getID()));
		OkHttpUtil.post("/promotionSync/deleteEx.bx", params);
	}

	private Promotion getPromotion(int no) {
		Promotion p = new Promotion();
		Random r = new Random();
		p.setName("机器人促销" + no + System.currentTimeMillis() % 1000000);
		// p.setStatus(0);
		p.setType(r.nextInt(2));
		p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2)); // 活动开始时间
		p.setDatetimeEnd(DatetimeUtil.getDays(new Date(), 3)); // 活动结束时间
		p.setExcecutionThreshold(r.nextInt(50) + 10);
		p.setExcecutionAmount(r.nextInt(10) + 1);
		p.setExcecutionDiscount(1 - r.nextDouble()); // 0<=ran.nextDouble()<1
		p.setScope(r.nextInt(50) % 2);
		// p.setStaff(1);
		p.setPageIndex(1);
		p.setPageSize(10);
		return p;
	}

	@Override
	protected boolean canRunNow(Date d) {
		return true;
	}

	@Override
	protected void generateReport() {
	}
}
