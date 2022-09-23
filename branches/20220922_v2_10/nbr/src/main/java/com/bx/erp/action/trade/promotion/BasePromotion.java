package com.bx.erp.action.trade.promotion;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;

@Component("BasePromotion")
public abstract class BasePromotion {
	private Log logger = LogFactory.getLog(BasePromotion.class);

	protected String dbName;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public enum EnumPromotionScope {
		EPS_AllCommodities("All Commodities", 0), EPS_SpecifiedCommodities("Specified Commodities", 1);

		private String name;
		private int index;

		private EnumPromotionScope(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static boolean inBound(int index) {
			if (index < EPS_AllCommodities.getIndex() || index > EPS_SpecifiedCommodities.getIndex()) {
				return false;
			}
			return true;
		}

		public static String getName(int index) {
			for (EnumPromotionScope c : EnumPromotionScope.values()) {
				if (c.getIndex() == index) {
					return c.name;
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
	
	public enum EnumPromotionShopScope {
		EPS_AllShops("All Shops", 0), EPS_SpecifiedShops("Specified Shops", 1);

		private String name;
		private int index;

		private EnumPromotionShopScope(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static boolean inBound(int index) {
			if (index < EPS_AllShops.getIndex() || index > EPS_SpecifiedShops.getIndex()) {
				return false;
			}
			return true;
		}

		public static String getName(int index) {
			for (EnumPromotionShopScope c : EnumPromotionShopScope.values()) {
				if (c.getIndex() == index) {
					return c.name;
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

	/** 标识零售单是否经过了促销计算得到了优惠 */
	protected boolean promoted;

	public boolean isPromoted() {
		return promoted;
	}

	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}

	// protected final int SPECIAL_OFFER_Involved = 1;
	// 流程
	protected StringBuilder promotionFlow;
	protected StringBuilder sbTableCommodity;
	protected StringBuilder sbP; // HTML的P标签
	// protected StringBuilder commodityPriceTable;
	// protected StringBuilder sbTableInP;
	protected StringBuilder sbPromotionTable;
	protected StringBuilder sbPromotionScopeTable;
	protected static final String SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;";

	protected StringBuilder promotionFlowHTML;
	protected final Calendar cal = Calendar.getInstance();

	public StringBuilder getPromotionFlowHTML() {
		return promotionFlowHTML;
	}

	public StringBuilder getPromotionflow() {
		return promotionFlow;
	}

	/** 保存此次活动为满减还是满折 还是 优惠卷 或者其他 */
	protected String promotionType;

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	/** 不算促销不算优惠券的前提下，零售单里的所有的商品的原价的总和 */
	protected double originalTotalAmount;

	public double getOriginalTotalAmount(RetailTrade rt, String dbName) {
		double dLocalBalance = 0.000000d; // 指定商品的售价总和
		for (Object o : rt.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;
			dLocalBalance = GeneralUtil.sum(dLocalBalance , GeneralUtil.mul(rtc.getPriceOriginal() ,rtc.getNO()));
		}
		this.originalTotalAmount = dLocalBalance;
		return originalTotalAmount;
	}

	public void setOriginalTotalAmount(double originalTotalAmount) {
		this.originalTotalAmount = originalTotalAmount;
	}

	protected RetailTrade retailTrade;

	public RetailTrade getRetailTrade() {
		return retailTrade;
	}

	public void setRetailTrade(RetailTrade retailTrade) throws FileNotFoundException {
		this.retailTrade = retailTrade;
	}

	// 保留两位小数
	protected DecimalFormat decimalFormatView = new DecimalFormat("#.##");

	protected DecimalFormat decimalFormatDB = new DecimalFormat("#.######");
	// 保存每个指定促销活动 参与的商品
	protected Map<Integer, List<RetailTradeCommodity>> mapCommodity = new HashMap<>();

	public Map<Integer, List<RetailTradeCommodity>> getMapCommodity() {
		return mapCommodity;
	}

	public void setMapCommodity(Map<Integer, List<RetailTradeCommodity>> mapCommodity) {
		this.mapCommodity = mapCommodity;
	}
	
	// 保存每个促销活动 的详细计算过程
    protected Map<Integer, RetailTradePromotingFlow> mapRetailTradePromotingFlow = new HashMap<>();

    public Map<Integer, RetailTradePromotingFlow> getMapRetailTradePromotingFlow() {
        return mapRetailTradePromotingFlow;
    }

    public void setMapRetailTradePromotingFlow(Map<Integer, RetailTradePromotingFlow> mapRetailTradePromotingFlow) {
        this.mapRetailTradePromotingFlow = mapRetailTradePromotingFlow;
    }

	// 保存最优惠的指定促销活动
	protected Promotion promotionBest;

	public Promotion getPromotionBest() {
		return promotionBest;
	}

	public void setPromotionBest(Promotion promotionBest) {
		this.promotionBest = promotionBest;
	}

	// 保存最优惠的全局促销活动
	protected Promotion promotionAllBest;

	public void setPromotionAllBest(Promotion promotionAllBest) {
		this.promotionAllBest = promotionAllBest;
	}

	public Promotion getPromotionAllBest() {
		return promotionAllBest;
	}

	protected void appendText(StringBuilder sb, String text) {
		sb.append(text);
	}

	/**
	 * <p>
	 * 准备促销计算的数据，包括
	 * </p>
	 * <p>
	 * 1、商品原零售价
	 * </p>
	 * <p>
	 * 2、退货时商品的退货价
	 * </p>
	 * <p>
	 * 3、应收款总和（未计促销优惠在内）
	 * </p>
	 */
	// @SuppressWarnings("unchecked")
	// public void prePromote(String dbName) {
	// List<RetailTradeCommodity> commodityList = (List<RetailTradeCommodity>)
	// this.getRetailTrade().getListSlave1();
	// originalTotalAmount = 0.0f;
	// for (RetailTradeCommodity rtc : commodityList) {
	// ErrorInfo ecOut = new ErrorInfo();
	// Commodity commodity = (Commodity)
	// CacheManager.getCache(EnumCacheType.ECT_Commodity).read1(rtc.getCommodityID(),
	// BaseBO.SYSTEM, ecOut);
	// if (commodity == null) {
	// // ...
	// throw new RuntimeException("commodity不能为null");
	// }
	// // -- 原价
	// rtc.setPriceOriginal(commodity.getPriceRetail());
	// // -- 退货价 暂时设置为零售价
	// rtc.setPriceReturn(commodity.getPriceRetail());
	// originalTotalAmount += rtc.getPriceOriginal() * rtc.getNO();
	// }
	// // 暂时将原价总和设置为实际支付价格
	// this.getRetailTrade().setAmount(originalTotalAmount);
	// }

	/** 按照设定的促销活动计算当前零售单的促销优惠，并将计算过程保存在promotionFlow和HTML中。如果计算过程中能确定退货价，则确定之
	 * 
	 * @param rt
	 *            本次计算的零售单。它一般是原零售单的副本
	 * @param listPromotion
	 *            所有当前有效的（不在删除状态）活动的（在促销时间中）促销活动 需要被初始化即使是null 请初始化
	 * @param eps
	 *            促销范围：指定的商品、全场。 */
	@SuppressWarnings("unchecked")
	public void promote(RetailTrade rt, EnumPromotionScope eps, final List<BaseModel> listPromotion, String dbName) {
		Promotion proBestPromotion = null;
		double fBestAmount = rt.getAmount();
		double fAmountTmp = 0.000000f;
		Date nowData = new Date();
		for (Object o : listPromotion) {
			final Promotion promotion = (Promotion) o;
			// 只计算满减
			if (isTargetPromotion(promotion) && DatetimeUtil.isInDateRange(promotion.getDatetimeStart(), nowData, promotion.getDatetimeEnd()) && promotion.getStatus() != EnumStatusPromotion.ESP_Deleted.getIndex()) {
				// ...采用promotion计算this.retailTrade得出proBestPromotion和fBestAmount
				switch (eps) {
				case EPS_SpecifiedCommodities:
					if (promotion.getScope() == EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()) {
						sbP.append("<h2>++++++++++++++++++" + promotion.getName() + "+++++在活动时间++++++++</h2>");
						// 清空
						sbPromotionTable.setLength(0);
						sbPromotionScopeTable.setLength(0);
						generatePromotionTable(promotion);
						generatePromotionScopeTable((List<PromotionScope>) promotion.getListSlave1());
						sbP.append("<p>" + sbPromotionTable.toString() + "</p>");
						sbP.append("<p>" + sbPromotionScopeTable.toString() + "</p>");
						fAmountTmp = promoteSpecifiedCommodities(rt, promotion, dbName);
					}
					break;
				default: // EPS_AllCommodities
					if (promotion.getScope() == EnumPromotionScope.EPS_AllCommodities.getIndex()) {
						sbP.append("<h2>+++++++++++++++++" + promotion.getName() + "+++++在活动时间++++++++</h2>");
						// 清空
						sbPromotionTable.setLength(0);
						generatePromotionTable(promotion);
						sbP.append("<p>" + sbPromotionTable.toString() + "</p>");
						fAmountTmp = promoteAllCommodities(rt, promotion, dbName);
					}
					break;
				}
				// 记录最优惠的活动和售价
				if (isPromoted() && fAmountTmp < fBestAmount) {
					proBestPromotion = promotion;
					fBestAmount = fAmountTmp;
				}
			}
		}
		if (!isPromoted()) {
			logger.info("范围为" + eps.toString() + "时没有找到有" + getPromotionType() + "的促销活动！");
			sbP.append("<p>范围为" + eps.toString() + "时没有找到有" + getPromotionType() + "的促销活动！</p>");
		} else {
			// 有参与过活动mn
			if (proBestPromotion.getScope() == EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()) {
				// 只保存指定促销活动
				promotionBest = proBestPromotion;
			}
			if (proBestPromotion.getScope() == EnumPromotionScope.EPS_AllCommodities.getIndex()) {
				// 只保存全局促销活动
				promotionAllBest = proBestPromotion;
			}
			rt.setAmount(fBestAmount);
			calculateReturnPrice(rt, fBestAmount, eps);
			logger.info("范围为" + eps.toString() + "时最优惠的促销活动为：" + proBestPromotion);
			sbP.append("<p class='btn btn-success'>范围为" + eps.toString() + "时最优惠的促销活动为：" + proBestPromotion.getName() + "的促销活动！支付价格为：" + rt.getAmount() + " 。</p>");
			sbP.append("<hr />");
			logger.info("---------------------------------------------------------------------------");
		}
	}

	protected abstract double promoteAllCommodities(RetailTrade rt, final Promotion promotion, String dbName);

	protected abstract double promoteSpecifiedCommodities(RetailTrade rt, final Promotion promotion, String dbName);

	/** fAmountTmp商品总价格计算出此单(rt)对应的商品退货价格
	 * 
	 * @param rt
	 * @param fAmountTmp
	 */
	@SuppressWarnings("unchecked")
	public void calculateReturnPrice(RetailTrade rt, double fBestAmount, EnumPromotionScope eps) {
		List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) rt.getListSlave1();
		for (RetailTradeCommodity rtc : retailTradeCommodityList) {
			rtc.setPriceReturn(( //
			GeneralUtil.mul( //
					GeneralUtil.div(rtc.getPriceOriginal() * rtc.getNO(), getOriginalTotalAmount(rt, dbName), 6), fBestAmount //
			) / rtc.getNO()));
		}
	}

	public BasePromotion() {
		promoted = false;

		promotionFlow = new StringBuilder();
		promotionFlowHTML = new StringBuilder();
		sbTableCommodity = new StringBuilder();
		sbP = new StringBuilder();
		// sbTableInP = new StringBuilder();
		sbPromotionTable = new StringBuilder();
		sbPromotionScopeTable = new StringBuilder();
		// commodityPriceTable = new StringBuilder();
	}

	/** 生成促销Promotion p的表格
	 * 
	 * @param p
	 *            促销的主表信息 */
	protected void generatePromotionTable(final Promotion p) {
		// ...sbPromotionTable
		sbPromotionTable.append("<h1>促销表 </h1>");

		sbPromotionTable.append("<p><table class=\"table table-bordered\">");
		sbPromotionTable.append("<th>ID</th>");
		sbPromotionTable.append("<th>活动名称</th>");
		sbPromotionTable.append("<th>活动类型</th>");
		sbPromotionTable.append("<th>是否生效</th>");
		sbPromotionTable.append("<th>起始日期</th>");
		sbPromotionTable.append("<th>当前日期</th>");
		sbPromotionTable.append("<th>结束日期</th>");
		sbPromotionTable.append("<th>全场</th>");
		sbPromotionTable.append("<th>买满金额</th>");
		sbPromotionTable.append("<th>减现</th>");
		sbPromotionTable.append("<th>折扣</th>");

		sbPromotionTable.append("<tr><td>").append(p.getID());
		sbPromotionTable.append("</td><td>").append(p.getName());
		sbPromotionTable.append("</td><td>").append(p.getType() == 0 ? "满减" : "满折");
		sbPromotionTable.append("</td><td>").append(p.getStatus() == 0 ? "生效" : "失效");
		sbPromotionTable.append("</td><td>").append(GeneralUtil.getStringTime(p.getDatetimeStart()));
		sbPromotionTable.append("</td><td>").append(GeneralUtil.getStringTime(new Date()));
		sbPromotionTable.append("</td><td>").append(GeneralUtil.getStringTime(p.getDatetimeEnd()));
		sbPromotionTable.append("</td><td>").append(p.getScope() == 0 ? "全场" : "指定商品参与");
		sbPromotionTable.append("</td><td>").append(p.getExcecutionThreshold());
		sbPromotionTable.append("</td><td>").append(p.getType() == 0 ? p.getExcecutionAmount() : "");
		sbPromotionTable.append("</td><td>").append(p.getType() == 1 ? p.getExcecutionDiscount() : "");
		sbPromotionTable.append("</td></tr>");
		sbPromotionTable.append("</table></p>");
	}

	/** 生成促销PromotionScope ps的表格
	 * 
	 * @param p
	 *            促销的从表信息 */
	protected void generatePromotionScopeTable(List<PromotionScope> piSc) {
		sbPromotionScopeTable.append("<h1>促销范围表</h1>");
		sbPromotionScopeTable.append("<p><table class=\"table table-bordered\">");
		sbPromotionScopeTable.append("<th>促销ID</th>");
		sbPromotionScopeTable.append("<th>商品ID</th>");
		for (PromotionScope pi : piSc) {
			sbPromotionScopeTable.append("<tr><td>" + pi.getPromotionID() + "</td>");
			sbPromotionScopeTable.append("<td>" + pi.getCommodityID() + "</td>");
			sbPromotionScopeTable.append("</tr>");
		}
		sbPromotionScopeTable.append("</table></p>");
	}

	/** 生成零售单里的所有商品的表格，其中包括所有商品的原零售价的总和 */
	// protected void generateCommodityTable(String dbName,
	// List<RetailTradeCommodity> retailTradeCommodityList) {
	// sbTableCommodity.append("<h1>零售单信息</h1>");
	//
	// sbTableCommodity.append("<p><table class=\"table table-bordered\">");
	// sbTableCommodity.append("<th>ID</th>");
	// sbTableCommodity.append("<th>商品名称</th>");
	// sbTableCommodity.append("<th>数量</th>");
	// sbTableCommodity.append("<th>品牌</th>");
	// sbTableCommodity.append("<th>单位</th>");
	// sbTableCommodity.append("<th>单价</th>");
	// sbTableCommodity.append("<th>销售价</th>");
	// sbTableCommodity.append("<th>退货价</th>");
	// double fTotalPriceRetail = 0.000000f;
	// double totalPrice = 0.000000f;
	// for (RetailTradeCommodity rtc : retailTradeCommodityList) {
	// ErrorInfo ecOut = new ErrorInfo();
	// Commodity commodity = (Commodity)
	// CacheManager.getCache(EnumCacheType.ECT_Commodity).read1(rtc.getCommodityID(),
	// BaseBO.SYSTEM, ecOut);
	// if (commodity == null) {
	// // ...
	// }
	// sbTableCommodity.append("<tr>");
	// sbTableCommodity.append("<td>" + commodity.getID() + "</td>");
	// sbTableCommodity.append("<td>" + commodity.getName() + "</td>");
	// sbTableCommodity.append("<td>" + rtc.getNO() + "</td>");
	// sbTableCommodity.append("<td>" + commodity.getBrandID() + "</td>");
	// sbTableCommodity.append("<td>" + commodity.getPurchasingUnit() + "</td>");
	// sbTableCommodity.append("<td>" + commodity.getPriceRetail() + "</td>");
	// sbTableCommodity.append("<td>" + rtc.getPriceOriginal() * rtc.getNO() +
	// "</td>");
	// sbTableCommodity.append("<td>" +
	// decimalFormatView.format(rtc.getPriceReturn()) + "</td>");
	// sbTableCommodity.append("</tr>");
	//
	// fTotalPriceRetail += rtc.getPriceOriginal() * rtc.getNO();
	// totalPrice += rtc.getPriceReturn() * rtc.getNO();
	// }
	// sbTableCommodity.append("<tr><td></td><td>合计</td><td></td><td></td><td></td><td></td><td>"
	// + fTotalPriceRetail + "</td><td>" + "$" + totalPrice + "</td></tr>");
	// sbTableCommodity.append("</table></p>");
	// }

	public abstract boolean isTargetPromotion(final Promotion pt);
}
