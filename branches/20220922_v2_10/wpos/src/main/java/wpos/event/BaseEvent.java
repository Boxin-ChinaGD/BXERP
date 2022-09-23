package wpos.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.bo.BaseHttpBO;
import wpos.bo.BaseSQLiteBO;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;

import java.util.List;

@Component("baseEvent")
@Scope("prototype")
public class BaseEvent {
    private static int INDEX = 0;

    /**
     * 不同的事件源/界面发出的事件应该作不同的处理。用以下变量区分不同的事件源
     * 比如SynThread收到同步后服务器发回的零售单，而MainStage上用户查旧单服务器也返回了零售单，双方的事件处理都是onRetailTradeSQLiteEvent()，会引起混乱。
     * 混乱表现在：
     * 1、是拿前者的数据还是后者的数据显示在查单界面上？
     * 2、前者可能有串行的事件1和2。1在修改查单界面的列表时，2也可能的修改查单界面的列表，这会引起ConcurrentModificationException。
     * 目前所有的Event默认的ID都是0，所以EVENT_ID_SyncThread必定不能从0计起，这里从1计起。
     */
    public static final int EVENT_ID_SyncThread = 1;
    public static final int EVENT_ID_MainStage = EVENT_ID_SyncThread + 1;
    public static final int EVENT_ID_SyncDataStage = EVENT_ID_SyncThread + 2;
    public static final int EVENT_ID_ResetBaseDataStage = EVENT_ID_SyncThread + 3;
    public static final int EVENT_ID_LoginStage = EVENT_ID_SyncThread + 4;
    public static final int EVENT_ID_PaymentStage = EVENT_ID_SyncThread + 5;
    public static final int EVENT_ID_PreSaleStage = EVENT_ID_SyncThread + 6;
    public static final int EVENT_ID_PrinterStage = EVENT_ID_SyncThread + 7;
    public static final int EVENT_ID_RetailTradeAggregationStage = EVENT_ID_SyncThread + 8;
    public static final int EVENT_ID_RetrieveCommodityInventoryStage = EVENT_ID_SyncThread + 9;
    public static final int EVENT_ID_SetupRightFragment = EVENT_ID_SyncThread + 10;
    public static final int EVENT_ID_SmallSheetStage = EVENT_ID_SyncThread + 11;
    /**
     * 本Event ID只用于在SQLite中创建退货型零售单
     */
    public static final int EVENT_ID_MainStage_CreateReturnRetailTrade = EVENT_ID_SyncThread + 12;
    public static final int EVENT_ID_CheckListStage = EVENT_ID_SyncThread + 13;

    public enum EnumEventStatus {
        /**
         * 准备操作SQLite
         */
        EES_SQLite_ToDo("EES_SQLite_ToDo", INDEX++),
        /**
         * 操作SQLite完毕。操作成功与否未知
         */
        EES_SQLite_Done("EES_SQLite_Done", INDEX++),
        /**
         * 准备发送网络请求
         */
        EES_Http_ToDo("EES_Http_ToDo", INDEX++),
        /**
         * 网络已经返回响应，UI层已经处理响应。请求的业务逻辑成功与否未知
         */
        EES_Http_Done("EES_Http_Done", INDEX++),
        /**
         * 发送网络请求前检测到意外情况（比如数据格式不正确），不再发送请求
         */
        EES_Http_NoAction("EES_Http_NoAction", INDEX++),
        /**
         * 准备将服务器的数据同步到本地SQLite
         */
        EES_SQLite_ToApplyServerData("EES_SQLite_ToApplyServerData", INDEX++),
        /**
         * 已经将服务器的数据同步到本地SQLite，或SQLite模块在触发网络请求前插入数据到SQLite时就发生错误，设置此状态让调用者不要再等待
         */
        EES_SQLite_DoneApplyServerData("EES_SQLite_DoneApplyServerData", INDEX++),
        /**
         * 没有DB操作。比如，网络请求因数据格式不符合要求，不能发送网络请求，自然就没有网络数据返回，自然就没有把网络数据写到本地的行为
         */
        EES_SQLite_NoAction("EES_SQLite_NoAction", INDEX++);

        private String name;
        private int index;

