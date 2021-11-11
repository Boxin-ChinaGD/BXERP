package com.bx.erp.test.robot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.Shared;
import com.bx.erp.test.robot.Config.EnumXlsBarcodesColumnName;
import com.bx.erp.test.robot.Config.EnumXlsCombinationCommodityColumnName;
import com.bx.erp.test.robot.Config.EnumXlsCommodityColumnName;
import com.bx.erp.test.robot.Config.EnumXlsMealColumn;
import com.bx.erp.test.robot.Config.EnumXlsProgramColumnName;
import com.bx.erp.test.robot.Config.EnumXlsPurchasingOrderColumnName;
import com.bx.erp.test.robot.Config.EnumXlsPurchasingOrderCommodityColumnName;
import com.bx.erp.test.robot.Config.EnumXlsWarehousingColumnName;
import com.bx.erp.test.robot.Config.EnumXlsWarehousingCommodityColumnName;
import com.bx.erp.test.robot.model.ProgramAttendee;
import com.bx.erp.test.robot.program.ApprovePurchasingOrder;
import com.bx.erp.test.robot.program.ApproveReturnCommoditySheet;
import com.bx.erp.test.robot.program.ApproveWarehousing;
import com.bx.erp.test.robot.program.CreateCommodity;
import com.bx.erp.test.robot.program.CreatePurchasingOrder;
import com.bx.erp.test.robot.program.CreateReturnCommoditySheet;
import com.bx.erp.test.robot.program.CreateWarehousing;
import com.bx.erp.test.robot.program.Program;
import com.bx.erp.test.robot.program.Program.EnumProgramType;
import com.bx.erp.test.robot.server.RobotServer;
import com.bx.erp.test.robot.server.ServerHandler;
import com.bx.erp.util.OkHttpUtil;

import net.sf.json.JSONObject;

import com.bx.erp.test.robot.program.ProgramPromotion;
import com.bx.erp.test.robot.program.ProgramUnit;
import com.bx.erp.test.robot.program.CreateWarehousing.EnumOperationType;

public class Robot {
	protected Program[] programs;
	protected Date startDatetime;
	protected Date endDatetime;
	protected boolean bRunInRandomMode;

	protected Date readExcelSheetEndDatetime;

	// protected Map<String, List<String>> mapCommodity = new HashMap<String,
	// List<String>>();
	// protected Map<String, List<String>> mapPurchasingOrder = new HashMap<String,
	// List<String>>();
	// protected Map<String, List<String>> mapPurchasingOrderCommodity = new
	// HashMap<String, List<String>>();
	// protected Map<String, List<String>> mapWarehousing = new HashMap<String,
	// List<String>>();
	// protected Map<String, List<String>> mapWarehousingCommodity = new
	// HashMap<String, List<String>>();

	public final int SHIFT_START_HOUR = 8;
	public final int SHIFT_END_HOUR = 20;

	/** 为了单独测试nbr的活动不测试pos的活动, 不用运行pos端的测试 ，免去通信的时间花费*/
	public static boolean bRunNbrOnly = false;

	/*在本次活动序号,nbr或pos机器人没有活动需要执行*/
	public static int noActivityInThisProgramUnitNO = 0;

	public String getErrorInfo() {
		return errorInfo.toString();
	}

	protected StringBuilder errorInfo;

	public Robot(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		this.startDatetime = startDatetime;
		this.endDatetime = endDatetime;
		this.bRunInRandomMode = bRunInRandomMode;
		setPrograms(startDatetime, endDatetime, bRunInRandomMode);
		// 加载机器餐
		if (!bRunInRandomMode) {
			mapBaseModels = Program.loadAll();
		}
	}

	// private Date getStartOfToday(Date currentDatetime) {
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(currentDatetime);
	// cal.set(Calendar.HOUR_OF_DAY, SHIFT_START_HOUR);
	//
	// return cal.getTime();
	// }
	//
	// private Date getEndOfToday(Date currentDatetime) {
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(currentDatetime);
	// cal.set(Calendar.HOUR_OF_DAY, SHIFT_END_HOUR);
	//
	// return cal.getTime();
	// }

	/** 全部机器餐： Key=工作表名称，Value=工作表的内容。内层Map： Key=活动单元编号，Value=一行数据 */
	public static Map<String, Map<String, List<String>>> mapBaseModels;

	protected Queue<ProgramUnit> queueIn;

	public static volatile Queue<ProgramUnit> queueOut;

	/** 初始化所有活动，加载活动单元的数据到内存 */
	public void setPrograms(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		programs = new Program[Program.EnumProgramType.values().length];
		programs[EnumProgramType.EPT_CreateCommodity.getIndex()] = new CreateCommodity(startDatetime, endDatetime, bRunInRandomMode);
		//
		programs[EnumProgramType.EPT_CreatePurchasingOrder.getIndex()] = new CreatePurchasingOrder(startDatetime, endDatetime, bRunInRandomMode);
		programs[EnumProgramType.EPT_ApprovePurchasingOrder.getIndex()] = new ApprovePurchasingOrder(startDatetime, endDatetime, bRunInRandomMode);
		//
		programs[EnumProgramType.EPT_CreateWarehousing.getIndex()] = new CreateWarehousing(startDatetime, endDatetime, bRunInRandomMode);
		programs[EnumProgramType.EPT_ApproveWarehousing.getIndex()] = new ApproveWarehousing(startDatetime, endDatetime, bRunInRandomMode);
		//
		programs[EnumProgramType.EPT_CreateReturnCommoditySheet.getIndex()] = new CreateReturnCommoditySheet(startDatetime, endDatetime, bRunInRandomMode);
		programs[EnumProgramType.EPT_ApproveReturnCommoditySheet.getIndex()] = new ApproveReturnCommoditySheet(startDatetime, endDatetime, bRunInRandomMode);
		//
		programs[EnumProgramType.EPT_ProgramPromotion.getIndex()] = new ProgramPromotion(startDatetime, endDatetime, bRunInRandomMode);

		// programs[EnumProgramType.EPT_CreateCommodity.getIndex()].loadProgramUnit();
		// programs[EnumProgramType.EPT_CreatePurchasingOrder.getIndex()].loadProgramUnit();
		// programs[EnumProgramType.EPT_CreateWarehousing.getIndex()].loadProgramUnit();
	}

