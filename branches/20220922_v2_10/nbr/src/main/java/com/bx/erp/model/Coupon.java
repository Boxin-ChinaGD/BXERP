package com.bx.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.CouponScope.EnumCouponScope;
//import com.bx.erp.model.wx.coupon.EnumCouponType;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Coupon extends BaseModel {
	private static final long serialVersionUID = 1L;

	public static final CouponField field = new CouponField();
	public static final int MaxDiscount = 1;
	public static final int Max_Length_title = 9;
	public static final int Max_Length_color = 16;
	public static final int Max_Length_description = 1024;
	/* 周一到周日1111111，全周可用 */
	public static final int WEEKDAY_Available = 127;
	public static final int Max_Length_beginTime = 8;
	public static final int Max_Length_endTime = 8;
	public static final int MinQuantity = 1;
	public static final int MaxQuantity = 100000;
	public static final int MinPersonalLimit = 1;
	/** 用于在小程序上查询VIP要的优惠券。小程序的用户域没有权限查询优惠券，所以让其有一个不正常的Pos会话，Pos的ID为-2 */
	public static final int POS_ID_RetrieveNCoupon_FromMiniProgram = -2;
	/** 用于在小程序上查询积分为0的优惠券 */
	public static final int BONUS_RetrieveNCoupon_FromMiniProgram = 0;
	//
	public static final String FIELD_ERROR_status = "优惠券的状态只能是" + EnumCouponStatus.ECS_Normal.getIndex() + "或" + EnumCouponStatus.ECS_Expired.getIndex();
	public static final String FIELD_ERROR_cardType = "优惠券的类型只能是" + EnumCouponType.ECT_Cash.getIndex() + "或" + EnumCouponType.ECT_Discount.getIndex();
	public static final String FIELD_ERROR_bonus = "积分必须大于或等于0";
	public static final String FIELD_ERROR_leastAmount = "起用金额必须大于或等于0";
	public static final String FIELD_ERROR_reduceAmount = "减免金额必须大于或等于0";
	public static final String FIELD_ERROR_discount = "折扣需大于0，小于等于" + MaxDiscount;
	public static final String FIELD_ERROR_title = "券名，中英数，不能有空格或其它符号, 字数上限为" + Max_Length_title + "个汉字";
	public static final String FIELD_ERROR_color = "券颜色长度应小于等于" + Max_Length_color;
	public static final String FIELD_ERROR_description = "券使用说明，字数上限为" + Max_Length_description + "个汉字";
	public static final String FIELD_ERROR_type = "可用星期应大于等于0或小于等于" + WEEKDAY_Available;
	public static final String FIELD_ERROR_beginTime = field.getFIELD_NAME_beginTime() + "长度应小于或等于" + Max_Length_beginTime;
	public static final String FIELD_ERROR_endTime = field.getFIELD_NAME_endTime() + "长度应小于或等于" + Max_Length_endTime;
	public static final String FIELD_ERROR_beginDateTime_endDateTime = "开始时间和结束时间不能为空，开始时间必须在结束时间之前！";
	public static final String FIELD_ERROR_quantity = "库存数量应大于等于" + MinQuantity + "小于或等于" + MaxQuantity;
	public static final String FIELD_ERROR_remainingQuantity = "创建优惠券时，当前剩余库存数量应大于或等于0，并且等于库存数量";
	public static final String FIELD_ERROR_scope = field.getFIELD_NAME_scope() + "应等于0或1";
	public static final String FIELD_ERROR_leastAmount_reduceAmount = "起用金额必须大于或等于减免金额";
	public static final String FIELD_ERROR_personalLimit = "每人可领券的数量限制>=" + MinPersonalLimit;
	public static final String FIELD_ERROR_posID = "posID只能为" + BaseAction.INVALID_ID + "或"+ POS_ID_RetrieveNCoupon_FromMiniProgram + "或者大于0";
	public static final String FIELD_ERROR_Paging = "PageIndex和PageSize不符合POS请求参数";
	//
	public static final String FIELD_ERROR_typeForRN = "RN优惠券的type只能是" + EnumCouponType.ECT_Cash.getIndex() + "或" + EnumCouponType.ECT_Discount.getIndex() + "或" + BaseAction.INVALID_ID;
	public static final String FIELD_ERROR_bonusForRN = "RN优惠券的bonus只能是" + BaseAction.INVALID_ID + "或者大于等于0";
	//
	protected int status;

	protected int type;

	protected int bonus;

	protected double leastAmount;

	protected double reduceAmount;

	protected double discount;

	protected String title;

	protected String color;

	protected String description;

	protected int personalLimit;

	protected int weekDayAvailable;

	protected String beginTime;

	protected String endTime;

	protected Date beginDateTime;

	protected Date endDateTime;

	protected int quantity;

	protected int remainingQuantity;

	protected int scope;

	// 用于存放指定商品促销的指定商品ID
	protected String commodityIDs;

	public String getCommodityIDs() {
		return commodityIDs;
	}

	public void setCommodityIDs(String commodityID) {
		this.commodityIDs = commodityID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int cardType) {
		this.type = cardType;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public double getLeastAmount() {
		return leastAmount;
	}

	public void setLeastAmount(double leastAmount) {
		this.leastAmount = leastAmount;
	}

	public double getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(double reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPersonalLimit() {
		return personalLimit;
	}

	public void setPersonalLimit(int personalLimit) {
		this.personalLimit = personalLimit;
	}

	public int getWeekDayAvailable() {
		return weekDayAvailable;
	}

	public void setWeekDayAvailable(int type) {
		this.weekDayAvailable = type;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Date getBeginDateTime() {
		return beginDateTime;
	}

	public void setBeginDateTime(Date beginDateTime) {
		this.beginDateTime = beginDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(int remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "Coupon [status=" + status + ", type=" + type + ", bonus=" + bonus + ", leastAmount=" + leastAmount + ", reduceAmount=" + reduceAmount + ", discount=" + discount + ", title=" + title + ", color=" + color + ", description="
				+ description + ", personalLimit=" + personalLimit + ", weekDayAvailable=" + weekDayAvailable + ", beginTime=" + beginTime + ", endTime=" + endTime + ", beginDateTime=" + beginDateTime + ", endDateTime=" + endDateTime
				+ ", quantity=" + quantity + ", remainingQuantity=" + remainingQuantity + ", scope=" + scope + ", commodityIDs=" + commodityIDs + ", listSlave1=" + listSlave1 + ", ID=" + ID + "]";
	}

	@Override
	public BaseModel clone() throws CloneNotSupportedException {
		Coupon coupon = new Coupon();
		coupon.setID(ID);
		coupon.setStatus(status);
		coupon.setType(type);
		coupon.setBonus(bonus);
		coupon.setLeastAmount(leastAmount);
		coupon.setReduceAmount(reduceAmount);
		coupon.setDiscount(discount);
		coupon.setTitle(title);
		coupon.setColor(color);
		coupon.setDescription(description);
		coupon.setPersonalLimit(personalLimit);
		coupon.setWeekDayAvailable(weekDayAvailable);
		coupon.setBeginTime(beginTime);
		coupon.setEndTime(endTime);
		coupon.setBeginDateTime(beginDateTime);
		coupon.setEndDateTime(endDateTime);
		coupon.setQuantity(quantity);
		coupon.setRemainingQuantity(remainingQuantity);
		coupon.setScope(scope);
		//
		if (listSlave1 != null) {
			List<BaseModel> list = new ArrayList<BaseModel>();
			for (Object object : listSlave1) {
				CouponScope couponScope = (CouponScope) object;
				list.add(couponScope.clone());
			}
			coupon.setListSlave1(list);
		}
		return coupon;
	}

	@Override
	public int compareTo(final BaseModel arg0) {
		if (arg0 == null) {

			return -1;
		}
		Coupon coupon = (Coupon) arg0;
		if ((ignoreIDInComparision == true ? true : coupon.getID() == ID && printComparator(field.getFIELD_NAME_ID())) //
				&& coupon.getStatus() == status && printComparator(field.getFIELD_NAME_status()) //
				&& coupon.getType() == type && printComparator(field.getFIELD_NAME_type()) //
				&& coupon.getBonus() == bonus && printComparator(field.getFIELD_NAME_bonus()) //
				&& Math.abs(GeneralUtil.sub(coupon.getLeastAmount(), leastAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_leastAmount()) //
				&& Math.abs(GeneralUtil.sub(coupon.getReduceAmount(), reduceAmount)) < TOLERANCE && printComparator(field.getFIELD_NAME_reduceAmount()) //
				&& Math.abs(GeneralUtil.sub(coupon.getDiscount(), discount)) < TOLERANCE && printComparator(field.getFIELD_NAME_discount()) //
				&& coupon.getTitle().equals(title) && printComparator(field.getFIELD_NAME_title()) //
				&& coupon.getColor().equals(color) && printComparator(field.getFIELD_NAME_color()) //
				&& coupon.getDescription().equals(description) && printComparator(field.getFIELD_NAME_description()) //
				&& coupon.getPersonalLimit() == personalLimit && printComparator(field.getFIELD_NAME_personalLimit()) //
				&& coupon.getWeekDayAvailable() == weekDayAvailable && printComparator(field.getFIELD_NAME_weekDayAvailable()) //
				&& coupon.getBeginTime().equals(beginTime) && printComparator(field.getFIELD_NAME_beginTime()) //
				&& coupon.getEndTime().equals(endTime) && printComparator(field.getFIELD_NAME_endTime()) //
				&& DatetimeUtil.compareDate(coupon.getBeginDateTime(), beginDateTime) && printComparator(field.getFIELD_NAME_beginDateTime()) //
				&& DatetimeUtil.compareDate(coupon.getEndDateTime(), endDateTime) && printComparator(field.getFIELD_NAME_endDateTime()) //
				&& coupon.getQuantity() == quantity && printComparator(field.getFIELD_NAME_quantity()) //
				&& coupon.getRemainingQuantity() == remainingQuantity && printComparator(field.getFIELD_NAME_remainingQuantity()) //
				&& coupon.getScope() == scope && printComparator(field.getFIELD_NAME_scope()) //
		) {
			return 0;
		}

		return -1;
	}

	@Override
	public BaseModel doParse1(JSONObject jo) {
		try {
			ID = jo.getInt(field.getFIELD_NAME_ID());
			status = jo.getInt(field.getFIELD_NAME_status());
			type = jo.getInt(field.getFIELD_NAME_type());
			bonus = jo.getInt(field.getFIELD_NAME_bonus());
			leastAmount = jo.getDouble(field.getFIELD_NAME_leastAmount());
			reduceAmount = jo.getDouble(field.getFIELD_NAME_reduceAmount());
			discount = jo.getDouble(field.getFIELD_NAME_discount());
			title = jo.getString(field.getFIELD_NAME_title());
			color = jo.getString(field.getFIELD_NAME_color());
			description = jo.getString(field.getFIELD_NAME_description());
			personalLimit = jo.getInt(field.getFIELD_NAME_personalLimit());
			weekDayAvailable = jo.getInt(field.getFIELD_NAME_weekDayAvailable());
			beginTime = jo.getString(field.getFIELD_NAME_beginTime());
			endTime = jo.getString(field.getFIELD_NAME_endTime());
			//
			String tmp = jo.getString(field.getFIELD_NAME_beginDateTime());
			if (!StringUtils.isEmpty(tmp)) {
				beginDateTime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (beginDateTime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_beginDateTime() + "=" + tmp);
				}
			}
			//
			tmp = jo.getString(field.getFIELD_NAME_endDateTime());
			if (!StringUtils.isEmpty(tmp)) {
				endDateTime = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1, Locale.ENGLISH).parse(tmp);
				if (endDateTime == null) {
					throw new RuntimeException("无法解析该日期：" + field.getFIELD_NAME_endDateTime() + "=" + tmp);
				}
			}
			//
			quantity = jo.getInt(field.getFIELD_NAME_quantity());
			remainingQuantity = jo.getInt(field.getFIELD_NAME_remainingQuantity());
			scope = jo.getInt(field.getFIELD_NAME_scope());
			
			// 查询范围表的集合
			List<CouponScope> listCS = new ArrayList<>();
			JSONArray o = jo.getJSONArray(field.getFIELD_NAME_listSlave1());
			// 解析转换JSON串
			for (int i = 0; i < o.size(); i++) {
				JSONObject object = o.getJSONObject(i);
				CouponScope cs = (CouponScope) new CouponScope().parse1(object.toString());
				listCS.add(cs);
			}
			listSlave1 = listCS;
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BaseModel> parseN(String s) {
		List<BaseModel> couponList = new ArrayList<BaseModel>();
		try {
			JSONArray jsonArray1 = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray1.size(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				Coupon coupon = new Coupon();
				coupon.doParse1(jsonObject1);
				couponList.add(coupon);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return couponList;
	}

	@Override
	public Map<String, Object> getCreateParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Coupon coupon = (Coupon) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_status(), coupon.getStatus());
		params.put(field.getFIELD_NAME_type(), coupon.getType());
		params.put(field.getFIELD_NAME_bonus(), coupon.getBonus());
		params.put(field.getFIELD_NAME_leastAmount(), coupon.getLeastAmount());
		params.put(field.getFIELD_NAME_reduceAmount(), coupon.getReduceAmount());
		params.put(field.getFIELD_NAME_discount(), coupon.getDiscount());
		params.put(field.getFIELD_NAME_title(), coupon.getTitle());
		params.put(field.getFIELD_NAME_color(), coupon.getColor());
		params.put(field.getFIELD_NAME_description(), coupon.getDescription());
		params.put(field.getFIELD_NAME_personalLimit(), coupon.getPersonalLimit());
		params.put(field.getFIELD_NAME_weekDayAvailable(), coupon.getWeekDayAvailable());
		params.put(field.getFIELD_NAME_beginTime(), coupon.getBeginTime());
		params.put(field.getFIELD_NAME_endTime(), coupon.getEndTime());
		params.put(field.getFIELD_NAME_beginDateTime(), coupon.getBeginDateTime());
		params.put(field.getFIELD_NAME_endDateTime(), coupon.getEndDateTime());
		params.put(field.getFIELD_NAME_quantity(), coupon.getQuantity());
		params.put(field.getFIELD_NAME_remainingQuantity(), coupon.getRemainingQuantity());
		params.put(field.getFIELD_NAME_scope(), coupon.getScope());

		return params;
	}

	@Override
	public Map<String, Object> getRetrieveNParam(int iUseCaseID, final BaseModel bm) {
		checkParameterInput(bm);

		Coupon coupon = (Coupon) bm;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(field.getFIELD_NAME_posID(), coupon.getPosID());
		params.put(field.getFIELD_NAME_bonus(), coupon.getBonus());
		params.put(field.getFIELD_NAME_type(), coupon.getType());
		params.put(field.getFIELD_NAME_iPageIndex(), coupon.getPageIndex());
		params.put(field.getFIELD_NAME_iPageSize(), coupon.getPageSize());

		return params;
	}

	@Override
	public Map<String, Object> getDeleteParam(int iUseCaseID, final BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public Map<String, Object> getRetrieve1Param(int iUseCaseID, BaseModel bm) {
		return getSimpleParam(iUseCaseID, bm.getID());
	}

	@Override
	public String checkCreate(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();
		switch (iUseCaseID) {
		default:
			if (printCheckField(field.getFIELD_NAME_status(), FIELD_ERROR_status, sbError) && !EnumCouponStatus.inBound(status)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_cardType, sbError) && !EnumCouponCardType.inBound(type)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bonus(), FIELD_ERROR_bonus, sbError) && bonus < 0) {
				return sbError.toString();
			}
			if (type == EnumCouponType.ECT_Cash.getIndex()) {
				if (printCheckField(field.getFIELD_NAME_leastAmount(), FIELD_ERROR_leastAmount, sbError) && leastAmount < 0) {
					return sbError.toString();
				}
				if (printCheckField(field.getFIELD_NAME_reduceAmount(), FIELD_ERROR_reduceAmount, sbError) && (reduceAmount < 0 || Math.abs(GeneralUtil.sub(reduceAmount, 0)) < TOLERANCE)) {
					return sbError.toString();
				}
				if (leastAmount < reduceAmount) {
					return FIELD_ERROR_leastAmount_reduceAmount;
				}
			}
			if (type == EnumCouponType.ECT_Discount.getIndex()) {
				if (printCheckField(field.getFIELD_NAME_discount(), FIELD_ERROR_discount, sbError) && (discount > MaxDiscount || discount < 0 || Math.abs(GeneralUtil.sub(discount, 0)) < TOLERANCE)) {
					return sbError.toString();
				}
			}
			if (printCheckField(field.getFIELD_NAME_title(), FIELD_ERROR_title, sbError) && (!FieldFormat.checkCouponTitle(title))) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_color(), FIELD_ERROR_color, sbError) && (color == null || color.length() > Max_Length_color)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_description(), FIELD_ERROR_description, sbError) && (description == null || description.length() > Max_Length_description)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_weekDayAvailable(), FIELD_ERROR_type, sbError) && (weekDayAvailable < 0 || weekDayAvailable > WEEKDAY_Available)) {
				return sbError.toString();
			}
			if (beginTime != null) {
				if (printCheckField(field.getFIELD_NAME_beginTime(), FIELD_ERROR_beginTime, sbError) && (beginTime.length() > Max_Length_beginTime)) {
					return sbError.toString();
				}
			}
			if (endTime != null) {
				if (printCheckField(field.getFIELD_NAME_endTime(), FIELD_ERROR_endTime, sbError) && (endTime.length() > Max_Length_endTime)) {
					return sbError.toString();
				}
			}
			if (endDateTime == null || beginDateTime == null || !DatetimeUtil.isAfterDate(endDateTime, beginDateTime, 0)) {
				return FIELD_ERROR_beginDateTime_endDateTime;
			}
			if (printCheckField(field.getFIELD_NAME_quantity(), FIELD_ERROR_quantity, sbError) && (quantity < 1 || quantity > MaxQuantity)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_remainingQuantity(), FIELD_ERROR_remainingQuantity, sbError) && (remainingQuantity < 0 || remainingQuantity != quantity)) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_scope(), FIELD_ERROR_scope, sbError) && (scope != EnumCouponScope.ECS_AllCommodities.getIndex() && scope != EnumCouponScope.ECS_SpecifiedCommodities.getIndex())) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_personalLimit(), FIELD_ERROR_personalLimit, sbError) && (personalLimit < 1)) {
				return sbError.toString();
			}
			break;
		}
		return "";
	}

	@Override
	public String checkRetrieve1(int iUseCaseID) {
		return super.checkRetrieve1(iUseCaseID);
	}

	@Override
	protected String doCheckRetrieveN(int iUseCaseID) {
		StringBuilder sbError = new StringBuilder();

		if (printCheckField(field.getFIELD_NAME_posID(), FIELD_ERROR_posID, sbError) && !(posID > 0 || posID == BaseAction.INVALID_ID || posID == POS_ID_RetrieveNCoupon_FromMiniProgram)) {
			return sbError.toString();
		}
		if (posID > 0) { // POS请求
			if (printCheckField(field.getFIELD_NAME_pageIndex(), FIELD_ERROR_Paging, sbError) && printCheckField(field.getFIELD_NAME_pageSize(), FIELD_ERROR_Paging, sbError)
					&& !(pageIndex == BaseAction.PAGE_StartIndex && pageSize == BaseAction.PAGE_SIZE_Infinite)) {
				return sbError.toString();
			}
		} else if(posID == POS_ID_RetrieveNCoupon_FromMiniProgram) {
			if (this.printCheckField(field.getFIELD_NAME_iPageIndex(), FieldFormat.FIELD_ERROR_Paging, sbError) && this.printCheckField(field.getFIELD_NAME_iPageSize(), FieldFormat.FIELD_ERROR_Paging, sbError)//
					&& !FieldFormat.checkPaging(pageIndex, pageSize))//
			{
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_type(), FIELD_ERROR_typeForRN, sbError) && !EnumCouponCardType.inBound(type) && type != BaseAction.INVALID_ID) {
				return sbError.toString();
			}
			if (printCheckField(field.getFIELD_NAME_bonus(), FIELD_ERROR_bonusForRN, sbError) && !(bonus == BaseAction.INVALID_ID || bonus == BONUS_RetrieveNCoupon_FromMiniProgram || bonus > 0)) {
				return sbError.toString();
			}
		}

		return "";
	}

	@Override
	public String checkDelete(int iUseCaseID) {
		return super.checkDelete(iUseCaseID);
	}

	public enum EnumCouponStatus {
		ECS_Normal("Normal", 0), //
		ECS_Expired("Expired", 1);

		private String name;
		private int index;

		private EnumCouponStatus(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumCouponStatus c : EnumCouponStatus.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		public static boolean inBound(int index) {
			if (index < ECS_Normal.getIndex() || index > ECS_Expired.getIndex()) {
				return false;
			}
			return true;
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

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public enum EnumCouponCardType {
		ECCT_CASH("Cash", 0), //
		ECCT_DISCOUNT("Discount", 1);

		private String name;
		private int index;

		private EnumCouponCardType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumCouponCardType c : EnumCouponCardType.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		public static boolean inBound(int index) {
			if (index < ECCT_CASH.getIndex() || index > ECCT_DISCOUNT.getIndex()) {
				return false;
			}
			return true;
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

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public enum EnumCouponType {
		ECT_Cash("CASH", 0), //
		ECT_Discount("DISCOUNT", 1);

		private String name;
		private int index;

		private EnumCouponType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumCouponType pt : EnumCouponType.values()) {
				if (pt.getIndex() == index) {
					return pt.name;
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

		public void setIndex(int index) {
			this.index = index;
		}
	}
}
