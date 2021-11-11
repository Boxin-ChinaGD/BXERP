package com.bx.erp.action.trade.promotion;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

@Component("promotionCalculator")
public class PromotionCalculator {
	private Log logger = LogFactory.getLog(PromotionCalculator.class);

	@Resource
	private DiscountOfAmount doa;
	@Resource
	private CashReducingOfAmount coa;
	@Resource
	private CommodityBO commodityBO;

	/**
	 * 计算零售单在特定促销活动listPromotion下的结果，包括应付款、退货价，找出最优惠的促销方案给顾客。 计算思路： <br />
	 * 1、计算满减或满折后最优惠的结果，促销范围为“指定商品”。 <br />
	 * 2、把1得到的结果，再计算满减或满折后最优惠的结果，促销范围为“全场”。 <br />
	 * 
	 * @param rt
	 *            其商品列表不能为null
	 * @param listPromotion
	 *            需要被初始化为非null的对象
	 * @return 计算后的零售单，这个零售单和rt不是同一个对象。
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public RetailTrade calculateBestCase(RetailTrade rt, List<BaseModel> listPromotion, String dbName) throws FileNotFoundException {
		StringBuilder builderHTML = new StringBuilder();
		builderHTML.append("<html>");
		builderHTML.append("<head><!-- 最新版本的 Bootstrap 核心 CSS 文件 -->\r\n"
				+ "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">\r\n"
				+ "\r\n" + "<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->\r\n"
				+ "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\">\r\n"
				+ "\r\n" + "<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->\r\n"
				+ "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script></head><body><div class=\"container\">");

		// 创建一个临时订单商品表（普通商品，需要进行促销计算）
		List<RetailTradeCommodity> retailTradeNormalCommodityList = new ArrayList<RetailTradeCommodity>();
		// 创建一个临时订单商品表（多包装、组合商品、服务商品不能参与促销）
		List<RetailTradeCommodity> retailtradeOtherCommodityList = new ArrayList<RetailTradeCommodity>();
		// 记录零售单单品的总价，因为非单品是不参与促销的
		Double retailTradeNormalCommodity = 000000d;
		// 记录零售单非单品的总价
		Double retailTradeOtherCommodity = 000000d;
		// 计算指定商品，输入：rt1 = rt.clone()，得出最优的rt1
		List<RetailTradeCommodity> retailTradeCommodityList = (List<RetailTradeCommodity>) rt.getListSlave1();
		if (retailTradeCommodityList.size() == 0) {
			return rt;
		}
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityFromCache = new Commodity();
		List<List<BaseModel>> bmList = new ArrayList<List<BaseModel>>();
		for (RetailTradeCommodity retailTradeCommodity : retailTradeCommodityList) {
			commodityFromCache.setID(retailTradeCommodity.getCommodityID());
			commodityFromCache.setIncludeDeleted(Commodity.IsIncludeDeleted);
			//
			DataSourceContextHolder.setDbName(dbName);
			bmList = commodityBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityFromCache);
			if (commodityBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
				commodityFromCache = Commodity.fetchCommodityFromResultSet(bmList);
				if (commodityFromCache == null) {
					return null; // 由于功能代码不会调用到，所以简单地return null让测试代码终止
				} else {
					if (commodityFromCache.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
						retailTradeNormalCommodity = GeneralUtil.sum(retailTradeNormalCommodity, GeneralUtil.mul(retailTradeCommodity.getPriceOriginal(), retailTradeCommodity.getNO()));
						retailTradeNormalCommodityList.add(retailTradeCommodity);
					} else {
						retailTradeOtherCommodity = GeneralUtil.sum(retailTradeOtherCommodity, GeneralUtil.mul(retailTradeCommodity.getPriceOriginal(), retailTradeCommodity.getNO()));
						retailtradeOtherCommodityList.add(retailTradeCommodity);
					}
				}
			} else {
				return null;// 由于功能代码不会调用到，所以简单地return null让测试代码终止
			}
		}

		RetailTrade rtToPromote = (RetailTrade) rt.clone();
		rtToPromote.setAmount(retailTradeNormalCommodity);
		rtToPromote.setListSlave1(retailTradeNormalCommodityList);
		//
		RetailTrade rtCoaCloned = (RetailTrade) rtToPromote.clone();
		RetailTrade rtDoaCloned = (RetailTrade) rtToPromote.clone();

		// 初始化零售单
		coa.setRetailTrade(rtCoaCloned);
		doa.setRetailTrade(rtDoaCloned);
		// coa.prePromote(Shared.DBName_Test);
		// doa.prePromote(Shared.DBName_Test);
		RetailTrade bestRt = null;
		// 计算促销
		builderHTML.append("<div style=\"background : #E0EEE0\"><h2>/////////////////////////////////////////////进行指定商品满减、满折计算////////////////////////////////////////////</h2>");
		builderHTML.append("<p>以下为计算最优惠的指定促销活动</p>");
		// 跑Jenkins时某个测试将coa的promoted设为true了，每次算促销活动时，把promoted设为false;
		coa.setPromoted(false);
		coa.promote(rtCoaCloned, EnumPromotionScope.EPS_SpecifiedCommodities, listPromotion, dbName);
		builderHTML.append(coa.sbP);
		doa.promote(rtDoaCloned, EnumPromotionScope.EPS_SpecifiedCommodities, listPromotion, dbName);
		builderHTML.append(doa.sbP);
		coa.sbP.setLength(0);
		doa.sbP.setLength(0);
		coa.setPromoted(false);
		doa.setPromoted(false);
		// 参与优惠的促销活动详细计算信息
		List<RetailTradePromotingFlow> retailTradePromotingFlowArrayList = new ArrayList<RetailTradePromotingFlow>();
		// 找出优惠最大（应收款最小）的RetailTrade
		if (rtCoaCloned.getAmount() > rtDoaCloned.getAmount()) {
			bestRt = rtDoaCloned;
			if (doa.promotionBest != null) {
				RetailTradePromotingFlow retailTradePromotingFlows = doa.getMapRetailTradePromotingFlow().get(doa.promotionBest.getID());
				retailTradePromotingFlowArrayList.add(retailTradePromotingFlows);
			}
		} else {
			bestRt = rtCoaCloned;
			if (coa.promotionBest != null) {
				RetailTradePromotingFlow retailTradePromotingFlows = coa.getMapRetailTradePromotingFlow().get(coa.promotionBest.getID());
				retailTradePromotingFlowArrayList.add(retailTradePromotingFlows);
			}
		}
		builderHTML.append("<p class=\"btn btn-primary\">原价为：" + coa.getOriginalTotalAmount(rt, dbName) + "此次参与最优惠促销活动后的价格为" + bestRt.getAmount() + "</p>");
		logger.info("/////////////////////////////////////////////进行全场商品满减、满折计算////////////////////////////////////////////");
		builderHTML.append("</div><div style=\"background : #EBEBEB\"><h2>/////////////////////////////////////////////进行全场商品满减、满折计算////////////////////////////////////////////</h2>");
		rtCoaCloned = (RetailTrade) bestRt.clone();
		rtDoaCloned = (RetailTrade) bestRt.clone();
		//
		builderHTML.append("<p>以下为计算最优惠的全场促销活动</p>");
		coa.promote(rtCoaCloned, EnumPromotionScope.EPS_AllCommodities, listPromotion, dbName);
		builderHTML.append(coa.sbP);
		doa.promote(rtDoaCloned, EnumPromotionScope.EPS_AllCommodities, listPromotion, dbName);
		builderHTML.append(doa.sbP);
		coa.sbP.setLength(0);
		doa.sbP.setLength(0);
		coa.setPromoted(false);
		doa.setPromoted(false);
		if (rtCoaCloned.getAmount() > rtDoaCloned.getAmount()) {
			bestRt = rtDoaCloned;
			if (doa.promotionAllBest != null) {
				RetailTradePromotingFlow retailTradePromotingFlows = doa.getMapRetailTradePromotingFlow().get(doa.promotionAllBest.getID());
				retailTradePromotingFlowArrayList.add(retailTradePromotingFlows);
			}
		} else {
			bestRt = rtCoaCloned;
			if (coa.promotionAllBest != null) {
				RetailTradePromotingFlow retailTradePromotingFlows = coa.getMapRetailTradePromotingFlow().get(coa.promotionAllBest.getID());
				retailTradePromotingFlowArrayList.add(retailTradePromotingFlows);
			}
		}
		// 将计算过程放入零售单中
		if (retailTradePromotingFlowArrayList.size() > 0) {
			RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
			retailTradePromoting.setListSlave1(retailTradePromotingFlowArrayList);

			List<BaseModel> list = new ArrayList<BaseModel>();
			list.add(retailTradePromoting);
			bestRt.setListSlave2(list);
		}
		// 将不参与促销的金额加回零售单中
		List<RetailTradeCommodity> rtcList = new ArrayList<RetailTradeCommodity>();
		bestRt.setAmount(GeneralUtil.sum(bestRt.getAmount(), retailTradeOtherCommodity));
		rtcList = (List<RetailTradeCommodity>) bestRt.getListSlave1();// 将不参与促销的商品加回(最佳)零售单中
		rtcList.addAll(retailtradeOtherCommodityList);
		bestRt.setListSlave1(rtcList);
		//
		builderHTML.append("<p class=\"btn btn-danger\">原价为：" + coa.getOriginalTotalAmount(rt, dbName) + "最终的的支付价格为" + bestRt.getAmount() + "</p>");
		builderHTML.append("</div><hr /><hr />");
		logger.info("***此单原价: " + coa.getOriginalTotalAmount(rt, dbName));
		logger.info("***实际支付价格: " + bestRt.getAmount());
		logger.info("--------------------------------------------------------------------");
		logger.info("商品名称\t\t单价\t\t数量\t\t小计\t\t退货价");
		builderHTML.insert(builderHTML.indexOf("<div class=\"container\">") + 23, "<p class=\"bg-danger\">***实际支付价格: " + bestRt.getAmount() + "</p>");
		builderHTML.insert(builderHTML.indexOf("<div class=\"container\">") + 23, "<p class=\"bg-danger\">***此单原价: " + coa.getOriginalTotalAmount(rt, dbName) + "</p>");
		builderHTML.append("<p class=\"bg-danger\">***此单原价: " + coa.getOriginalTotalAmount(rt, dbName) + "</p>");
		builderHTML.append("<p class=\"bg-danger\">***实际支付价格: " + bestRt.getAmount() + "</p>");
		// coa.generateCommodityTable((List<RetailTradeCommodity>)
		// bestRt.getListSlave1());
		builderHTML.insert(builderHTML.indexOf("<div class=\"container\">") + 23, coa.sbTableCommodity.toString());
		for (Object o : bestRt.getListSlave1()) {
			RetailTradeCommodity rCommodity = (RetailTradeCommodity) o;
			Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(rCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				System.out.println("零售单商品不存在，ID：" + rCommodity.getCommodityID());
				return null;
			}
			logger.info(commodity.getName() + "\t\t" + //
					rCommodity.getPriceOriginal() + "\t\t" + //
					rCommodity.getNO() + "\t\t" + //
					GeneralUtil.mul(rCommodity.getPriceOriginal(), rCommodity.getNO()) + "\t\t" + //
					rCommodity.getPriceReturn()//
			);
		}
		// coa.generateCommodityTable((List<RetailTradeCommodity>)
		// bestRt.getListSlave1());
		coa.promotionFlowHTML.append("<html>");

		coa.promotionFlowHTML.append(coa.sbP);
		coa.promotionFlowHTML.append("</html>");

		builderHTML.append("</div></body>\n</html>");
		logger.info(builderHTML.toString());
		return bestRt;
	}

}