	/** 把xls中的内容整理成一个个活动单元。这些活动单元带有足够的信息去完成活动 */
	protected Queue<ProgramUnit> collateProgramUnit(Map<String, Map<String, List<String>>> map) {
		// 拿到活动x工作表
		Map<String, List<String>> mapActivitySheet = mapBaseModels.get(Config.Acitivity_NO[1]);
		// 遍历活动工作表每一行
		Queue<ProgramUnit> ProgramUnitList = new LinkedList<ProgramUnit>();
		ProgramAttendee attendee;
		for (int i = 1; i < mapActivitySheet.size(); i++) {
			// 从第二行开始遍历，第一行是说明列
			List<String> rowActivity = mapActivitySheet.get(i + "");
			if (rowActivity.get(EnumXlsProgramColumnName.EXPCN_ProgramUnitNO.getIndex()).equals("活动单元no")) {
				continue;
			}
			attendee = new ProgramAttendee();
			attendee.setProgramUnitID(Integer.valueOf(rowActivity.get(EnumXlsProgramColumnName.EXPCN_ProgramUnitNO.getIndex())));
			attendee.setNbrProgramUnitID(Integer.valueOf(rowActivity.get(EnumXlsProgramColumnName.EXPCN_NbrMachineMeal.getIndex())));
			attendee.setPos1ProgramUnitID(Integer.valueOf(rowActivity.get(EnumXlsProgramColumnName.EXPCN_Pos1MachineMeal.getIndex())));
			attendee.setPos2ProgramUnitID(Integer.valueOf(rowActivity.get(EnumXlsProgramColumnName.EXPCN_Pos2MachineMeal.getIndex())));
			attendee.setPos3ProgramUnitID(Integer.valueOf(rowActivity.get(EnumXlsProgramColumnName.EXPCN_Pos3MachineMeal.getIndex())));
			// unitID不为0，说明在该活动序号有活动执行，加载相应机器餐
			if (attendee.getNbrProgramUnitID() != noActivityInThisProgramUnitNO) {
				// 拿到nbr机器餐工作表
				Map<String, List<String>> mapNbrMealSheet = mapBaseModels.get(Config.Machine_Meal[0]);
				// 因为机器餐信息具体是商品信息还是采购信息还不清楚，不能直接进行doParse1，所以先按list保存机器餐信息
				List<String> rowNbrMeal = mapNbrMealSheet.get(String.valueOf(attendee.getNbrProgramUnitID()));
				attendee.setNbrMachineMealInfo(rowNbrMeal);
			}
			if (attendee.getPos1ProgramUnitID() != noActivityInThisProgramUnitNO) {
				// 拿到pos1机器餐工作表
				Map<String, List<String>> mapNbrMealSheet = mapBaseModels.get(Config.Machine_Meal[1]);
				// 因为机器餐信息具体是商品信息还是采购信息还不清楚，不能直接进行doParse1，所以先按list保存机器餐信息
				List<String> rowNbrMeal = mapNbrMealSheet.get(String.valueOf(attendee.getPos1ProgramUnitID()));
				attendee.setPos1MachineMealInfo(rowNbrMeal);
			}
			if (attendee.getPos2ProgramUnitID() != noActivityInThisProgramUnitNO) {
				// 拿到pos2机器餐工作表
				Map<String, List<String>> mapNbrMealSheet = mapBaseModels.get(Config.Machine_Meal[2]);
				// 因为机器餐信息具体是商品信息还是采购信息还不清楚，不能直接进行doParse1，所以先按list保存机器餐信息
				List<String> rowNbrMeal = mapNbrMealSheet.get(String.valueOf(attendee.getPos2ProgramUnitID()));
				attendee.setPos2MachineMealInfo(rowNbrMeal);
			}
			if (attendee.getPos3ProgramUnitID() != noActivityInThisProgramUnitNO) {
				// 拿到pos3机器餐工作表
				Map<String, List<String>> mapNbrMealSheet = mapBaseModels.get(Config.Machine_Meal[3]);
				// 因为机器餐信息具体是商品信息还是采购信息还不清楚，不能直接进行doParse1，所以先按list保存机器餐信息
				List<String> rowNbrMeal = mapNbrMealSheet.get(String.valueOf(attendee.getPos3ProgramUnitID()));
				attendee.setPos3MachineMealInfo(rowNbrMeal);
			}
			ProgramUnit pu = new ProgramUnit();
			pu.setNo(Integer.valueOf(rowActivity.get(EnumXlsProgramColumnName.EXPCN_ProgramUnitNO.getIndex())));
			pu.setAttendee(attendee);
			ProgramUnitList.add(pu);
		}

		return ProgramUnitList;
	}

