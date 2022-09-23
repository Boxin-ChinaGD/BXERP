package com.bx.erp.http.sync;

import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.RetailTradeHttpBO;
import com.bx.erp.bo.RetailTradeSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.RetailTradeSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用来上传零售单、零售单计算过程
 */
public class SyncThread extends Thread {
    public final static int SIGNAL_ThreadExit = 1;
    private Logger log = Logger.getLogger(this.getClass());
    /**
     * 控制线程是否立即（近似立即）执行
     */
    private static AtomicInteger atomicExecuteInstantly = new AtomicInteger();

    private AtomicInteger atomicInteger;

    /**
     * 功能代码中使用：APP启动时，同步线程是暂停的。STAFF登录成功后才RESUME。
     * 测试代码中使用：零售单和零售单计算过程的上传，需要同步线程运行。但其它测试有时不需要立即上传，就将其PAUSE，运行完后RESUME
     */
    public final static int PAUSE = 0;
    /**
     * 参考PAUSE
     */
    public final static int RESUME = 1;

    public static AtomicInteger aiPause;

    private static RetailTradeSQLiteBO retailTradeSQLiteBO = null;// = GlobalController.getInstance().getRetailTradeSQLiteBO();
    private static RetailTradeHttpEvent retailTradeHttpEvent = null;// = GlobalController.getInstance().getRetailTradeHttpEvent();
    private static RetailTradeSQLiteEvent retailTradeSQLiteEvent = null;// = GlobalController.getInstance().getRetailTradeSQLiteEvent();
    private static RetailTradeHttpBO retailTradeHttpBO = null;

    public void setAtomicInteger(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }

    /**
     * 每隔interval秒钟，向服务器获取同步信息一次
     */
    protected long interval;

    /**
     * 在上传零售单的时候需要知道上一次上传零售单的时间，要是没有第一次上传零售单，这个时间就是第一张临时零售单保存在本地SQLite的时间
     */
//    public static long lastCreateNRetailtradeTime;
    public SyncThread(long interval) {
        if (retailTradeSQLiteEvent == null) {
            retailTradeSQLiteEvent = new RetailTradeSQLiteEvent();
            retailTradeSQLiteEvent.setId(BaseEvent.EVENT_ID_SyncThread);
        }
        if (retailTradeHttpEvent == null) {
            retailTradeHttpEvent = new RetailTradeHttpEvent();
            retailTradeHttpEvent.setId(BaseEvent.EVENT_ID_SyncThread);
        }
        if (retailTradeSQLiteBO == null) {
            retailTradeSQLiteBO = new RetailTradeSQLiteBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        if (retailTradeHttpBO == null) {
            retailTradeHttpBO = new RetailTradeHttpBO(GlobalController.getInstance().getContext(), retailTradeSQLiteEvent, retailTradeHttpEvent);
        }
        retailTradeHttpEvent.setSqliteBO(retailTradeSQLiteBO);
        retailTradeHttpEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setHttpBO(retailTradeHttpBO);
        retailTradeSQLiteEvent.setSqliteBO(retailTradeSQLiteBO);

        atomicInteger = new AtomicInteger(SIGNAL_ThreadExit + 1);
        aiPause = new AtomicInteger(PAUSE);
        setName("同步线程");
    }

    /**
     * 设置优先级为1，默认为0，所以当SyncThread post一个Event的时候，先会在SyncThread接收到，
     * EventBus.getDefault().cancelEventDelivery(event);这一句的目的：SyncThread接收到Event之后，其他位置就不会接收到
     * 以上两种做法是为了不让其他地方接收到SyncThread有关的Event
     *
     * @param event
     */
    @Subscribe(priority = 1)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        if (event.getId() == BaseSQLiteEvent.EVENT_ID_SyncThread) {
            event.onEvent();
            log.info("****************************************SyncThread onRetailTradeHttpEvent");
            //
        } else {
            log.info("未处理的Event，ID=" + event.getId() + "syncThread");
        }
    }