        private EnumEventStatus(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public static String getName(int index) {
            for (EnumEventStatus c : EnumEventStatus.values()) {
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
    }

    public EnumEventStatus getStatus() {
        return status;
    }

    public void setStatus(EnumEventStatus status) {
        this.status = status;
    }

    /**
     * 标识事件的状态
     */
    protected EnumEventStatus status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 惟一标识一个事件。事件发起者可以凭这个ID，找到或识别相应的事件对象，处理这个对象附带的数据
     */
    protected int id;

    protected long lTimeoutInSecond;

    public final long TIME_OUT = 10;

    /**
     * @param l 秒数
     */
    public void setTimeout(long l) {
        if (l <= 0) {
            throw new RuntimeException("超时值必须大于0！");
        }
        this.lTimeoutInSecond = l;
    }

    public long getTimeout() {
        if (lTimeoutInSecond != 0L) {
            return lTimeoutInSecond;
        }
        return TIME_OUT;
    }

    public String showErrorInfo() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("错误码：");
        sb.append(lastErrorCode);
        sb.append("\r\n");
        sb.append("错误信息：");
        sb.append(lastErrorMessage);
        return String.valueOf(sb);
    }

    public ErrorInfo.EnumErrorCode getLastErrorCode() {
        return lastErrorCode;
    }

    public void setLastErrorCode(ErrorInfo.EnumErrorCode lastErrorCode) {
        this.lastErrorCode = lastErrorCode;
    }

    /**
     * 事件处理后，记录的错误码
     */
    protected ErrorInfo.EnumErrorCode lastErrorCode;

    protected String lastErrorMessage;

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    public List<?> getListMasterTable() {
        return listMasterTable;
    }

    public void setListMasterTable(List<?> listMasterTable) {
        this.listMasterTable = listMasterTable;
    }

    /**
     * 事件处理完后，用来存储主表信息
     */
    protected List<?> listMasterTable;

    public List<?> getListSlaveTable() {
        return listSlaveTable;
    }

    public void setListSlaveTable(List<?> listSlaveTable) {
        this.listSlaveTable = listSlaveTable;
    }

    /**
     * 事件处理完后，用来存储从表信息
     */
    protected List<?> listSlaveTable;

    public BaseModel getBaseModel1() {
        return baseModel1;
    }

    public void setBaseModel1(BaseModel baseModel) {
        this.baseModel1 = baseModel;
    }

    /**
     * 事件处理完后，用来存储一个对象
     */
    protected BaseModel baseModel1;

    public BaseModel getBaseModel2() {
        return baseModel2;
    }

    public void setBaseModel2(BaseModel baseModel2) {
        this.baseModel2 = baseModel2;
    }

    /**
     * 事件处理完后，用来存储一个对象
     */
    protected BaseModel baseModel2;

    /**
     * 职责：UI收到底层或其它地方post的event后，会在@Subcribe函数中处理事件。
     * 其中，onEvent处理所有和UI无关的事情，比如设置status/lastErrorCode/eventProcessed/触发httpBO发起网络请求，和UI有关的全部留给@Subcribe函数的其它代码处理
     */
    public void onEvent() {
        throw new RuntimeException("Not yet implemented!");
    }

    public boolean isEventProcessed() {
        return isEventProcessed;
    }

    public void setEventProcessed(boolean eventProcessed) {
        isEventProcessed = eventProcessed;
    }

    /**
     * 当事件被UI层处理完后，此值为true，否则为false。
     */
    protected boolean isEventProcessed;

    public BaseModel getTmpMasterTableObj() {
        return tmpMasterTableObj;
    }

    public void setTmpMasterTableObj(BaseModel tmpMasterTableObj) {
        this.tmpMasterTableObj = tmpMasterTableObj;
    }

    /**
     * 临时主表对象。其内有从表指针。用于异步事件流处理：异步插入主表时，在插入事件中可以再触发事件插入从表信息，这时需要知道要插入的从表的信息。从本变量中就能得知
     */
    protected BaseModel tmpMasterTableObj;

    public void setSqliteBO(BaseSQLiteBO bo) {
        sqliteBO = bo;
    }

    /**
     * 用于异步事件流处理。异步成功插入主表后，需要再插从表。这时需要知道相应的BO
     */
    protected BaseSQLiteBO sqliteBO;

    public void setHttpBO(BaseHttpBO httpBO) {
        this.httpBO = httpBO;
    }

    public BaseHttpBO getHttpBO() {
        return httpBO;
    }

    /**
     * 用于异步事件流处理。异步成功插入主表从表后，需要再发出http请求。这时需要知道相应的BO
     */
    protected BaseHttpBO httpBO;

    public String getMessageBeforeEventPosted() {
        return messageBeforeEventPosted;
    }

    public void setMessageBeforeEventPosted(String messageBeforeEventPosted) {
        this.messageBeforeEventPosted = messageBeforeEventPosted;
    }

    /**
     * 在Event post给UI之前，有的信息需要带给@Subcribe函数，用来提示用户一些注意事项或下一步动作。这个信息可以记在本变量中。
     */
    protected String messageBeforeEventPosted;

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    /**
     * 本地同步服务器相应的某个类型数据。有C、U、D三种类型
     */
    protected String syncType;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 事件携带的网络响应数据。这些数据的类型不一。
     * 目前NtpHttpEvent和其它类都有使用到
     */
    protected Object data;

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 实体类的查询或同步时的分页页码，代表开始页或结束页，不代表其它页码
     */
    protected String pageIndex;

    public String printErrorInfo() {
        return "错误码=" + lastErrorCode + "\t错误信息=" + lastErrorMessage;
    }

    public String printErrorInfo(Object param) {
        return "错误码=" + lastErrorCode + "\t错误信息=" + lastErrorMessage + "\t参数：" + param;
    }
}