	/** 派给自己或它人吃。派给自己吃的，通过
	 * 
	 * @param pu
	 * @throws Exception
	 */
	protected void assignProgramUnit(ProgramUnit pu, RobotServer minaServer) throws Exception {
		// minaServer
		ProgramAttendee pa = pu.getAttendee();
		// nbr
		if (pa.getNbrProgramUnitID() != noActivityInThisProgramUnitNO) {
			if (pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()).equals(Config.Nbr_Activity[0])) {
				// 商品活动
				System.out.println(" -------- 活动序号：" + pu.getNo() + ",活动类型：" + Config.Nbr_Activity[0] + " -------- ");
				createCommodityProgram(pu, pa);
			} else if (pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()).equals(Config.Nbr_Activity[1])) {
				// 采购订单活动
				System.out.println(" -------- 活动序号：" + pu.getNo() + ",活动类型：" + Config.Nbr_Activity[1] + " -------- ");
				createPurchasingOrderProgram(pu, pa);
			} else if (pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex()).equals(Config.Nbr_Activity[2])) {
				// 入库单活动
				System.out.println(" -------- 活动序号：" + pu.getNo() + ",活动类型：" + Config.Nbr_Activity[2] + " -------- ");
				createWarehousingProgram(pu, pa);
			}
		}
		if (!bRunNbrOnly) {
			// pos1或pos2有餐
			if (pa.getPos1ProgramUnitID() != noActivityInThisProgramUnitNO || pa.getPos2ProgramUnitID() != noActivityInThisProgramUnitNO || pa.getPos3ProgramUnitID() != noActivityInThisProgramUnitNO) {
				System.out.println(" -------- 发给pos，活动序号： " + pu.getNo() + " -------- ");
				minaServer.sendToAll(pu);
			}

			// 这一次pos1没餐，设为已吃餐
			if (pa.getPos1ProgramUnitID() == noActivityInThisProgramUnitNO) {
				minaServer.setPos1Eaten(true);
			}

			// 这一次pos2没餐，设为已吃餐
			if (pa.getPos2ProgramUnitID() == noActivityInThisProgramUnitNO) {
				minaServer.setPos2Eaten(true);
			}

			// 这一次pos3没餐，设为已吃餐
			if (pa.getPos3ProgramUnitID() == noActivityInThisProgramUnitNO) {
				minaServer.setPos3Eaten(true);
			}
		}
	}

	private void createWarehousingProgram(ProgramUnit pu, ProgramAttendee pa) throws Exception {
		List<String> listRowTitle = mapBaseModels.get("入库单主表").get("ID"); // 标题
		List<String> ws = mapBaseModels.get(pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex())).get(pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_MasterTable.getIndex()));
		Map<String, Object> params = new HashMap<String, Object>();
		for (int columnNO = 0; columnNO < listRowTitle.size(); columnNO++) {
			if (ws.get(columnNO) == null || ws.get(columnNO).equals("null")) {
				params.put(listRowTitle.get(columnNO), ws.get(columnNO));
			} else {
				params.put(listRowTitle.get(columnNO), "\"" + ws.get(columnNO) + "\"");
			}
		}
		List<String> listFromTableTitle = mapBaseModels.get("入库单从表").get("ID"); // 标题
		List<String> listWC = new ArrayList<String>();
		for (List<String> wc : mapBaseModels.get("入库单从表").values()) {
			if (wc.get(EnumXlsWarehousingCommodityColumnName.EXCCN_warehousingID.getIndex()).equals(ws.get(EnumXlsWarehousingColumnName.EXCN_ID.getIndex()))) {
				Map<String, Object> mapFromTable = new HashMap<String, Object>();
				for (int columnNO = 0; columnNO < wc.size(); columnNO++) {
					if (wc.get(columnNO) == null || wc.get(columnNO).equals("null")) {
						mapFromTable.put(listFromTableTitle.get(columnNO), wc.get(columnNO));
					} else {
						mapFromTable.put(listFromTableTitle.get(columnNO), "\"" + wc.get(columnNO) + "\"");
					}
				}
				// TODO 时间格式问题，暂时设为""
				mapFromTable.put(WarehousingCommodity.field.getFIELD_NAME_productionDatetime(), "\"\"");
				mapFromTable.put(WarehousingCommodity.field.getFIELD_NAME_expireDatetime(), "\"\"");
				mapFromTable.put(WarehousingCommodity.field.getFIELD_NAME_createDatetime(), "\"\"");
				mapFromTable.put(WarehousingCommodity.field.getFIELD_NAME_updateDatetime(), "\"\"");
				listWC.add(mapFromTable.toString());
			}
		}
		params.put(Warehousing.field.getFIELD_NAME_listSlave1(), listWC);
		params.put(Warehousing.field.getFIELD_NAME_messageID(), "0");
		//
		params.put(Warehousing.field.getFIELD_NAME_createDatetime(), "\"\"");
		params.put(Warehousing.field.getFIELD_NAME_updateDatetime(), "\"\"");
		// 根据.xsl中的purchasingOrderID找到子商品在数据库中的真实ID
		String pOrderIdInExcel = params.get("purchasingOrderID").toString();
		// 两个双引号，去掉一个
		pOrderIdInExcel = pOrderIdInExcel.substring(1, pOrderIdInExcel.length() - 1);
		// 入库单分有采购订单和无采购订单两种
		if (!pOrderIdInExcel.equals("NULL")) {
			int pOrderIdInDB = findRealpOrderIdByExcelpOrderID(Integer.parseInt(pOrderIdInExcel));
			params.put("purchasingOrderID", pOrderIdInDB);
		} else {
			params.put("purchasingOrderID", "0");
		}
		Warehousing warehousing = new Warehousing();
		warehousing = (Warehousing) warehousing.parse1(params.toString());
		switch (EnumOperationType.values()[Integer.parseInt(pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_OperationType.getIndex()))]) {
		case EOT_CreateWarehousing:
			System.out.println("CreateWarehousing");
			warehousing.setReturnObject(1);
			for (Object o : warehousing.getListSlave1()) {
				WarehousingCommodity wc = (WarehousingCommodity) o;
				// wc.setCommodityID(wc.getCommodityID() + Program.maxCommodityID);
				// wc.setBarcodeID(wc.getBarcodeID() + Program.maxBarcodesID);
				int commIdInExcel = wc.getCommodityID();
				// 从机器蜜中查找真正商品ID
				int comIdInDB = findRealCommodityIdByExcelCommodityID(commIdInExcel);
				Commodity comm = new Commodity();
				comm.setID(comIdInDB);
				List<Barcodes> barcodeList = CreateCommodity.retrieveNBarcodeEx(comm);
				int barcodeIdInDB = barcodeList.get(0).getID();
				wc.setCommodityID(comIdInDB);
				wc.setBarcodeID(barcodeIdInDB);
			}
			Warehousing createW = CreateWarehousing.warehousingCUA("/warehousing/createEx.bx", warehousing);
			if (Program.maxWarehousingID == 0) {
				Program.maxWarehousingID = createW.getID() - 1;
				WarehousingCommodity wc = (WarehousingCommodity) createW.getListSlave1().get(0);
				Program.maxWarehousingCommodityID = wc.getID() - 1;
			}
			pu.setBaseModelOut1(createW);
			queueOut.add(pu);

			break;
		case EOT_ApproveWarehousing:
			System.out.println("ApproveWarehousing");
			int warehousingInDB = findRealpWarehousignIdByExcelpOrderID(warehousing.getID());
			warehousing.setID(warehousingInDB);
			for (Object o : warehousing.getListSlave1()) {
				WarehousingCommodity wc = (WarehousingCommodity) o;
				// wc.setCommodityID(wc.getCommodityID() + Program.maxCommodityID);
				// wc.setBarcodeID(wc.getBarcodeID() + Program.maxBarcodesID);
				int commIdInExcel = wc.getCommodityID();
				// 从机器蜜中查找真正商品ID
				int comIdInDB = findRealCommodityIdByExcelCommodityID(commIdInExcel);
				Commodity comm = new Commodity();
				comm.setID(comIdInDB);
				List<Barcodes> barcodeList = CreateCommodity.retrieveNBarcodeEx(comm);
				int barcodeIdInDB = barcodeList.get(0).getID();
				wc.setCommodityID(comIdInDB);
				wc.setBarcodeID(barcodeIdInDB);
			}
			Warehousing approveW = CreateWarehousing.warehousingCUA("/warehousing/approveEx.bx", warehousing);
			pu.setBaseModelOut1(approveW);
			queueOut.add(pu);

			break;
		case EOT_UpdateWarehousing:
			// 这里set需要更改的参数
			Warehousing updateW = CreateWarehousing.warehousingCUA("/warehousing/updateEx.bx", warehousing);
			pu.setBaseModelOut1(updateW);
			queueOut.add(pu);
			// 下面是审核入库单的
//			pu.setNo(2);
			pu.setOperationType(EnumOperationType.EOT_ApproveWarehousing.getIndex());
			// feed(pu, programs[EnumProgramType.EPT_CreateWarehousing.getIndex()]);

			break;
		case EOT_RetrieveNExByFieldsExWarehousing:
			// 这里set需要模糊查询的参数，不需要查询的参数set为-1或者null
			List<BaseModel> listW = CreateWarehousing.warehousingRNByFieldsEx(warehousing);
			pu.setListBaseModelOut1(listW);
			queueOut.add(pu);

			break;
		case EOT_Retrieve1ExWarehousing:
			// 这里set需要更改的参数
			Warehousing retrieve1ExW = CreateWarehousing.warehousingR1Ex(warehousing);
			pu.setBaseModelOut1(retrieve1ExW);
			queueOut.add(pu);

			break;
		default:
			warehousing = (Warehousing) pu.getBaseModelOut1();
			CreateWarehousing.warehousingD(warehousing);

			break;
		}
	}

	private void createPurchasingOrderProgram(ProgramUnit pu, ProgramAttendee pa) throws Exception {
		List<String> listRowTitle = mapBaseModels.get("采购订单主表").get("ID"); // 标题
		List<String> poStrList = mapBaseModels.get(pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex())).get(pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_MasterTable.getIndex()));
		Map<String, Object> params = new HashMap<String, Object>();
		for (int columnNO = 0; columnNO < listRowTitle.size(); columnNO++) {
			if (poStrList.get(columnNO) == null || poStrList.get(columnNO).equals("null")) {
				params.put(listRowTitle.get(columnNO), poStrList.get(columnNO));
			} else {
				params.put(listRowTitle.get(columnNO), "\"" + poStrList.get(columnNO) + "\"");
			}
		}
		List<String> listFromTableTitle = mapBaseModels.get("采购从表").get("ID"); // 标题
		List<String> listPOC = new ArrayList<String>();
		for (List<String> poc : mapBaseModels.get("采购从表").values()) {
			if (poc.get(EnumXlsPurchasingOrderCommodityColumnName.EXPCCN_purchasingOrderID.getIndex()).equals(poStrList.get(EnumXlsPurchasingOrderColumnName.EXPCN_ID.getIndex()))) {
				Map<String, Object> mapFromTable = new HashMap<String, Object>();
				for (int columnNO = 0; columnNO < poc.size(); columnNO++) {
					if (poc.get(columnNO) == null || poc.get(columnNO).equals("null")) {
						mapFromTable.put(listFromTableTitle.get(columnNO), poc.get(columnNO));
					} else {
						mapFromTable.put(listFromTableTitle.get(columnNO), "\"" + poc.get(columnNO) + "\"");
					}
				}
				// TODO 时间格式问题，暂时设为""
				mapFromTable.put(PurchasingOrderCommodity.field.getFIELD_NAME_createDatetime(), "\"\"");
				mapFromTable.put(PurchasingOrderCommodity.field.getFIELD_NAME_updateDatetime(), "\"\"");
				listPOC.add(mapFromTable.toString());
			}
		}
		params.put(PurchasingOrder.field.getFIELD_NAME_listSlave1(), listPOC);
		//
		params.put(PurchasingOrder.field.getFIELD_NAME_createDatetime(), "\"\"");
		params.put(PurchasingOrder.field.getFIELD_NAME_approveDatetime(), "\"\"");
		params.put(PurchasingOrder.field.getFIELD_NAME_updateDatetime(), "\"\"");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder = (PurchasingOrder) purchasingOrder.parse1(params.toString());
		PurchasingOrder po = new PurchasingOrder();
		switch (CreatePurchasingOrder.EnumOperationType.values()[Integer.parseInt(pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_OperationType.getIndex()))]) {
		case EOT_CreatePurchasingOrder:
			System.out.println("CreatePurchasingOrder");
			// purchasingOrder = (PurchasingOrder) pu.getBaseModelIn1();
			for (Object o : purchasingOrder.getListSlave1()) {
				PurchasingOrderCommodity poc = (PurchasingOrderCommodity) o;
				int commIdInExcel = poc.getCommodityID();
				// 从机器蜜中查找真正商品ID
				int comIdInDB = findRealCommodityIdByExcelCommodityID(commIdInExcel);
				Commodity comm = new Commodity();
				comm.setID(comIdInDB);
				List<Barcodes> barcodeList = CreateCommodity.retrieveNBarcodeEx(comm);
				int barcodeIdInDB = barcodeList.get(0).getID();
				poc.setCommodityID(comIdInDB);
				poc.setBarcodeID(barcodeIdInDB);
			}
			po = CreatePurchasingOrder.purchasingOrderCUA("/purchasingOrder/createEx.bx", purchasingOrder);
			if (CreatePurchasingOrder.maxPurchasingOrderID == 0) {
				CreatePurchasingOrder.maxPurchasingOrderID = po.getID() - 1;
				PurchasingOrderCommodity poc = (PurchasingOrderCommodity) po.getListSlave1().get(0);
				CreatePurchasingOrder.maxPurchasingOrderCommodityID = poc.getID() - 1;
			}
			pu.setBaseModelOut1(po);
			queueOut.add(pu);

			break;
		case EOT_ApprovePurchasingOrder:
			System.out.println("ApprovePurchasingOrder");
			int pOrderIdInDB = findRealpOrderIdByExcelpOrderID(purchasingOrder.getID());
			purchasingOrder.setID(pOrderIdInDB);
			for (Object o : purchasingOrder.getListSlave1()) {
				PurchasingOrderCommodity poc = (PurchasingOrderCommodity) o;
				int commIdInExcel = poc.getCommodityID();
				// 从机器蜜中查找真正商品ID
				int comIdInDB = findRealCommodityIdByExcelCommodityID(commIdInExcel);
				Commodity comm = new Commodity();
				comm.setID(comIdInDB);
				List<Barcodes> barcodeList = CreateCommodity.retrieveNBarcodeEx(comm);
				int barcodeIdInDB = barcodeList.get(0).getID();
				poc.setCommodityID(comIdInDB);
				poc.setBarcodeID(barcodeIdInDB);
			}
			//
			po = CreatePurchasingOrder.purchasingOrderCUA("/purchasingOrder/approveEx.bx", purchasingOrder);
			pu.setBaseModelOut1(po);
			queueOut.add(pu);

			break;
		case EOT_UpdatePurchasingOrder:
			// 这里set需要更改的参数（目前没有参数，所以先这样）
			po = CreatePurchasingOrder.purchasingOrderCUA("/purchasingOrder/updateEx.bx", purchasingOrder);
			pu.setBaseModelOut1(po);
			queueOut.add(pu);

			break;
		case EOT_RetrieveNExPurchasingOrder:
			// 这里set需要模糊查询的参数，不需要查询的参数set为-1或者null
			List<BaseModel> listPO = CreatePurchasingOrder.purchasingOrderRNEx(purchasingOrder);
			pu.setListBaseModelOut1(listPO);
			queueOut.add(pu);

			break;
		case EOT_Retrieve1ExPurchasingOrder:
			// 这里set需要查询的采购订单ID
			po = CreatePurchasingOrder.purchasingOrderR1Ex(purchasingOrder);
			pu.setBaseModelOut1(po);
			queueOut.add(pu);

			break;
		default:
			CreatePurchasingOrder.purchasingOrdeD(purchasingOrder);

			break;
		}
	}

	private void createCommodityProgram(ProgramUnit pu, ProgramAttendee pa) throws InterruptedException {
		List<String> listRowTitle = mapBaseModels.get("商品信息").get("ID"); // 标题
		// 获取商品信息
		// get("商品信息").get("ID")
		List<String> comm = mapBaseModels.get(pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_SheetTableName.getIndex())).get(pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_MasterTable.getIndex()));
		Map<String, Object> params = new HashMap<String, Object>();
		for (int columnNO = 0; columnNO < listRowTitle.size(); columnNO++) {
			if (comm.get(columnNO) == null || comm.get(columnNO).equals("null")) {
				params.put(listRowTitle.get(columnNO), comm.get(columnNO));
			} else {
				params.put(listRowTitle.get(columnNO), "\"" + comm.get(columnNO) + "\"");
			}
		}
		String nameReplace = ((String) params.get(Commodity.field.getFIELD_NAME_name())).replaceAll("\"", "");
		params.put(Commodity.field.getFIELD_NAME_name(), "\"" + (nameReplace + System.currentTimeMillis() % 1000000) + "\"");
		// 使用for循环遍历，找到对应商品ID的条形码
		for (List<String> bar : mapBaseModels.get("条形码").values()) {
			if (bar.get(EnumXlsBarcodesColumnName.EBCN_commodityID.getIndex()).equals(comm.get(EnumXlsCommodityColumnName.ECCN_ID.getIndex()))) {
				params.put(Commodity.field.getFIELD_NAME_barcodes(), bar.get(EnumXlsBarcodesColumnName.EBCN_barcode.getIndex()));
				break;
			}
		}
		// 组合商品
		if (String.valueOf(params.get("type")).equals("\"" + String.valueOf(EnumCommodityType.ECT_Combination.getIndex()) + "\"")) {
			List<String> listFromTableTitle = mapBaseModels.get("组合型商品表").get("ID"); // 标题
			List<String> listSubComm = new ArrayList<String>();
			for (List<String> subComm : mapBaseModels.get("组合型商品表").values()) {
				if (subComm.get(EnumXlsCombinationCommodityColumnName.ECCCN_commodityID.getIndex()).equals(comm.get(EnumXlsCommodityColumnName.ECCN_ID.getIndex()))) {
					Map<String, Object> mapFromTable = new HashMap<String, Object>();
					for (int columnNO = 0; columnNO < subComm.size(); columnNO++) {
						if (subComm.get(columnNO) == null || subComm.get(columnNO).equals("null")) {
							mapFromTable.put(listFromTableTitle.get(columnNO), subComm.get(columnNO));
						} else {
							mapFromTable.put(listFromTableTitle.get(columnNO), "\"" + subComm.get(columnNO) + "\"");
						}
					}
					// TODO 时间格式问题，暂时设为""
					mapFromTable.put(PurchasingOrderCommodity.field.getFIELD_NAME_createDatetime(), "\"\"");
					mapFromTable.put(PurchasingOrderCommodity.field.getFIELD_NAME_updateDatetime(), "\"\"");
					// 根据.xsl中的subCommodityID找到子商品在数据库中的真实ID
					String subCommodiyIdInExcel = mapFromTable.get("subCommodityID").toString();
					// 两个双引号，去掉一个
					subCommodiyIdInExcel = subCommodiyIdInExcel.substring(1, subCommodiyIdInExcel.length() - 1);
					int subCommodiyIdInDB = findRealCommodityIdByExcelCommodityID(Integer.parseInt(subCommodiyIdInExcel));
					mapFromTable.put("subCommodityID", subCommodiyIdInDB);
					listSubComm.add(mapFromTable.toString());
				}
			}
			params.put(Commodity.field.getFIELD_NAME_listSlave1(), listSubComm);
		} else {
			params.put(Commodity.field.getFIELD_NAME_listSlave1(), "[]");
		}
		//
		params.put(Commodity.field.getFIELD_NAME_operatorStaffID(), "0");
		params.put(Commodity.field.getFIELD_NAME_syncType(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_subCommodityInfo(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_packageUnitName(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_startValueRemark(), "\"\"");
		// TODO 时间格式问题，暂时设为""
		params.put(Commodity.field.getFIELD_NAME_createDate(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_createDatetime(), "\"\"");
		params.put(Commodity.field.getFIELD_NAME_updateDatetime(), "\"\"");
		Commodity commodity = new Commodity();
		commodity = (Commodity) commodity.parse1(params.toString());
		commodity.setReturnObject(1);
		commodity.setProviderIDs("1");
		int commodityType = commodity.getType();
		// 多包装商品
		if (commodity.getType() == EnumCommodityType.ECT_MultiPackaging.getIndex()) {
			// 从机器蜜中查找refCommodity
			int refCommodityID = commodity.getRefCommodityID();
			int refCommodityIDfromDB = refCommodityID;
			// TODO .xls创建多包装商品时，对应的参考商品不在.xls怎么办，在.xls加一个？，目前的做法是在.xls加一个
			// if (refCommodityID != 231) {
			// refCommodityIDfromDB = findRealCommodityIdByExcelCommodityID(refCommodityID);
			// }
			//
			refCommodityIDfromDB = findRealCommodityIdByExcelCommodityID(refCommodityID);
			ErrorInfo ecOut = new ErrorInfo();
			Commodity refCommodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(refCommodityIDfromDB, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				throw new RuntimeException("从缓存获取商品失败，错误信息：" + ecOut.getErrorMessage());
			}
			if (refCommodity == null) {
				throw new RuntimeException("从缓存获取商品失败，返回为null");
			}
			List<Barcodes> refBarcodes = CreateCommodity.retrieveNBarcodeEx(refCommodity);
			refCommodity.setBarcodes(refBarcodes.get(0).getBarcode());
			refCommodity.setMultiPackagingInfo(refCommodity.getBarcodes() + "," + commodity.getBarcodes() + ";" + refCommodity.getPackageUnitID() + "," + commodity.getPackageUnitID() + ";1," + commodity.getRefCommodityMultiple() + ";"
					+ refCommodity.getPriceRetail() + "," + commodity.getPriceRetail() + ";" + refCommodity.getPriceVIP() + "," + commodity.getPriceVIP() + ";" + refCommodity.getPriceWholesale() + "," + commodity.getPriceWholesale() + ";"
					+ refCommodity.getName() + "," + commodity.getName() + System.currentTimeMillis() % 1000000 + ";");
			// 创建多包装就是update单品
			commodity = refCommodity;
		} else {
			commodity.setMultiPackagingInfo(commodity.getBarcodes() + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";"
					+ commodity.getPriceWholesale() + ";" + commodity.getName() + ";");
		}
		// 服务商品
		if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
			commodity.setPurchasingUnit(null);
			commodity.setShelfLife(0);
		}
		Commodity com = new Commodity();
		List<Barcodes> listBarcodes = new ArrayList<Barcodes>();
		switch (CreateCommodity.EnumOperationType.values()[Integer.parseInt(pa.getNbrMachineMealInfo().get(EnumXlsMealColumn.EB_OperationType.getIndex()))]) {
		case EOT_CreateCommodity:
			System.out.println("CreateCommodity");
			com = CreateCommodity.createOrUpdateCommodity("/commoditySync/createEx.bx", commodity);
			listBarcodes = CreateCommodity.retrieveNBarcodeEx(com);
			com.setBarcodeID(listBarcodes.get(0).getID());
			// TODO 改用机器蜜了，这里不需要了
			if (Program.maxCommodityID == 0) {
				Program.maxCommodityID = com.getID() - 1;
				Program.maxBarcodesID = com.getBarcodeID() - 1;
			}
			//
			pu.setBaseModelOut1(com);
			queueOut.offer(pu);

			break;
		case EOT_UpdateCommodity:
			// 这里set需要修改的参数
			commodity.setProviderIDs("1");
			commodity.setReturnObject(EnumBoolean.EB_Yes.getIndex());
			com = CreateCommodity.createOrUpdateCommodity("/commoditySync/updateEx.bx", commodity);

			// 如果是多包装商品，机器蜜应该是多包装商品，而不是它对应的单品
			if (commodityType == EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				// 查找单品对应的多包装商品
				Map<String, String> paramsR1 = new HashMap<>();
				paramsR1.put(Commodity.field.getFIELD_NAME_ID(), String.valueOf(commodity.getID()));
				//
				String response = OkHttpUtil.get("/commodity/retrieve1Ex.bx", paramsR1);
				//
				JSONObject object = JSONObject.fromObject(response);
				Commodity commodity1 = new Commodity();
				List<BaseModel> multiCommList = commodity1.parseN(object.getString(BaseAction.KEY_ObjectList));
				if (multiCommList == null || multiCommList.size() == 0) {
					throw new RuntimeException("找不到刚创建的多包装商品");
				}
				// ID最大的这个就是刚创建的多包装商品
				Commodity multiCommNowCreated = new Commodity();
				for (BaseModel bm : multiCommList) {
					if (bm.getID() > multiCommNowCreated.getID()) {
						multiCommNowCreated = (Commodity) bm;
					}
				}
				listBarcodes = CreateCommodity.retrieveNBarcodeEx(multiCommNowCreated);
				com.setBarcodeID(listBarcodes.get(0).getID());
				pu.setBaseModelOut1(multiCommNowCreated);
				queueOut.offer(pu);
			} else {
				listBarcodes = CreateCommodity.retrieveNBarcodeEx(com);
				com.setBarcodeID(listBarcodes.get(0).getID());
				pu.setBaseModelOut1(com);
				queueOut.offer(pu);
			}

			break;
		case EOT_RetrieveNExCommodity:
			commodity = (Commodity) pu.getBaseModelIn1();
			// 这里set需要模糊查询的参数，不查的set为-1
			Commodity comRNEx = CreateCommodity.retrieveNCommodityEx(commodity);
			pu.setBaseModelOut1(comRNEx);
			queueOut.offer(pu);

			break;
		case EOT_Retrieve1ExCommodity:
			commodity = (Commodity) pu.getBaseModelIn1();
			// 这里set需要查询的商品ID
			com = CreateCommodity.retrieve1CommodityEx(commodity);
			listBarcodes = CreateCommodity.retrieveNBarcodeEx(com);
			com.setBarcodeID(listBarcodes.get(0).getID());
			pu.setBaseModelOut1(com);
			queueOut.offer(pu);

			break;
		default:
			CreateCommodity.deleteCommodity((Commodity) pu.getBaseModelIn1());
			break;
		}
	}

	public boolean run() throws Exception {
		// 登录,只会运行一次
		Shared.getHttpStaffLoginSession();
		// TODO maxRetailTradeID已经变成创建退货型零售单时，对应的源零售单的真实ID，考虑重命名
		RetailTrade retailTrade = CreateCommodity.retrieveRetailTradeWithMaxID();
		Program.maxRetailTradeID = retailTrade.getID();
		// 1、在RUN之前，加载xls到内存（已经做）
		// 2、启动服务器。
		System.out.println(" ------------- 启动服务器,等待客户端连接 ------------- ");
		RobotServer minaServer = new RobotServer();
		minaServer.init();
		// 3、等特定数量的客户端连接完毕
		System.out.println(" ------------- 等特定数量的客户端连接完毕 ------------- ");
		if (!bRunNbrOnly) {
			while (Config.TOTAL_CLIENT_Count > minaServer.getClientCount()) {
				Thread.sleep(500);
			}
		}
		// 告诉pos开始运行ShopRobotTest，加载excel数据到内存
		if (!bRunNbrOnly) {
			minaServer.sendMsgToPosForRun();
		}
		// 加载完excel数据到内存后，各pos机器人把名字发给nbr
		System.out.println(" ------------- 等待各个Pos加载完excel数据到内存后,把名字发给nbr ------------- ");
		// TODO 先注释下面代码，nbr自己跑，用于测试nbr活动
		if (!bRunNbrOnly) {
			while (ServerHandler.startPosNum < Config.TOTAL_CLIENT_Count) {
				Thread.sleep(500);
			}
		}
		// 4、看餐、派餐、等吃完通知
		queueIn = collateProgramUnit(mapBaseModels);
		queueOut = new LinkedList<ProgramUnit>();
		System.out.println(" ------------- 本次活动的数量:" + queueIn.size() + " ------------- ");
		while (queueIn.peek() != null) {
			ProgramUnit pu = queueIn.peek();
			// TODO maxRetailTradeID已经变成创建退货型零售单时，对应的源零售单的真实ID，考虑重命名
			Program.maxRetailTradeID = 0;
			// 派餐
			// 每一次派餐前，各个pos机默认都为没吃餐
			// minaServer.setPos1Eaten(false);
			minaServer.setPos1Eaten(true);
			minaServer.setPos2Eaten(false);
			minaServer.setPos3Eaten(false);
			assignProgramUnit(pu, minaServer);
			// nbr吃完后才会执行到下面的while，所以不用判断nbr是否吃完
			// 等待吃完通知
			if (!bRunNbrOnly) {
				while (!minaServer.isPos1Eaten() || !minaServer.isPos2Eaten() || !minaServer.isPos3Eaten()) {
					Thread.sleep(300);
				}
			}
			queueIn.poll();
		}
		// 吃完机器餐，通知各个pos断开连接
		if (!bRunNbrOnly) {
			minaServer.sendMsgToPosForCloseConnect();
		}
		// 等待全部pos机断开连接
		if (!bRunNbrOnly) {
			while (ServerHandler.stopPosNum < Config.TOTAL_CLIENT_Count) {
				Thread.sleep(1000);
			}
		}
		System.out.println(" ------------- 服务端关闭连接 ------------- ");
		// TODO 以下代码是否可以删除
		// 5、没餐，让客户端关闭连接，自己退出
		// System.out.println("exit");

		// errorInfo = new StringBuilder();
		// if (!bRunInRandomMode) {
		// while (Program.currentProgramUnitNO <= mapBaseModels.size()) {
		// if (!programs[EnumProgramType.EPT_CreateCommodity.getIndex()].run(new Date(),
		// errorInfo, programs)) {
		// // ...
		// }
		//
		// if (!programs[EnumProgramType.EPT_CreatePurchasingOrder.getIndex()].run(new
		// Date(), errorInfo, programs)) {
		// // ...
		// }
		//
		// if (!programs[EnumProgramType.EPT_CreateWarehousing.getIndex()].run(new
		// Date(), errorInfo, programs)) {
		// // ...
		// }
		// // if (Program.counter == 4 || Program.counter == 5 || Program.counter == 7
		// ||
		// // Program.counter > 10) {
		// // Program.counter++;
		// // }
		// // System.out.println(Program.counter);
		// }
		// }
		// else {
		// Date currentDatetime = startDatetime;
		// while (currentDatetime.getTime() <= endDatetime.getTime()) {
		// // 设置上班时间、下班时间
		// Date startOfToday = getStartOfToday(currentDatetime);
		// Date endOfToday = getEndOfToday(currentDatetime);
		//
		// Date now = (Date) startOfToday.clone();
		// while (now.getTime() < endOfToday.getTime()) { // 模拟一天内boss主要活动
		// now = DatetimeUtil.addMinutes(now, new Random().nextInt(60) + 1);//
		// 时间逐渐流逝,以分钟为单位
		//
		// if (!programs[EnumProgramType.EPT_CreatePurchasingOrder.getIndex()].run(now,
		// errorInfo, programs)) {// 活动3（创建采购订单）
		// // ...
		// }
		// ((ApprovePurchasingOrder)
		// programs[EnumProgramType.EPT_ApprovePurchasingOrder.getIndex()]).setPurchasingOrderList(((CreatePurchasingOrder)
		// programs[EnumProgramType.EPT_CreatePurchasingOrder.getIndex()]).getListPO());
		// if (!programs[EnumProgramType.EPT_ApprovePurchasingOrder.getIndex()].run(now,
		// errorInfo, programs)) {// 活动0（审核采购订单）
		// // ...
		// }
		//
		// ((CreateWarehousing)
		// programs[EnumProgramType.EPT_CreateWarehousing.getIndex()])
		// .setPurchasingOrderList(((ApprovePurchasingOrder)
		// programs[EnumProgramType.EPT_ApprovePurchasingOrder.getIndex()]).getApprovePurchasingOrderList());
		// if (!programs[EnumProgramType.EPT_CreateWarehousing.getIndex()].run(now,
		// errorInfo, programs)) { // 活动5(入库)
		// // ...
		// }
		//
		// ((ApproveWarehousing)
		// programs[EnumProgramType.EPT_ApproveWarehousing.getIndex()]).setWarehousingList(((CreateWarehousing)
		// programs[EnumProgramType.EPT_CreateWarehousing.getIndex()]).getWarehousingList());
		// if (!programs[EnumProgramType.EPT_ApproveWarehousing.getIndex()].run(now,
		// errorInfo, programs)) { // 活动2(审核入库)
		// // ...
		// }
		//
		// // 仓管退货不关联入库单
		// if
		// (!programs[EnumProgramType.EPT_CreateReturnCommoditySheet.getIndex()].run(now,
		// errorInfo, programs)) { // 活动4(创建仓管退货)
		// // ...
		// }
		//
		// ((ApproveReturnCommoditySheet)
		// programs[EnumProgramType.EPT_ApproveReturnCommoditySheet.getIndex()])
		// .setReturnCommodityList(((CreateReturnCommoditySheet)
		// programs[EnumProgramType.EPT_CreateReturnCommoditySheet.getIndex()]).getListReturnCommodity());
		// if
		// (!programs[EnumProgramType.EPT_ApproveReturnCommoditySheet.getIndex()].run(now,
		// errorInfo, programs)) { // 活动1(审核仓管退货)
		// // ...
		// }
		// // ...循环调用program并写入报表给BA。打印报表
		//
		// }
		// //
		// currentDatetime = DatetimeUtil.getDays(currentDatetime, 1);
		// } // 结束了机器人的活动
		// System.out.println("错误信息：" + errorInfo.toString());
		// }
		return true;

	}

	// // 好像可以不经过活动1表
	// protected Map<String, List<String>> readExcelSheetForActivityUnit() {
	// Map<String, List<String>> mapRows = new HashMap<String, List<String>>(); //
	// sheet表的所有数据
	// Map<String, List<String>> listProgramUnit = new HashMap<String,
	// List<String>>(); // 用于保存nbr机器餐返回的数据
	// //
	// List<String> listCell = PoiUtils.readExcelCell(Program.file, "活动1", 0); //
	// 第0列的所有数据
	// for (int i = 0; i < listCell.size(); i++) { // 遍历所有列，获取sheet表的所有数据
	// List<String> listRow = PoiUtils.readExcelRow(Program.file, "活动1", i); //
	// 第i行的所有数据
	// mapRows.put(listRow.get(0), listRow);
	// }
	// readExcelSheetEndDatetime =
	// DatetimeUtil.addMinutes(Program.getRobotStartDatetime(), listCell.size());
	// for (int activitySequence = 1; activitySequence < mapRows.size();
	// activitySequence++) { // 第activitySequence个活动，机器餐是什么，机器餐为null则这个时间点无活动
	// List<String> listR = mapRows.get(String.valueOf(activitySequence));
	// String row = listR.get(1); // nbr端拿1，代表拿第二列
	// if (!row.equals("0")) {
	// List<String> programUnit =
	// readExcelSheetForNBRMachineMeal(Integer.valueOf(row));
	// listProgramUnit.put(programUnit.get(0), programUnit); // nbr机器餐表的数据
	// }
	// }
	// return (Map<String, List<String>>) listProgramUnit;
	//
	// }

	// protected List<String> readExcelSheetForNBRMachineMeal(int activitySequence)
	// {
	// List<String> listRow = PoiUtils.readExcelRow(Program.file, "nbr机器餐",
	// activitySequence);
	// return listRow;
	// }

	public static int findRealCommodityIdByExcelCommodityID(int refCommodityID) {
		String activityNo = "";
		for (List<String> commInfoList : Robot.mapBaseModels.get("商品信息").values()) {
			if (commInfoList.get(EnumXlsCommodityColumnName.ECCN_ID.getIndex()).equals(String.valueOf(refCommodityID))) {
				String refCommID = commInfoList.get(EnumXlsCommodityColumnName.ECCN_ID.getIndex());
				activityNo = searchActivityNoByRefCommID(refCommID);
				break;
			}
		}
		if (activityNo.equals("")) {
			throw new RuntimeException("根据.xls商品信息表的商品ID，找不到创建该商品的活动序号");
		}
		Iterator<?> ite = Robot.queueOut.iterator();
		int refCommodityIDfromDB = 0;
		while (ite.hasNext()) {
			ProgramUnit pUnit = (ProgramUnit) ite.next();
			if (pUnit.getNo() == Integer.parseInt(activityNo)) {
				BaseModel bm = pUnit.getBaseModelOut1();
				refCommodityIDfromDB = bm.getID();
				break;
			}
		}
		if (refCommodityIDfromDB == 0) {
			throw new RuntimeException("根据商品信息表的商品ID，找不到在数据库的真实ID");
		}
		return refCommodityIDfromDB;
	}

	private static String searchActivityNoByRefCommID(String refCommID) {
		String activityNo = "";
		// 找nbr机器餐
		for (List<String> mealInfo : Robot.mapBaseModels.get("nbr机器餐").values()) {
			if (mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()) == null) {
				continue;
			}
			if (mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()).equals(refCommID)) {
				activityNo = mealInfo.get(EnumXlsMealColumn.EB_ProgramUnitNO.getIndex());
				break;
			}
		}
		return activityNo;
	}

	private int findRealpOrderIdByExcelpOrderID(int pOrderIdInExcel) {
		String activityNo = "";
		// 找nbr机器餐
		for (List<String> mealInfo : Robot.mapBaseModels.get("nbr机器餐").values()) {
			if (mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()) == null) {
				continue;
			}
			// TODO 硬编码，0代表创建
			if (mealInfo.get(EnumXlsMealColumn.EB_SheetTableName.getIndex()).equals("采购订单主表") && mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()).equals(String.valueOf(pOrderIdInExcel)) && mealInfo.get(EnumXlsMealColumn.EB_OperationType.getIndex()).equals(String.valueOf(CreatePurchasingOrder.EnumOperationType.EOT_CreatePurchasingOrder.getIndex()))) {
				activityNo = mealInfo.get(EnumXlsMealColumn.EB_ProgramUnitNO.getIndex());
				break;
			}
		}
		if (activityNo.equals("")) {
			throw new RuntimeException("根据.xls采购订单主表的采购订单ID，找不到创建该采购订单的活动序号");
		}
		Iterator<?> ite = Robot.queueOut.iterator();
		int pOrderIDfromDB = 0;
		while (ite.hasNext()) {
			ProgramUnit pUnit = (ProgramUnit) ite.next();
			if (pUnit.getNo() == Integer.parseInt(activityNo)) {
				BaseModel bm = pUnit.getBaseModelOut1();
				pOrderIDfromDB = bm.getID();
				break;
			}
		}
		if (pOrderIDfromDB == 0) {
			throw new RuntimeException("根据.xls采购订单主表的采购订单ID，找不到在数据库的真实ID");
		}
		return pOrderIDfromDB;
	}

	private int findRealpWarehousignIdByExcelpOrderID(int warehousingIdInExcel) {
		String activityNo = "";
		// 找nbr机器餐
		for (List<String> mealInfo : Robot.mapBaseModels.get("nbr机器餐").values()) {
			if (mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()) == null) {
				continue;
			}
			// TODO 硬编码，0代表创建
			if (mealInfo.get(EnumXlsMealColumn.EB_SheetTableName.getIndex()).equals("入库单主表") && mealInfo.get(EnumXlsMealColumn.EB_MasterTable.getIndex()).equals(String.valueOf(warehousingIdInExcel)) && mealInfo.get(EnumXlsMealColumn.EB_OperationType.getIndex()).equals(String.valueOf(EnumOperationType.EOT_CreateWarehousing.getIndex()))) {
				activityNo = mealInfo.get(EnumXlsMealColumn.EB_ProgramUnitNO.getIndex());
				break;
			}
		}
		if (activityNo.equals("")) {
			throw new RuntimeException("根据.xls入库单主表的入库ID，找不到创建该入库单的活动序号");
		}
		Iterator<?> ite = Robot.queueOut.iterator();
		int warehousingIDfromDB = 0;
		while (ite.hasNext()) {
			ProgramUnit pUnit = (ProgramUnit) ite.next();
			if (pUnit.getNo() == Integer.parseInt(activityNo)) {
				BaseModel bm = pUnit.getBaseModelOut1();
				warehousingIDfromDB = bm.getID();
				break;
			}
		}
		if (warehousingIDfromDB == 0) {
			throw new RuntimeException("根据.xls入库单主表的入库ID，找不到在数据库的真实ID");
		}
		return warehousingIDfromDB;
	}
}