    @Subscribe(priority = 1)
    public void onRetailTradeSQLiteEvent(RetailTradeSQLiteEvent event) {
        if (event.getId() == BaseSQLiteEvent.EVENT_ID_SyncThread) {
            log.info("****************************************SyncThread onRetailTradeSQLiteEvent");
            event.onEvent();
        } else {
            log.info("未处理的Event，ID=" + event.getId() + "syncThread");
        }
    }

    /**
     * 令线程立即（近似立即）执行上传临时零售单。只能给测试代码调用
     */
    public static void executeInstantly(boolean bExecuteInstantly) {
        atomicExecuteInstantly.set(bExecuteInstantly ? 1 : 0);
    }

    @Override
    public void run() {
        EventBus.getDefault().register(this);//在线程运行后才注册EventBus

        while (atomicInteger.get() != SIGNAL_ThreadExit) {
            while (aiPause.get() == PAUSE && atomicInteger.get() != SIGNAL_ThreadExit) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (atomicInteger.get() == SIGNAL_ThreadExit) {
                break; //在断网情况下，staff登录到的是本地SQLite，本线程不会被启动。退出登录后，下面的代码不需要执行，所以这里break出去。同时，随时可能有用户交班或会话超时发生
            }
            //定时批量上传零售单：第一次上传时间的5秒后，查询本地SQLite是否存在临时零售单，若存在，进行批量上传。每一次进行零售单的上传，都要把lastCreateNRetailtradeTime设置成最近上传完成的时间
            int iTimeout = 10; //10* 500 = 5秒
            while (iTimeout-- > 0 && atomicExecuteInstantly.get() != 1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (atomicInteger.get() == SIGNAL_ThreadExit) {
                    break; //随时可能有用户交班或会话超时发生
                }
            }
            if (atomicExecuteInstantly.get() == 1) {
                atomicExecuteInstantly.set(0);
            }

            if (atomicInteger.get() == SIGNAL_ThreadExit) {
                break; //随时可能有用户交班或会话超时发生
            }

            queryLocalRetailTradeAndUpload();//TODO 交班或会话超时之时，UI层切换activity可能会慢，以后需要waiting dialog。
        }

        log.info("线程：" + this.getName() + "正常结束");
    }

    /**
     * TODO:登录成功后，把N天前的已经上传的零售单删除，以加速搜索和上传
     */
    private void queryLocalRetailTradeAndUpload() {
        //查找本地是否存在临时零售单，并且上传
        log.info("同步线程开始查找临时零售单！");
        retrieveNTempRetailTradeInSQLite();
    }

    /**
     * 找到本地所有的临时RetailTrade，并上传
     */
    public boolean retrieveNTempRetailTradeInSQLite() {
        retailTradeHttpEvent.setStatus(BaseEvent.EnumEventStatus.EES_Http_ToDo);
        retailTradeSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        retailTradeSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_RetailTrade_RetrieveNAsync);
        if (!retailTradeSQLiteBO.retrieveNASync(BaseSQLiteBO.CASE_RetailTrade_RetrieveNToUpload, null)) {
            log.info("查询临时零售单失败！");
        }
        long lTimeOut = 60;
        while (retailTradeSQLiteEvent.getStatus() != BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData && lTimeOut-- > 0) {
            if ((retailTradeSQLiteEvent.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_Done && retailTradeSQLiteEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)//因为是在Presenter中设置了错误码，在event中设置了状态，所以先判断状态再判断错误码
                    || (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError)) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (retailTradeHttpEvent.getStatus() == BaseEvent.EnumEventStatus.EES_Http_Done && retailTradeHttpEvent.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            log.error("解析服务器返回的RetailTrade出错！");
            return false;
        }
        if (lTimeOut <= 0) {
            log.error("上传临时零售单同步数据失败！原因：超时！");
            log.error("创建retailtrade失败");// TODO 将来可能添加处理
            retailTradeHttpEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_NoError);
            return false;
        } else {
            return true;
        }
    }


    public void exit() {
        EventBus.getDefault().unregister(this);//注销EventBus
        atomicInteger.set(SIGNAL_ThreadExit);
        aiPause.set(RESUME);
    }
}
