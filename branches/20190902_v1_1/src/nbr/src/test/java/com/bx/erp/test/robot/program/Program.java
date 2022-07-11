package com.bx.erp.test.robot.program;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.bx.erp.test.robot.protocol.BaseBuffer;
import com.bx.erp.util.PoiUtils;

public abstract class Program extends BaseBuffer {
	/** 上次执行的时间 */
	protected Date lastExecutionDatetime;
	/** 计划下一次执行的最早时间 */
	protected Date nextScheduledRunDatetime;
	/** 做机器人测试创建商品的个数 */
	public static final int CreateCommodityNO = 1;
	/** 机器人运行的开始时间 */
	protected Date startDatetime;
	/** 机器人运行的结束时间 */
	protected Date endDatetime;
	/** 是否运行随机模式的机器人 */
	protected boolean bRunInRandomMode;
	/** 机器餐文件路径 */
//	public static final String FILE_PATH_ProgramUnit = "D:\\BXERP\\trunk\\doc\\手动测试文档\\DEV\\元粒度测试（单元测试）_nbr\\machine.xlsx";
	// 日报表测试用例文件
	public static final String FILE_PATH_ProgramUnit = "D:\\BXERP\\trunk\\doc\\手动测试文档\\DEV\\元粒度测试（单元测试）_nbr\\machinedailyreporttest.xlsx";
	/** 在第一次创建时赋值，值为创建前的最大ID */
	public static int maxCommodityID = 0;
	public static int maxBarcodesID = 0;
	public static int maxPurchasingOrderID = 0;
	public static int maxPurchasingOrderCommodityID = 0;
	public static int maxWarehousingID = 0;
	public static int maxWarehousingCommodityID = 0;
	public static int maxRetailTradeID = 0;
	// Excel数据

	/** 当前正在执行的活动单元，默认跑第1个活动 */
	public static volatile int currentProgramUnitNO = 1;

	private static int INDEX_EnumProgramType = 0;

	public enum EnumProgramType {
		EPT_CreateCommodity("Create Commodity", INDEX_EnumProgramType++), //

		EPT_CreatePurchasingOrder("Create Purchasing Order", INDEX_EnumProgramType++), //
		EPT_ApprovePurchasingOrder("Approve Purchasing Order", INDEX_EnumProgramType++), //

		EPT_CreateWarehousing("Create Warehousing", INDEX_EnumProgramType++), //
		EPT_ApproveWarehousing("Approve Warehousing", INDEX_EnumProgramType++), //

		EPT_CreateReturnCommoditySheet("Create Return Commodity Sheet", INDEX_EnumProgramType++), //
		EPT_ApproveReturnCommoditySheet("Approve Return Commodity Sheet", INDEX_EnumProgramType++), //

		EPT_ProgramPromotion("Create Return Commodity Sheet", INDEX_EnumProgramType++);

		private String name;
		private int index;

		private EnumProgramType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumProgramType c : EnumProgramType.values()) {
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

	protected Program() {
	}

	/** 把当前活动单元的输出，喂给另外一个活动，作为后者的输入 */
	protected void feed(ProgramUnit pu, Program pro) {
		pro.eat(pu);
	}

	/** 吃进另外一个活动喂的活动单元 */
	protected void eat(ProgramUnit pu) {
		queueIn.add((ProgramUnit) pu.clone());
	}

	/** @param startDatetime
	 * @param endDatetime
	 * @param bRunInRandomMode
	 *            false是特定模式 */
	public Program(Date startDatetime, Date endDatetime, boolean bRunInRandomMode) {
		this.startDatetime = startDatetime;
		this.endDatetime = endDatetime;
		this.bRunInRandomMode = bRunInRandomMode;

		queueIn = new LinkedList<ProgramUnit>();
		queueOut = new LinkedList<ProgramUnit>();
	}

	/** 保存活动单元的输出 */
	protected Queue<ProgramUnit> queueOut;

	/** 保存活动单元的输入。如果不是工作在随机模式下，则queue内会包含一批活动单元，每次跑完一个就移出一个 */
	protected Queue<ProgramUnit> queueIn;

	protected static PoiUtils poiXls;
	
	/** 如果不是工作在随机模式下，则需要在测试启动时，加载活动单元，以让活动可以运行一些操作 */
	// public boolean loadProgramUnit() {
	// // assert bRunInRandomMode = false;
	// return doLoadProgramUnit();
	// }

	/** 每个派生类，必须实现本接口，以便将活动单元塞进queue中。到了要跑的时间，就拿出里面的内容来跑 */
	// protected boolean doLoadProgramUnit() {
	// throw new RuntimeException("尚未实现的接口！");
	// }

	// /** 已经执行的次数 */
	// protected int executionNO;

	// /**
	// * 1天frequence1次
	// */
	// protected int frequence1;
	// /**
	// * frequence2天1次
	// */
	// protected int frequence2;
	// /**
	// * 1采购单入库frequence3次
	// */
	// protected int frequence3;
	// /**
	// * 1采购单入库frequence3次。限制最大值为3。一个采购订单最多入库3次
	// */
	// protected final int MAX_frequence3 = 3;
	// /**
	// *
	// */
	// protected int frequence4;

	protected abstract void generateReport();

	public abstract boolean run(Date currentDatetime, StringBuilder sbError, final Program[] programs) throws Exception;

	/** 判断当前这个活动能否运行。其依据是不同的规则和配置， 比如，配置一天之中最大收银次数为100，如果当前收银次数已经为100，则不可以继续收银。
	 * 又如，如果执行频率未超过设置的最大值，则可以继续执行。 */
	protected abstract boolean canRunNow(Date d);

	// /** 重置活动的状态，以免影响到第2天重复运算后的结果。
	// * 每次活动完成后，都会遗留一些状态数据，比如零售单，遗留一天积下来的零售商品表，如果不清除，第2天重复运行后，其收银汇总会包含第1天的数据。 */
	// public abstract void resetForNextDay();

	public static Map<String, Map<String, List<String>>> loadAll() {
		try {
			poiXls = new PoiUtils(Program.FILE_PATH_ProgramUnit);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		do {
			// 获取excel文件工作表数量
			int sheetNO = poiXls.readExcelSheetNO(Program.FILE_PATH_ProgramUnit);
			if (sheetNO == -1) {
				break;
			}

			Map<String, Map<String, List<String>>> mapBaseModels = new HashMap<String, Map<String, List<String>>>();
			for (int i = 0; i < sheetNO; i++) {
				String sheetName = poiXls.readExcelSheetName(Program.FILE_PATH_ProgramUnit, i);
				if ("".equals(sheetName)) {
					break;
				}
				System.out.println("\tsheetName=" + sheetName);
				//
				List<String> row = new ArrayList<String>();
				Map<String, List<String>> mapBaseModel = new HashMap<String, List<String>>();
				List<String> listCell = poiXls.readExcelCell(Program.FILE_PATH_ProgramUnit, i, 0); // 第0列的所有数据
				if (listCell == null) {
					break;
				}
				// 遍历工作表的所有行
				for (int k = 0; k < listCell.size(); k++) {
					row = poiXls.readExcelRow(Program.FILE_PATH_ProgramUnit, i, k);
					if (row == null) {
						break;
					}
					mapBaseModel.put(row.get(0), row);
				}
				mapBaseModels.put(sheetName, mapBaseModel);
			}

			return mapBaseModels;
		} while (false);

		return null;
	}
	//
	// protected Map<String, List<String>> readExcelSheet(int sheetAtNO) {
	//
	// return mapBaseModel;
	// }
}
