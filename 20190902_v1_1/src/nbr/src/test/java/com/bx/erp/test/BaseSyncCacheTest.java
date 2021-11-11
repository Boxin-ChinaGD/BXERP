package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.Pos;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class BaseSyncCacheTest extends BaseTestNGSpringContextTest {

	/** 所有POS机都同步了一个块，才可以删除这个同步块 */
	protected static void letAllPosSyncAndDeleteSyncCache(BaseMapper mapperSyncCache, BaseSyncCache syncCache, BaseMapper mapperDispather, BaseSyncCacheDispatcher syncCacheDispatcher, PosBO posBO) {
		syncCacheDispatcher.setSyncCacheID(syncCache.getID());
		//
		List<Pos> listPos = Shared.getPosesFromDB(posBO, Shared.DBName_Test);
		Assert.assertTrue(listPos != null, "必须先知道有多少台有效Pos才能为所有Pos创建同步块从表");
		for (Pos pos : listPos) {
			syncCacheDispatcher.setPos_ID(pos.getID());
			Map<String, Object> createParam = syncCacheDispatcher.getCreateParam(BaseAction.INVALID_ID, syncCacheDispatcher);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			mapperDispather.create(createParam);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString() + "\r\n syncCacheDispatcher=" + syncCacheDispatcher);
		}
		//
		Map<String, Object> paramsForDelete2 = syncCache.getDeleteParam(BaseBO.INVALID_CASE_ID, syncCache);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		mapperSyncCache.delete(paramsForDelete2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString() + "\r\n syncCache=" + syncCache);
	}
}
