package com.bx.erp.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Pos.EnumStatusPos;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

@Controller
public class BaseSyncAction extends BaseAction {
	private Log logger = LogFactory.getLog(BaseSyncAction.class);

	/** 不断自增，标记同步块在客户端同步的顺序。<br />
	 * 客户端收到N个同步块后，先排序，再逐个将这些块写入客户端的DB。<br />
	 * 多线程安全问题：本变量不需要加锁，因为每个ACTION都是互斥运行的。<br />
	 * 缺陷：理论上存在过大溢出的bug。实际上在产品的迭代过程中，不可能会有机会溢出 */
	protected static AtomicInteger aiSequenceSyncBlockForCommodityAndBarcodes = new AtomicInteger();

	protected int getSequence() {
		throw new RuntimeException("Not yet implemented!");
	}

	@Resource
	protected PosBO posBO;

	protected BaseBO getSyncCacheBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseBO getSyncCacheDispatcherBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseBO getModelBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseModel getModel() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected EnumCacheType getCacheType() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected EnumSyncCacheType getSyncCacheType() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected SyncCache getSyncCache() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseSyncCache getSyncCacheModel() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		throw new RuntimeException("Not yet implemented!");
	}

	/** 获取正确的Case ID。<br/>
	 * 同步表主表和从表存储特定对象的同步信息，要删除特定对象的同步信息，需要定义特殊的Case ID以调用正确的Mapper/SP。
	 * 
	 * @return */
	// protected int getCaseIDToDeleteSameCache() {
	// throw new RuntimeException("Not yet implemented!");
	// }

	/** 删除从表
	 * 
	 * @param bmMasterFromDB
	 * @param model
	 * @param req
	 * @param ec
	 * @return */
	protected EnumErrorCode deleteSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		throw new RuntimeException("Not yet implemented!");
	}

	/** 插入从表。并建立和主表对象bmMasterFromDB的关系。<br />
	 * 插入从表可能导致其它表被修改,这时应该更新其它表的缓存和同存。更新同存的前提是POS的数量>1
	 * 
	 * @param bmMasterFromDB
	 * @param model
	 * @param req
	 * @param ec
	 * @return */
	protected EnumErrorCode createSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseModel createDBObjectWithDuplicatedError(BaseModel bmFromDB) {
		throw new RuntimeException("Not yet implemented!");
	}

	/** 向DB中插入数据，可能需要插主表和从表。
	 * 
	 * @param baseModel
	 * @param model
	 * @param req
	 * @param ec
	 * @return 返回的是主表的MODEL，其中可能附有从表的信息 */
	protected BaseModel createDBObject(Boolean useSYSTEM, BaseModel baseModel, ModelMap model, HttpServletRequest req, ErrorInfo ec, String dbName) {
		logger.info("向DB中插入数据，可能需要插主表和从表");
		logger.info("baseModel=" + baseModel);

		BaseModel bmFromDB = null;
		DataSourceContextHolder.setDbName(dbName);
		bmFromDB = getModelBO().createObject((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID, baseModel);
		ec.setErrorCode(getModelBO().getLastErrorCode());
		ec.setErrorMessage(getModelBO().getLastErrorMessage());
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			if (ec.getErrorCode() == EnumErrorCode.EC_Duplicated) {
				return createDBObjectWithDuplicatedError(bmFromDB);
			}
			logger.info("创建普通对象失败，错误码=" + ec.getErrorCode() + ",错误信息=" + ec.getErrorMessage());
			return null;
		} else {
			logger.info("创建普通对象成功，bmFromDB=" + bmFromDB);
		}

		ec.setErrorCode(createSlaveDBObjects(bmFromDB, model, req, dbName));
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("createSlaveDBObjects()错误码=" + ec.getErrorCode() + ",错误信息=" + ec.getErrorMessage());
			return null;
		}

		return bmFromDB;
	}

	/** 更新DB中的主表数据。如果有从表，处理从表时，需要先删除从表，再插从表。
	 * 
	 * @param baseModel
	 * @param model
	 * @param req
	 * @param ec
	 * @return 返回的是主表的MODEL，其中可能附有从表的信息 */
	protected BaseModel updateDBObject(Boolean useSYSTEM, int caseID, BaseModel baseModel, ModelMap model, HttpServletRequest req, ErrorInfo ec, String dbName) {
		logger.info("更新DB中的主从表数据。baseModel=" + baseModel);

		BaseModel bmFromDB = null;
		DataSourceContextHolder.setDbName(dbName);
		bmFromDB = getModelBO().updateObject((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()), caseID, baseModel);
		ec.setErrorCode(getModelBO().getLastErrorCode());
		ec.setErrorMessage(getModelBO().getLastErrorMessage());
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			return null;
		} else {
			logger.info("修改成功,bmFromDB=" + bmFromDB);
		}

		ec.setErrorCode(deleteSlaveDBObjects(bmFromDB, model, req, dbName));
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("删除从表失败(如果有)!");
			return null;
		} else {
			logger.info("删除从表成功(如果有)!");
		}

		ec.setErrorCode(createSlaveDBObjects(bmFromDB, model, req, dbName));
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("创建从表失败(如果有)!");
			return null;
		} else {
			logger.info("创建从表成功(如果有)!");
		}

		return bmFromDB;
	}

	/** 更新DB中的同步对象
	 * 
	 * @param validPosID
	 *            必须为有效的POS的ID或BaseAction.INVALID_POS_ID
	 * @param objSequence
	 *            POS端收到N个同步块后，同步此块的顺序 */
	protected List<List<BaseModel>> updateSyncDBObject(Boolean useSYSTEM, int validPosID, HttpServletRequest req, int iBaseModelID, int objSequence, String syncType, ErrorInfo ec, String dbName) {
		logger.info("更新DB中的同步对象");

		BaseSyncCache vscCreate = getSyncCacheModel();
		vscCreate.setSyncData_ID(iBaseModelID);
		vscCreate.setSyncType(syncType);
		vscCreate.setSyncSequence(objSequence);
		vscCreate.setPosID(validPosID);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> list = getSyncCacheBO().createObjectEx((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID, vscCreate);
		ec.setErrorCode(getSyncCacheBO().getLastErrorCode());
		ec.setErrorMessage(getSyncCacheBO().getLastErrorMessage());
		if (getSyncCacheBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("创建DB中的同步对象失败，错误码=" + ec.getErrorCode() + ",错误信息=" + ec.getErrorMessage());
			return null;
		} else {
			logger.info("同步对象的DB表插入的数据为:" + list);
		}

		return list;
	}

	protected void handleCreateExWithDuplictedError(BaseModel bmFromDB, ErrorInfo ec, HttpServletRequest req, String dbName) {
		throw new RuntimeException("Not yet implemented!");
	}

	/** @param useSYSTEM
	 *            是否使用系统权限
	 * @throws CloneNotSupportedException
	 */
	protected Map<String, Object> handleCreateEx(Boolean useSYSTEM, BaseModel baseModel, ModelMap model, HttpServletRequest req, String dbName) throws CloneNotSupportedException {
		boolean bReturnObject = baseModel.getReturnObject() == 1;
		logger.info("baseModel=" + baseModel);

		BaseModel bmFromDB = null;
		ErrorInfo ec = new ErrorInfo();
		do {
			// 1、更新普通对象的DB表，可能有从表数据
			DataSourceContextHolder.setDbName(dbName);
			bmFromDB = createDBObject(useSYSTEM, baseModel, model, req, ec, dbName);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				if (ec.getErrorCode() == EnumErrorCode.EC_Duplicated) {
					handleCreateExWithDuplictedError(bmFromDB, ec, req, dbName);
				}
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("创建普通对象失败！错误码=" + ec.getErrorCode() + ",错误信息=" + ec.getErrorMessage());
				}
				break;
			} else {
				logger.info("创建普通对象成功：" + bmFromDB);
				bmFromDB.setSyncDatetime(new Date());
			}

			// 2、更新普通对象的缓存
			CacheManager.getCache(dbName, getCacheType()).write1(bmFromDB, dbName, (useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()));

			// 获得POS的数目。如果POS的数目为1，则不用维护缓存，不需要执行以下if体内的代码；否则必须执行之，因为其它POS机需要同步此同步块
			int iPosSize = getPosSize(useSYSTEM, ec, dbName, req);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				break;
			} else {
				logger.info("获取POS机的数目成功，iPosSize=" + iPosSize);
			}

			int posID = getLoginPOSID(req);
			if (posID == INVALID_POS_ID || iPosSize > 1) {// 不管是不是网页端操作的，都需要更新调度表
				logger.info(posID == INVALID_POS_ID ? "网页端在操作，需要创建同步块" : "非网页端在操作，POS机数量>1，即将创建同步块...");
				// 3、更新同步对象的DB表
				int objSequence = getSequence();
				List<List<BaseModel>> list = updateSyncDBObject(useSYSTEM, posID, req, bmFromDB.getID(), objSequence, SyncCache.SYNC_Type_C, ec, dbName);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("更新同步对象的DB表失败！错误码=" + ec.getErrorCode() + ",错误信息=" + ec.getErrorMessage());
					break;
				}
				logger.info("成功更新同步对象的DB表。list=" + list);

				// 4、更新同步块缓存
				updateSyncCacheObjectInMemory(useSYSTEM, SyncCache.SYNC_Type_C, getSyncCacheType(), objSequence, list, dbName, req);
			}

			break;
		} while (false);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
		params.put(BaseAction.KEY_HTMLTable_Parameter_msg, ec.getErrorMessage().toString());
		if (bReturnObject) {
			params.put(KEY_Object, bmFromDB);
		}

		logger.info("handleCreateEx()返回的params=" + params.toString());

		return params;
	}

	protected Map<String, Object> handleUpdateEx(Boolean useSYSTEM, int caseID, BaseModel baseModel, ModelMap model, HttpServletRequest req, String dbName) throws CloneNotSupportedException {
		boolean bReturnObject = baseModel.getReturnObject() == 1;

		BaseModel bmFromDB = null;
		ErrorInfo ec = new ErrorInfo();
		do {
			logger.info("即将修改对象：" + baseModel);
			// 1、更新普通对象的DB表
			bmFromDB = updateDBObject(useSYSTEM, caseID, baseModel, model, req, ec, dbName);
			ec.setErrorCode(getModelBO().getLastErrorCode());
			ec.setErrorMessage(getModelBO().getLastErrorMessage().toString());
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				break;
			} else {
				logger.info("修改对象成功：" + bmFromDB);
				bmFromDB.setSyncDatetime(new Date());
			}

			// 2、更新普通对象的缓存
			CacheManager.getCache(dbName, getCacheType()).write1(bmFromDB, dbName, (useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()));

			// 获得POS的数目。如果POS的数目为1，则不用维护缓存，不需要执行以下if体内的代码；否则必须执行之，因为其它POS机需要同步此同步块
			int iPosSize = getPosSize(useSYSTEM, ec, dbName, req);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				break;
			} else {
				logger.info("获取成功！POS的数目=" + iPosSize);
			}

			int posID = getLoginPOSID(req);
			if (posID == INVALID_POS_ID || iPosSize > 1) {// 不管是不是网页端操作的，都需要更新调度表
				logger.info(posID == INVALID_POS_ID ? "网页端在操作，需要更新同步块" : "非网页端在操作，POS机数量>1，即将更新同步块...");
				// 3、更新同步对象的DB表
				int objSequence = getSequence();
				List<List<BaseModel>> list = updateSyncDBObject(useSYSTEM, posID, req, bmFromDB.getID(), objSequence, SyncCache.SYNC_Type_U, ec, dbName);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					break;
				}
				logger.info("更新后:" + list);

				// 4、更新同步块缓存
				updateSyncCacheObjectInMemory(useSYSTEM, SyncCache.SYNC_Type_U, getSyncCacheType(), objSequence, list, dbName, req);
			} else {
				logger.info("pos的数量少于两个,iPosSize=" + iPosSize);
			}

			break;
		} while (false);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
		params.put(KEY_HTMLTable_Parameter_msg, ec.getErrorMessage().toString());
		if (bReturnObject) {
			params.put(KEY_Object, bmFromDB);
		}

		return params;
	}

	protected Map<String, Object> handleDeleteEx(Boolean useSYSTEM, BaseModel baseModel, ModelMap model, HttpServletRequest req, String dbName, int iCaseID) throws CloneNotSupportedException {
		logger.info("baseModel=" + baseModel);

		ErrorInfo ec = new ErrorInfo();
		do {
			// 1、删除普通对象的DB表
			DataSourceContextHolder.setDbName(dbName);
			BaseModel bmFromDB = getModelBO().deleteObject((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()), iCaseID, baseModel);
			ec.setErrorCode(getModelBO().getLastErrorCode());
			ec.setErrorMessage(getModelBO().getLastErrorMessage().toString());
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("删除普通对象失败，错误码=" + ec.getErrorCode() + ",错误信息=" + ec.getErrorMessage());
				}
				break;
			} else {
				logger.info("删除普通对象成功");
			}
			bmFromDB = getModel();
			bmFromDB.setID(baseModel.getID());

			// 2、删除普通对象的缓存
			CacheManager.getCache(dbName, getCacheType()).delete1(bmFromDB);

			// 获得POS的数目。如果是POS端操作且POS的数目为1，则不用维护缓存，不需要执行以下if体内的代码；否则必须执行之，因为其它POS机需要同步此块
			int iPosSize = getPosSize(useSYSTEM, ec, dbName, req);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("读取POS机的数目错误，错误码=" + ec.getErrorCode());
				break;
			}
			int iCurrentPosID = getLoginPOSID(req);
			if (iCurrentPosID == BaseAction.INVALID_POS_ID // 本操作由非POS端（网页端）发出，需要同步到所有POS机
					|| (iCurrentPosID != BaseAction.INVALID_POS_ID && iPosSize > 1) // 本操作由POS端发出且POS数目>1，则需要同步到其它POS机
			) {
				// 3、删除旧的和普通对象相关的DB同步表，包括同步主表和同步从表
				// getSyncCacheBO().deleteObject(BaseBO.STAFF_ID, getCaseIDToDeleteSameCache(),
				// bmFromDB); //已经在deleteObject中删除

				// 4、删除旧的同步对象的缓存
				SyncCacheManager.getCache(dbName, getSyncCacheType()).delete1(bmFromDB);

				// 5、插入D型同步块缓存DB
				BaseSyncCache bsc = new BaseSyncCache();
				bsc.setSyncData_ID(bmFromDB.getID());
				bsc.setSyncType(SyncCache.SYNC_Type_D);
				bsc.setSyncSequence(getSequence());// ...
				bsc.setPosID(iCurrentPosID);
				DataSourceContextHolder.setDbName(dbName);
				List<List<BaseModel>> list = getSyncCacheBO().createObjectEx((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID, bsc);
				logger.info("创建的是：" + list);
				ec.setErrorCode(getSyncCacheBO().getLastErrorCode());
				ec.setErrorMessage(getSyncCacheBO().getLastErrorMessage().toString());
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					break;
				}

				// 标记本POS已经同步了此块
				if (iCurrentPosID != BaseAction.INVALID_POS_ID) { // 网页端无POS会话，无有效的POS的ID
					BaseSyncCacheDispatcher bscd = new BaseSyncCacheDispatcher();
					bscd.setPos_ID(iCurrentPosID);
					bscd.setSyncCacheID(list.get(0).get(0).getID());
					DataSourceContextHolder.setDbName(dbName);
					getSyncCacheDispatcherBO().createObject((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID, bscd);
					ec.setErrorCode(getSyncCacheDispatcherBO().getLastErrorCode());
					ec.setErrorMessage(getSyncCacheDispatcherBO().getLastErrorMessage().toString());
					if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
						break;
					}
				}

				// 6、重新加载所有同步块到同存
				logger.info("加载所有同步块到同存");
				getSyncCache().load(dbName);
			} else {
				logger.info("pos机的数量小于2,iPosSize=" + iPosSize);
			}

			// 7、处理相关对象的缓存、同存。
			deleteRelatedObjectsAfterDeleteEx(iPosSize, baseModel, ec, model, req, dbName);

			break;
		} while (false);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
		params.put(KEY_HTMLTable_Parameter_msg, ec.getErrorMessage().toString());

		logger.info("handleDeleteEx()返回的数据=" + params);

		return params;
	}

	/** 调用handleDeleteEx()后，相关对象可能在上面的操作过程中出现被删除或修改的情况，对应的缓存和同存需要更新
	 * 
	 * @param ec
	 * @throws CloneNotSupportedException
	 */
	protected void deleteRelatedObjectsAfterDeleteEx(int iPosSize, BaseModel baseModel, ErrorInfo ec, ModelMap model, HttpServletRequest req, String dbName) throws CloneNotSupportedException {
		logger.info("不需要更新相关对象的缓存和同存");
	}

	protected Map<String, Object> handleRetrieve1Ex(BaseModel baseModel, ModelMap model, HttpServletRequest req) {
		return null;
	}

	/** 1、 POS请求同步块。 <br />
	 * 2、查找同步块缓存，并返回相应的普存对象的列表 */
	protected Map<String, Object> handleRetrieveNEx(int posID, BaseModel baseModel, ModelMap model, HttpServletRequest req, String dbName) {
		logger.info("查找同步块缓存，并返回相应的普存对象的列表");

		Map<String, Object> params = new HashMap<String, Object>();
		ErrorInfo ec = new ErrorInfo();
		ec.setErrorCode(EnumErrorCode.EC_NoError);
		final List<BaseModel> vsInfoList = SyncCacheManager.getCache(dbName, getSyncCacheType()).readN(false, false);
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		for (BaseModel bm : vsInfoList) {
			final BaseSyncCache syncCache = (BaseSyncCache) bm;

			// 没有同步的话，才将数据发回给POS以便它同步
			SyncCache sc = (SyncCache) SyncCacheManager.getCache(dbName, getSyncCacheType());
			if (!sc.checkIfPosSyncThisBlock(syncCache.getID(), posID)) {
				logger.info("POS(ID=" + posID + ")未同步块:" + syncCache + "。正在获取块的信息...");
				BaseModel v = getModel();
				v.setID(syncCache.getSyncData_ID());
				BaseModel bmTmp = null;
				if (!syncCache.getSyncType().equals(SyncCache.SYNC_Type_D)) {
					logger.info("->非D型同步");
					DataSourceContextHolder.setDbName(dbName);
					bmTmp = getModelBO().retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, v); // 系统初始化时需要加载所有同存DB
					ec.setErrorCode(getModelBO().getLastErrorCode());
					ec.setErrorMessage(getModelBO().getLastErrorMessage().toString());
					if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
						params.put(KEY_HTMLTable_Parameter_msg, ec.getErrorMessage().toString());
						logger.info("返回的普存对象失败，错误码=" + ec.getErrorCode());
						break;
					} else {
						logger.info("返回的普存对象成功：" + bmTmp);
					}

					logger.info("->主表数据：" + bmTmp);
					if (retrieveNSlaveDBObjects(bmTmp, dbName, req)) { // 设置bmTmp的从表数据
						ec.setErrorCode(getModelBO().getLastErrorCode());
						ec.setErrorMessage(getModelBO().getLastErrorMessage().toString());
						if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
							logger.info("->加载从表数据失败，错误码=" + ec.getErrorCode());
							break;
						} else {
							logger.info("->加载从表数据成功，bmTmp=" + bmTmp);
						}
					}
				} else { // D型同步
					logger.info("->D型同步");
					bmTmp = getModel();
					bmTmp.setID(syncCache.getSyncData_ID());
				}
				if (bmTmp != null) {
					bmTmp.setSyncType(syncCache.getSyncType());
					bmTmp.setSyncSequence(syncCache.getSyncSequence()); // 设置此DB对象的同步次序，以便POS收到该对象后，知道以什么顺序同步这个对象
					bmTmp.setSyncDatetime(new Date());
					bmList.add(bmTmp);
				} else {
					logger.info("->即将同步的对象为null!"); // 不可能事件
				}
			}
		}

		System.out.println("RN出的数据：" + bmList);
		params.put(KEY_ObjectList, bmList);
		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());

		logger.info("handleRetrieveNEx()返回的数据=" + params);

		return params;
	}

	/** 如果一个对象有从表信息，则获取之，并设置进bmTmp中；否则返回false。
	 * 
	 * @param bmTmp
	 *            主表MODEL对象。其内可能有从表的指针
	 * @return false，默认值，没有从表信息。true，有从表信息，但是需要判断错误码，看看是否获取成功 */
	protected boolean retrieveNSlaveDBObjects(BaseModel bmTmp, String dbName, HttpServletRequest req) {
		return false;
	}

	/** 更新同步块的缓存，并标记其同步顺序
	 * 
	 * @param list
	 *            有1个或2个元素。有1个元素时，表明同步块主表有数据，从表没有数据。有2个元素时，主从表都有数据 */
	protected void updateSyncCacheObjectInMemory(Boolean useSYSTEM, String syncCacheType, EnumSyncCacheType enumSyncCacheType, int objSequence, List<List<BaseModel>> list, String dbName, HttpServletRequest req) {
		logger.info("更新同步块的缓存，并标记其同步顺序");

		BaseSyncCache syncCache = null;
		if (list.size() == 1) {
			logger.info("网页端创建的对象，没有POS会话，不需要记录某POS已经同步了某块。但为了方便测试时，进行结果验证，这里还是要将主表写进缓存。");
			syncCache = (BaseSyncCache) list.get(0);
			//
			// 如果是U型，应该把之前相同F_SyncData_ID的U型从同存中清除
			if (syncCacheType.equals(SyncCache.SYNC_Type_U)) {
				SyncCacheManager.getCache(dbName, enumSyncCacheType).deleteOldU((BaseSyncCache) list.get(0));
			}
		} else {
			// 如果是U型，应该把之前相同F_SyncData_ID的U型从同存中清除
			if (syncCacheType.equals(SyncCache.SYNC_Type_U)) {
				SyncCacheManager.getCache(dbName, enumSyncCacheType).deleteOldU((BaseSyncCache) list.get(0).get(0));
			}

			syncCache = ((BaseSyncCache) list.get(0).get(0));

			List<BaseSyncCacheDispatcher> listDispatcher = new ArrayList<BaseSyncCacheDispatcher>();
			for (BaseModel bm : list.get(1)) {
				listDispatcher.add((BaseSyncCacheDispatcher) bm);
			}
			syncCache.setListSlave1(listDispatcher);// 设置同步块调度表信息（从表信息）
		}
		syncCache.setSyncSequence(objSequence);
		SyncCacheManager.getCache(dbName, enumSyncCacheType).write1(syncCache, dbName, (useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()));
	}

	// ...TODO 如果缓存中一开始就加载全部POS，则可直接取得总数
	protected int getPosSize(Boolean useSYSTEM, ErrorInfo ec, String dbName, HttpServletRequest req) {
		Pos pos = new Pos();
		pos.setPos_SN("");
		pos.setStatus(EnumStatusPos.ESP_Active.getIndex());
		pos.setShopID(BaseAction.INVALID_ID);
		DataSourceContextHolder.setDbName(dbName);
		List<?> listPos = posBO.retrieveNObject((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID, pos);
		ec.setErrorCode(posBO.getLastErrorCode());
		ec.setErrorMessage(posBO.getLastErrorMessage().toString());

		logger.info("读取到的POS的总数=" + listPos.size());

		return listPos.size();
	}

	/** 网页端登录时，POS会话存在但POSID=INVALID_POS_ID。相应的SP内要作正确处理 */
	protected int getLoginPOSID(HttpServletRequest req) {
		Pos pos = (Pos) req.getSession().getAttribute(EnumSession.SESSION_POS.getName());
		return pos.getID();
	}

	protected Map<String, Object> handleFeedbackEx(Boolean useSYSTEM, BaseModel bm, ModelMap model, HttpServletRequest req, String dbName) {
		Map<String, Object> param = new HashMap<String, Object>();

		String ids = GetStringFromRequest(req, "sID", String.valueOf(BaseAction.INVALID_ID)).trim();
		String errorCode = GetStringFromRequest(req, "errorCode", String.valueOf(BaseAction.INVALID_ID)).trim();
		if (ids.equals(String.valueOf(BaseAction.INVALID_ID)) || errorCode.equals(String.valueOf(BaseAction.INVALID_ID))) {
			logger.info("参数为空");
			return null;
		}

		// 此POS已经成功同步了一个块
		if (errorCode.equals(EnumErrorCode.EC_NoError.toString())) {// ...判断POS机返回的错误码是否为EC_NoError
			Integer[] iArrID = GeneralUtil.toIntArray(ids);
			if (iArrID == null || iArrID.length == 0) {
				logger.info("传入的参数不符合要求" + ids);
				return null;
			}
			List<Integer> listObjID = GeneralUtil.sortAndDeleteDuplicated(iArrID);

			int posID = getLoginPOSID(req);
			if (posID != BaseAction.INVALID_POS_ID) {// 如果是网页端操作的，不需要更新调度表
				logger.info("POS" + posID + "说它已经同步的对象ID是（已经消除重复）：" + listObjID + "		服务器即将记录该信息进DB");
				if (updateSyncCacheAfterPOSSync(useSYSTEM, posID, listObjID, dbName, req) != EnumErrorCode.EC_NoError) {
					String log = "操作失败：记录POS" + posID + "已经同步了块（对应的对象ID={" + iArrID.toString() + "}），通过更新同存及同存DB";
					logger.info(log);
					param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
					param.put(KEY_HTMLTable_Parameter_msg, log);
					return param;
				}

				// 如果所有POS机都已经同步了这个块，那就从同存及同存DB表中都删除这个块的相关信息
				deleteSyncCacheObjectsSinceAllPOSSync(useSYSTEM, iArrID, dbName, req);

				// 更新同存
				getSyncCache().load(dbName); // ...将来重构成直接从同存中增加这个POS+块已经同步的信息
			}
		}
		logger.info("Feedback后的同存信息：" + SyncCacheManager.getCache(dbName, getSyncCacheType()).readN(false, false));
		logger.info("handleFeedbackEx()返回的数据=" + param);

		param.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);

		return param;
	}

	/** 记录POS已经同步了某些块，通过更新同存及同存DB
	 * 
	 * @param iPosID
	 *            同步了块的POS机
	 * @param iArrID
	 *            同步块对应的对象的ID数组（不是同步块ID）
	 * @return */
	protected EnumErrorCode updateSyncCacheAfterPOSSync(Boolean useSYSTEM, int iPosID, List<Integer> listObjID, String dbName, HttpServletRequest req) {
		List<BaseModel> listSyncCache = SyncCacheManager.getCache(dbName, getSyncCacheType()).readN(false, false);
		logger.info("listObjID为：" + listObjID);
		logger.info("listSyncInfo为：" + listSyncCache);
		for (BaseModel bm : listSyncCache) {
			BaseSyncCache syncCache = (BaseSyncCache) bm;

			for (int iID : listObjID) {
				if (syncCache.getSyncData_ID() == iID) {
					// 更新同存DB。在同存DB中记录此块已经被此POS同步
					BaseSyncCacheDispatcher vscd = getSyncCacheDispatcher();
					vscd.setPos_ID(iPosID);
					vscd.setSyncCacheID(syncCache.getID());
					//
					DataSourceContextHolder.setDbName(dbName);
					vscd = (BaseSyncCacheDispatcher) getSyncCacheDispatcherBO().createObject((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID, vscd);
					if (getSyncCacheDispatcherBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
						// ...
						logger.info("同存DB更新失败，块ID=" + vscd + ", 对象ID=" + iID + ", POS ID=" + iPosID + "\t错误码:" + getSyncCacheDispatcherBO().getLastErrorCode());
						return getSyncCacheDispatcherBO().getLastErrorCode();
					}
					logger.info("同存DB已经成功更新，对象ID=" + iID + ", POS ID=" + iPosID + "。块信息：" + syncCache);
				}
			}
		}

		return EnumErrorCode.EC_NoError;
	}

	/** 删除同存DB和同存。 <br />
	 * 当所有POS机都已经同步了某个块，则应当删除这个块相关的同存DB及同存。
	 * 
	 * @param iArrID
	 *            块ID的数组 */
	protected void deleteSyncCacheObjectsSinceAllPOSSync(Boolean useSYSTEM, Integer[] iArrID, String dbName, HttpServletRequest req) {
		logger.info("删除同存DB和同存,iArrID=" + iArrID);

		for (int iID : iArrID) {
			BaseSyncCache bsc = getSyncCacheModel();
			bsc.setSyncData_ID(iID);

			DataSourceContextHolder.setDbName(dbName);
			getSyncCacheBO().deleteObject((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID, bsc);
			if (getSyncCacheBO().getLastErrorCode() == EnumErrorCode.EC_NoError) {// 表明所有POS机都已经同步了这个块，应当删除相关的同存
				BaseModel bm = getModel();
				bm.setID(iID);
				SyncCacheManager.getCache(dbName, getSyncCacheType()).delete1(bm);
				System.out.println("所有POS机都已经同步了这个块" + iID + "，删除了相关的同存。删除块" + iID + "相关同存的错误码=" + getSyncCacheBO().getLastErrorCode());
				logger.info("所有POS机都已经同步了这个块" + iID + "，删除了相关的同存。删除块" + iID + "相关同存的错误码=" + getSyncCacheBO().getLastErrorCode());
			} else {
				System.out.println("并非所有POS机都已经同步了这个块ID=" + iID + "，所以并未删除这个块相关的同存DB数据。");
				logger.info("并非所有POS机都已经同步了这个块ID=" + iID + "，所以并未删除这个块相关的同存DB数据。");
			}
		}
	}

	// public Map<String, Object> handleDeleteListEx(Boolean useSYSTEM, BaseModel
	// baseModel, ModelMap model, HttpServletRequest req, String dbName) {
	// String sObjIDs = GetStringFromRequest(req, "objectListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sObjIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("参数异常");
	// return null;
	// } else {
	// logger.info("sObjIDs=" + sObjIDs);
	// }
	//
	// int[] iaObjID = toIntArray(sObjIDs);
	// if (iaObjID == null) {
	// logger.info("参数缺失");
	// return null;
	// } else {
	// logger.info("iaObjID=" + iaObjID);
	// }
	//
	// Map<String, Object> params = getDefaultParamToReturn(false);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// ErrorInfo ec = new ErrorInfo();
	// for (int i = 0; i < iaObjID.length; i++) {
	// // 删除DB普通对象
	// BaseModel bm = getModel();
	// bm.setID(iaObjID[i]);
	// DataSourceContextHolder.setDbName(dbName);// ...
	// getModelBO().deleteObject((useSYSTEM ? BaseBO.SYSTEM :
	// getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID, bm);
	// //
	// if (iaObjID.length == 1) {
	// if (getModelBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// params.put(BaseAction.JSON_ERROR_KEY, getModelBO().getLastErrorCode());
	// break;
	// }
	// } else {
	// if (getModelBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// if (i == 0) {
	// params.put(BaseAction.JSON_ERROR_KEY, getModelBO().getLastErrorCode());//
	// 删除第1个对象时，已经失败了，后面将不删除其它对象
	// } else {
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_PartSuccess.toString());
	// params.put("msg", "选中的对象只有部分删除失败，请重试");
	// }
	// break;
	// }
	// }
	//
	// // 删除DB普通缓存
	// CacheManager.getCache(dbName, getCacheType()).delete1(bm);
	//
	// // 获得POS的数目。如果POS的数目为1，则不用维护缓存，不需要执行以下if体内的代码；否则必须执行之，因为其它POS机需要同步此块
	// int iPosSize = getPosSize(useSYSTEM, ec, dbName, req);
	// if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
	// params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
	// break;
	// } else {
	// logger.info("pos机的数目=" + iPosSize);
	// }
	//
	// if (iPosSize > 1) {
	// // 3、删除旧的和普通对象相关的DB同步表，包括同步主表和同步从表
	// // getSyncCacheBO().deleteObject(BaseBO.STAFF_ID,
	// getCaseIDToDeleteSameCache(),
	// // bmFromDB); //已经在deleteObject中删除
	//
	// // 4、删除旧的同步对象的缓存
	// SyncCacheManager.getCache(dbName, getSyncCacheType()).delete1(bm);
	//
	// int iCurrentPosID = getLoginPOSID(req);
	//
	// // 5、插入D型同步块缓存DB
	// BaseSyncCache bsc = new BaseSyncCache();
	// bsc.setSyncData_ID(bm.getID());
	// bsc.setSyncType(SyncCache.SYNC_Type_D);
	// bsc.setSyncSequence(getSequence());// ...
	// bsc.setInt1(iCurrentPosID);
	// //
	// DataSourceContextHolder.setDbName(dbName);
	// List<List<BaseModel>> list = getSyncCacheBO().createObjectEx((useSYSTEM ?
	// BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()),
	// BaseBO.INVALID_CASE_ID, bsc);
	// ec.setErrorCode(getSyncCacheBO().getLastErrorCode());
	// ec.setErrorMessage(getSyncCacheBO().getLastErrorMessage().toString());
	// if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
	// getSyncCache().loadAll(dbName);// 从同存DB重新加载所有同存
	// break;
	// } else {
	// logger.info("创建的是：" + list);
	// }
	//
	// // 标记本POS已经同步了此块
	// if (iCurrentPosID != BaseAction.INVALID_POS_ID) { // 网页端无POS会话，无有效的POS的ID
	// BaseSyncCacheDispatcher bscd = new BaseSyncCacheDispatcher();
	// bscd.setPos_ID(iCurrentPosID);
	// bscd.setSyncCacheID(list.get(0).get(0).getID());
	// DataSourceContextHolder.setDbName(dbName);// ...
	// getSyncCacheDispatcherBO().createObject((useSYSTEM ? BaseBO.SYSTEM :
	// getStaffFromSession(req.getSession()).getID()), BaseBO.INVALID_CASE_ID,
	// bscd);
	// ec.setErrorCode(getSyncCacheDispatcherBO().getLastErrorCode());
	// ec.setErrorMessage(getSyncCacheDispatcherBO().getLastErrorMessage().toString());
	// if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
	// params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
	// break;
	// }
	// } else {
	// logger.info("pos机的数目小于2个,iPosSize=" + iPosSize);
	// }
	//
	// // 6、重新加载所有同步块到同存
	// getSyncCache().loadAll(dbName);
	// }
	// if (i == iaObjID.length - 1) {
	// logger.info("删除多个对象成功");
	// }
	// }
	//
	// return params;
	// }
}
