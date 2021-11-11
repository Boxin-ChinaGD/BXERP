package com.bx.erp.cache.commodity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.CommodityShopInfoBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.util.DataSourceContextHolder;

@Component("commodityCache")
@Scope("prototype")
public class CommodityCache extends BaseCache {
	private Log logger = LogFactory.getLog(CommodityCache.class);

	public Hashtable<String, BaseModel> htCommodity;

	@Resource
	private CommodityBO commodityBO;
	
	@Resource
	private CommodityShopInfoBO commodityShopInfoBO;

	public CommodityCache() {
		sCacheName = "商品";
	}

	@Override
	protected void doLoad(String dbName) {
		logger.info("不加载缓存（" + sCacheName + "）");

		List<BaseModel> ls = new ArrayList<BaseModel>();
		writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_Commodity.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return commodityBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return null;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		Commodity com = new Commodity();
		System.out.println(DataSourceContextHolder.getDbName());
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(com.getCacheSizeID(), BaseBO.SYSTEM, new ErrorInfo(), dbName);
		com.setPageSize(Integer.parseInt(ccs.getValue()));

		return com;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		return null;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htCommodity;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htCommodity = listToHashtable(list);
	}

	@Override
	protected int getMaxCacheNumber(String dbName, int staffID) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigCacheSize ccs = (ConfigCacheSize) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("从缓存中读取配置信息错误！");
			return CACHESIZE_Default;
		}

		return Integer.parseInt(ccs.getValue());
	}

	/** 从缓存中读不到对象时，会从DB中读取。如果该对象有从表，则其从表也会读取并设置进主表 */
	@Override
	protected BaseModel retrieve1(int id, int StaffID, ErrorInfo ecOut, String dbName) {
		BaseModel baseModel = getMasterModel(dbName);
		baseModel.setID(id);
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> bmList = getMasterBO().retrieve1ObjectEx(StaffID, BaseBO.INVALID_CASE_ID, baseModel);
		if (getMasterBO().getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.size() == 0) {
			ecOut.setErrorCode(getMasterBO().getLastErrorCode());
			ecOut.setErrorMessage(getMasterBO().getLastErrorMessage());
			return null;
		}
		Commodity commodity = Commodity.fetchCommodityFromResultSet(bmList);
		CommodityShopInfo commodityShopInfo = new CommodityShopInfo();
		commodityShopInfo.setCommodityID(commodity.getID());
		DataSourceContextHolder.setDbName(dbName);
		List<?> listSlave2 = commodityShopInfoBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityShopInfo);
		if (commodityShopInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			ecOut.setErrorCode(commodityShopInfoBO.getLastErrorCode());
			ecOut.setErrorMessage(commodityShopInfoBO.getLastErrorMessage());
			return null;
		}
		commodity.setListSlave2(listSlave2);
		return commodity;
	}

	@Override
	protected void doDelete1(BaseModel o) {
		if (o == null) {
			logger.info("对象为null，不会删除普通缓存");
			return;
		}

		Commodity comm = (Commodity) getCache().get(String.valueOf(o.getID()));
		if (comm != null) {
			if (comm.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				deleteCombinationSlaveCache(comm);
			} else if (comm.getType() == EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				deleteMultiPackagingSlaveCache(comm);
			}
			getCache().remove(String.valueOf(o.getID()));
		}
	}

	/** 场景1: 多包装商品A参照普通商品A1;A被卖出，则A1的库存改变了,需要更新A1的缓存 */
	protected void deleteMultiPackagingSlaveCache(BaseModel bm) {
		getCache().remove(String.valueOf(((Commodity) bm).getRefCommodityID()));
	}

	/** 场景1: 组合商品A包括A1和A2,A被卖出,则A1和A2的库存改变了,需要更新A1和A2的缓存 */
	@SuppressWarnings("unchecked")
	protected void deleteCombinationSlaveCache(BaseModel bm) {
		List<SubCommodity> listSlave1 = (List<SubCommodity>) bm.getListSlave1();
		if (listSlave1 != null) {
			for (SubCommodity subComm : listSlave1) {
				getCache().remove(String.valueOf(subComm.getSubCommodityID()));
			}
		} else {
			logger.error("【商品缓存】当前组合商品的从表为空，组合商品为=" + bm);
		}
	}
	
//	/** 加载从表商品门店信息 */
//	@Override
//	protected void doLoadSlave(List<?> listMaster, String dbName) {
//		super.doLoadSlave(listMaster, dbName);
//		for (Object bm : listMaster) {
//			Commodity commodity = (Commodity) bm;
//			CommodityShopInfo commodityShopInfo = new CommodityShopInfo();
//			commodityShopInfo.setCommodityID(commodity.getID());
//			DataSourceContextHolder.setDbName(dbName);
//			List<?> slaveList2 = commodityShopInfoBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityShopInfo);
//			if (getSlaveBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
//				logger.error("加载缓存（" + sCacheName + "）的从表失败！请重启服务器！！错误信息：" + getMasterBO().printErrorInfo());
//				throw new RuntimeException(BaseModel.ERROR_Tag);
//			}
//			((BaseModel) bm).setListSlave2(slaveList2);
//		}
//	}
}
