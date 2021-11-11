package com.test.bx.app.robot.program;

import com.bx.erp.utils.PoiUtils;
import com.test.bx.app.robot.RobotConfig;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class Program {
    /**
     * 上次执行的时间
     */
    protected  Date lastExecutionDatetime;
    /**
     * 计划下一次执行的最早时间
     */
    protected Date nextScheduledRunDatetime;

    /**
     * 机器人运行的开始时间
     */
    protected Date startDatetime;
    /**
     * 机器人运行的结束时间
     */
    protected Date endDatetime;

    /** 运行的是随机机器人 */
    public static final int RandomMode = 0;
    /** 运行的是特定机器人 */
    public static final int SpecificMode = 1;
    /** 运行的是读取Excel模式的机器人 */
    public static final int ReadExcelMode = 2;

    /** 机器人路径 */
//    public static final String file = "/sdcard/machine.xlsx";

    // 日报表测试用例
    public static final String file = "/sdcard/machinedailyreporttest.xlsx";

    public volatile static int maxCommodityID = 0;
    public volatile static int maxBarcodeID = 0;
    public volatile static int maxRetailTradeID = 0;

    /** 是否运行随机模式的机器人 */
    protected boolean bRunInRandomMode;

    public static int getINDEX() {
        return INDEX;
    }

    private static int INDEX = 0;
    private static int operationINDEX = 0;
    protected long lTimeout;

    private static Date robotStartDatetime;

    private RobotConfig robotConfig;

    // Excel数据
    public static Map<String, Map<String, List<String>>> mapBaseModels = new HashMap<String, Map<String, List<String>>>();

    public enum EnumProgramType {
        EPT_RetailTrade("EPT_RetailTrade", INDEX++),
        EPT_PosLogin("EPT_PosLogin", INDEX++), //
        EPT_SyncData("EPT_SyncData", INDEX++), //
        EPT_QueryRetailTrade("EPT_QueryRetailTrade", INDEX++), //
        EPT_RetailTradeAggregation("EPT_RetailTradeAggregation", INDEX++); //

        private String name;
        private int index;

        private EnumProgramType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumProgramType ept : EnumProgramType.values()) {
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

    protected Program() { }

    public Program(Date startDatetime, Date endDatetime, final RobotConfig rc, boolean bRunInRandomMode) {
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.bRunInRandomMode = bRunInRandomMode;
        queueIn = new LinkedList<ProgramUnit>();
        queueOut = new LinkedList<ProgramUnit>();

        robotConfig = rc;
    }

    /**
     * 已经执行的次数
     */
    protected int executionNO;

    protected abstract void generateReport();

    public abstract boolean run(Date currentDatetime, StringBuilder sbError, Program[] programs, boolean bRunInRandomMode) throws Exception;

    /**
     * 判断当前这个活动能否运行。其依据是不同的规则和配置，
     * 比如，配置一天之中最大收银次数为100，如果当前收银次数已经为100，则不可以继续收银。
     * 又如，如果执行频率未超过设置的最大值，则可以继续执行。
     */
    protected abstract boolean canRunNow(Date d);

    /**
     * 重置活动的状态，以免影响到第2天重复运算后的结果。
     * 每次活动完成后，都会遗留一些状态数据，比如零售单，遗留一天积下来的零售商品表，如果不清除，第2天重复运行后，其收银汇总会包含第1天的数据。
     */
    public abstract void resetForNextDay();

    /** 如果不是工作在随机模式下，则需要在测试启动时，加载活动单元，以让活动可以运行一些操作 */
    public boolean loadProgramUnit() {
        // assert bRunInRandomMode = false;
        return doLoadProgramUnit();
    }

    /** 每个派生类，必须实现本接口，以便将活动单元塞进queue中。到了要跑的时间，就拿出里面的内容来跑 */
    protected boolean doLoadProgramUnit() {
        throw new RuntimeException("尚未实现的接口！");
    }

    public static Date getRobotStartDatetime() {
        return robotStartDatetime;
    }

    public static void setRobotStartDatetime(Date robotStartDatetime) {
        Program.robotStartDatetime = robotStartDatetime;
    }

    /** 保存活动单元的输出 */
    protected Queue<ProgramUnit> queueOut;

    /** 保存活动单元的输入。如果不是工作在随机模式下，则queue内会包含一批活动单元，每次跑完一个就移出一个 */
    protected Queue<ProgramUnit> queueIn;

    /** 把当前活动单元的输出，喂给另外一个活动，作为后者的输入 */
    protected void feed(ProgramUnit pu, Program pro) {
        pro.eat(pu);
    }

    /** 吃进另外一个活动喂的活动单元 */
    protected void eat(ProgramUnit pu) {
        queueIn.add((ProgramUnit)pu.clone());
    }
}
