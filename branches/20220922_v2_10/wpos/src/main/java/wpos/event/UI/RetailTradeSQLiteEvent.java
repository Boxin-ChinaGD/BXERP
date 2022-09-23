package wpos.event.UI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.bo.BaseHttpBO;
import wpos.bo.BaseSQLiteBO;
import wpos.event.BaseEvent;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTrade;
import wpos.presenter.RetailTradePresenter;
import wpos.utils.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Component("retailTradeSQLiteEvent")
@Scope("prototype")
public class RetailTradeSQLiteEvent extends BaseSQLiteEvent {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private RetailTradePresenter retailTradePresenter;

    //此方法在测试代码中使用，不可用于功能代码
    public void setRetailTradePresenter(RetailTradePresenter retailTradePresenter) {
        this.retailTradePresenter = retailTradePresenter;
    }

    public RetailTradeSQLiteEvent() {
    }

    @Override
    public void onEvent() {
        StringUtils.printCurrentFunction("eventTypeSQLite", eventTypeSQLite);

        switch (eventTypeSQLite) {
            case ESET_RetailTrade_CreateMasterSlaveAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("主表和从表都已经插入成功，触发网络同步请求...");
                    // 如果临时单为退货单，则将其saleAmountAlipay和saleAmountWeChat设置为源单的相应值，以便临时单的checkCreate()能检查这2个字段，同时微信退款不会出现错误：微信退款的金额不能比零售时微信支付的钱多
                    RetailTrade retailTrade = (RetailTrade) getTmpMasterTableObj();
                    if (retailTrade != null && retailTrade.getSourceID() > 0) {
                        RetailTrade retailTrade1 = new RetailTrade();
                        retailTrade1.setID(Integer.valueOf(retailTrade.getSourceID()));
                        RetailTrade retailTrade2 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade1);
                        retailTrade.setSaleAmountAlipay(retailTrade2.getAmountAlipay());
                        retailTrade.setSaleAmountWeChat(retailTrade2.getAmountWeChat());
                    }
                    if (httpBO != null) {
                        httpBO.createAsync(BaseHttpBO.INVALID_CASE_ID, retailTrade);
                    }
                } else {
                    log.info("主表和从表插入失败!不会触发网络同步请求。");
                    setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                    break;
                }

                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_RetailTrade_RefreshMasterSlaveAsyncSQLite_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("主表和从表都已经插入成功，不会上传到服务器");
                } else {
                    log.info("主表和从表插入失败!不会上传到服务器");
                }
                setStatus(BaseEvent.EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_RetailTrade_CreateNReplacerAsync_Done:
            case ESET_RetailTrade_CreateReplacerAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经成功同步了服务器返回的同步数据块(C型)");
                } else {
                    log.info("本地SQLite同步服务器返回的同步数据块(C型)失败!");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_RetailTrade_CreateReplacerNAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地SQLite已经全部成功同步了服务器返回的N个零售单");
                } else {
                    log.info("本地SQLite同步服务器返回的N个零售单失败!");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_RetailTrade_RefreshByServerDataAsyncC_Done:
            case ESET_RetailTrade_RefreshByServerDataAsync_Done:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("主从表的数据都已经根据服务器返回的数据(CUD)同步成功...");
                } else {
                    log.info("本地SQLite同步零售单(CUD)失败!");
                }
                setStatus(EnumEventStatus.EES_SQLite_DoneApplyServerData);
                break;
            case ESET_RetailTrade_updateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("查询后服务器返回的零售单，成功插入数据库！");
                } else {
                    log.info("查询后服务器返回的零售单，插入数据库失败！");
                }
                status = EnumEventStatus.EES_SQLite_Done;
                break;
            case ESET_RetailTrade_RetrieveNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("查询临时的零售单成功！" + getListMasterTable());
                    // 如果临时单为退货单，则将其saleAmountAlipay和saleAmountWeChat设置为源单的相应值，以便临时单的checkCreate()能检查这2个字段，同时微信退款不会出现错误：微信退款的金额不能比零售时微信支付的钱多
                    List<BaseModel> listRetailTrade = (List<BaseModel>) getListMasterTable();
                    if (listRetailTrade != null && listRetailTrade.size() > 0) {
                        for (BaseModel bm : listRetailTrade) {
                            RetailTrade retailTrade = (RetailTrade) bm;
                            if (retailTrade.getSourceID() > 0) {
                                RetailTrade retailTrade1 = new RetailTrade();
                                retailTrade1.setID(Integer.valueOf(retailTrade.getSourceID()));
                                RetailTrade retailTrade2 = (RetailTrade) retailTradePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, retailTrade1);
                                retailTrade.setSaleAmountAlipay(retailTrade2.getAmountAlipay());
                                retailTrade.setSaleAmountWeChat(retailTrade2.getAmountWeChat());
                            }
                        }
                    }
                    if (getListMasterTable().size() > 0) {
                        if (!httpBO.createNAsync(BaseHttpBO.INVALID_CASE_ID, listRetailTrade)) {
                            if (httpBO.getHttpEvent().getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError){
                                lastErrorCode = httpBO.getHttpEvent().getLastErrorCode();
                            }
                            status = EnumEventStatus.EES_SQLite_Done;
                            break;
                        }
                    } else {
                        status = EnumEventStatus.EES_SQLite_DoneApplyServerData;
                        break;
                    }
                } else {
                    log.info("查询临时零售单失败！");
                }
                status = EnumEventStatus.EES_SQLite_Done;
                break;
            case ESET_RetailTrade_CreateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("插入临时零售单成功！");
                } else if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_BusinessLogicNotDefined) {
                    log.info("已经有相同源单ID的退货单在临时零售单中,插入临时零售单失败！");
                } else {
                    log.info("插入临时零售单失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_RetailTrade_UpdateAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("更新临时零售单成功！");
                } else {
                    log.info("更新临时零售单失败！");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_RetailTrade_Retrieve1Async:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("查询零售单成功");
                } else {
                    log.info("查询零售单失败");
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_RetailTrade_DeleteAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地delete RetailTrade 成功！");
                } else {
                    log.info("本地delete RetailTrade 失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            case ESET_RetailTrade_CreateNAsync:
                if (lastErrorCode == ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("本地createN RetailTrade 成功！");
                } else {
                    log.info("本地createN RetailTrade 失败！");
                }
                setStatus(EnumEventStatus.EES_SQLite_Done);
                break;
            default:
                log.info(eventTypeSQLite);
                log.info("未定义的事件！");
                throw new RuntimeException("未定义的事件！");
        }

        setEventProcessed(true);
    }
}
