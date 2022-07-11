package com.bx.erp.cache;

import java.util.Hashtable;
import java.util.List;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.SmallSheetFrameBO;
import com.bx.erp.action.bo.SmallSheetTextBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.model.CacheType.EnumCacheType;

@Component("smallSheetCache")
@Scope("prototype")
public class SmallSheetCache extends BaseCache {
	private Log logger = LogFactory.getLog(SmallSheetCache.class);

	public Hashtable<String, BaseModel> htSmallSheetCache;

	@Resource
	private SmallSheetFrameBO smallSheetFrameBO;

	@Resource
	private SmallSheetTextBO smallSheetTextBO;

	public SmallSheetCache() {
		sCacheName = "小票格式";
	}

	@Override
	protected int getCacheType() {
		return EnumCacheType.ECT_SmallSheet.getIndex();
	}

	@Override
	protected BaseBO getMasterBO() {
		return smallSheetFrameBO;
	}

	@Override
	protected BaseBO getSlaveBO() {
		return smallSheetTextBO;
	}

	@Override
	protected BaseModel getSlaveBaseModel(BaseModel master) {
		SmallSheetText sst = new SmallSheetText();
		sst.setFrameId(master.getID());
		sst.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		return sst;
	}

	@Override
	protected BaseModel getMasterModel(String dbName) {
		SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
		smallSheetFrame.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		return smallSheetFrame;
	}

	@Override
	protected Hashtable<String, BaseModel> getCache() {
		return htSmallSheetCache;
	}

	@Override
	protected void setCache(List<BaseModel> list) {
		htSmallSheetCache = listToHashtable(list);
	}

	public void load(String dbName) {
		logger.info("加载缓存（" + sCacheName + "）");
		doLoad(dbName);
		
		register(dbName);
	}
	// @Override
	// protected void doLoad(String dbName) {
	// logger.info("不加载缓存（" + sCacheName + "）");
	//
	// List<BaseModel> ls = new ArrayList<BaseModel>();
	// writeN((List<BaseModel>) ls); // 将DB的数据缓存进本对象的hashtable中
	// }

}
