package wpos.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;

import javax.annotation.Resource;
import java.util.List;

@Component("baseSQLiteBO")
public abstract class BaseSQLiteBO {
    private Log log = LogFactory.getLog(this.getClass());

    public static int INVALID_INT_ID = -1;
    public static int INVALID_ID = -1;
    public static int INVALID_CASE_ID = -1;
    public static int INVALID_STATUS = -1;
    public static final int INVALID_Type = -1;
    public static final int INVALID_NO = -1;

    /**
     * TODO 待review
     */
    public final static int CASE_Normal = 10086;
    public final static int CASE_Commodity_RetrieveNByConditions = CASE_Normal + 1;
    public final static int CASE_SmallSheetFrame_RetrieveNByConditions = CASE_Normal + 2;
    public final static int CASE_SmallSheetText_RetrieveNByConditions = CASE_Normal + 3;
    public final static int CASE_RetailTrade_RetrieveNByConditions = CASE_Normal + 4;
    public final static int CASE_RetailTradeCommodity_RetrieveNByConditions = CASE_Normal + 5;
    public final static int CASE_Vip_RetrieveNByConditions = CASE_Normal + 6;
    public final static int CASE_Barcodes_RetrieveNByConditions = CASE_Normal + 7;
    public final static int CASE_Pos_RetrieveNByConditions = CASE_Normal + 8;
    public final static int CASE_RetailTradeAggregation_RetrieveNByConditions = CASE_Normal + 9;
    public final static int CASE_RetailTradeAggregation_DeleteOutdatedSync = CASE_Normal + 10;
    public final static int CASE_Staff_RetrieveNByConditions = CASE_Normal + 11;
    public final static int CASE_Brand_RetrieveNByConditions = CASE_Normal + 12;
    public final static int CASE_RerailTrade_DeleteOutdatedSync = CASE_Normal + 13;
    public final static int CASE_Category_RetrieveNByConditions = CASE_Normal + 14;
    public final static int CASE_ConfigGeneral_RetrieveNByConditions = CASE_Normal + 15;
    public final static int CASE_PackageUnit_RetrieveNByConditions = CASE_Normal + 16;
    public final static int CASE_ConfigCacheSize_RetrieveNByConditions = CASE_Normal + 17;
    public final static int CASE_SmallSheet_UpdateByServerData = CASE_Normal + 18;
    public final static int CASE_ConfigGeneral_UpdateByServerData = CASE_Normal + 19;
    public final static int CASE_RetailTrade_CreateMasterSlaveSQLite = CASE_Normal + 20;
    public final static int CASE_PromotionScope_RetrieveNByConditions = CASE_Normal + 21;
    public final static int CASE_RetailTradePromoting_RetrieveNByConditions = CASE_Normal + 22;
    public final static int CASE_Barcodes_DeleteNByConditions = CASE_Normal + 23;
    public final static int CASE_RetailTradeCoupon_CreateSync = CASE_Normal + 24;
    public final static int CASE_PromotionShopScope_RetrieveNByConditions = CASE_Normal + 25;

    /**
     * 用于POS查找自己身份
     */
    public final static int CASE_POS_Retrieve1ForIdentity = CASE_Normal + 22;
    /**
     * 上传N个需要上传的零售单。在以下三种情况下需要上传零售单：
     * 1、开机同步。开机时，其中一项同步操作是检查上次收银未上传的零售单，然后将其上传
     * 2、间歇性上传。收银时，不会立即将零售单上传到服务器，而是间歇性地上传。
     * 3、收银汇总。收银汇总上传到服务器前，要先上传残余的零售单到服务器。
     */
    public final static int CASE_RetailTrade_RetrieveNToUpload = CASE_Normal + 23;
    public final static int CASE_Promotion_RetrieveNByConditions = CASE_Normal + 24;
    public final static int CASE_RetailTradePromotingFlow_RetrieveNByConditions = CASE_Normal + 25;
    public final static int CASE_RetailTradePromoting_RetrieveNByTradeID = CASE_Normal + 26;
    public final static int CASE_RetailTradePromoting_CreateMasterSlaveSQLite = CASE_Normal + 27;
    public final static int CASE_RetailTradePtomoting_RetrieveNToUpload = CASE_Normal + 28;
    public final static int CASE_Staff_RetrieveResigned = CASE_Normal + 29;
    public final static int CASE_BXConfigGeneral_RetrieveNByConditions = CASE_Normal + 30;
    //
    public final static int CASE_Commodity_CreateComposition = CASE_Normal + 31;
    public final static int CASE_Commodity_CreateMultiPackaging = CASE_Normal + 32;
    public final static int CASE_Commodity_CreateService = CASE_Normal + 33;
    public final static int CASE_UpdateCommodityOfMultiPackaging = CASE_Normal + 34;
    public final static int CASE_UpdatePurchasingUnit = CASE_Normal + 35;
    public final static int CASE_UpdateCommodityOfService = CASE_Normal + 36;
    public final static int CASE_Commodity_UpdatePrice = CASE_Normal + 37;
    public final static int CASE_ReturnCommoditySheet_Approve = CASE_Normal + 38;
    public final static int CASE_CheckUniqueField = CASE_Normal + 39;
    public final static int CASE_RetrieveNMultiPackageCommodity = CASE_Normal + 40;
    public final static int CASE_UpdateCommodityOfNO = CASE_Normal + 41;
    public final static int CASE_RetailTrade_RetrieveNForReturned = CASE_Normal + 42;
    public final static int CASE_SmallSheetFrame_RetrieveNToUpload = CASE_Normal + 43;

//    protected Context context;

    public BaseSQLiteEvent getSqLiteEvent() {
        return sqLiteEvent;
    }

    //    @Resource
    protected BaseSQLiteEvent sqLiteEvent;

    public BaseHttpEvent getHttpEvent() {
        return httpEvent;
    }

    //    @Resource
    protected BaseHttpEvent httpEvent;

    /**
     * 向SQLite发送异步DB操作请求：创建对象
     */
    public abstract boolean createAsync(int iUseCaseID, final BaseModel bm);

    /**
     * 向SQLite发送同步DB操作请求：创建对象
     */
    public BaseModel createSync(int iUseCaseID, final BaseModel bm) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * 处理网络返回的数据。这些数据已经parse成功，作为参数bm
     *
     * @param bm 从网络得到的数据
     */
    public boolean onResultCreate(BaseModel bm) {
        return applyServerDataAsync(bm);
    }

    public boolean onResultCreateN(List<BaseModel> bmList) {
        return applyServerDataListAsync(bmList);
    }

    /**
     * 用于同步服务器返回创建N个对象后的数据..删除本地SQLite的临时数据，再插入服务器的新数据.
     */
    protected abstract boolean applyServerDataListAsync(List<BaseModel> bmList);

    /**
     * 同步数据成功后，删除本地SQLite的临时数据，再插入服务器的新数据。
     * 目前只处理创建型的数据，不处理修改型的数据
     */
    protected abstract boolean applyServerDataAsync(BaseModel bmNew);

    public boolean updateSync(int iUseCaseID, final BaseModel bm) {
        throw new RuntimeException("尚未实现的方法！");
    }

    /**
     * 向SQLite发送DB操作请求：修改对象
     */
    public abstract boolean updateAsync(int iUseCaseID, final BaseModel bm);

    /**
     * 处理网络返回的数据。这些数据已经parse成功，作为参数bm。
     *
     * @param bm 从网络得到的数据
     */
    public boolean onResultUpdate(BaseModel bm) {
        return applyServerDataUpdateAsync(bm);
    }

    protected abstract boolean applyServerDataUpdateAsync(BaseModel bmNew);

    /**
     * 处理网络返回的数据。这些数据已经parse成功，作为参数bm。
     *
     * @param bmList 从网络得到的数据
     */
    public boolean onResultUpdateN(List<?> bmList) {
        return applyServerDataUpdateAsyncN(bmList);
    }

    protected abstract boolean applyServerDataUpdateAsyncN(List<?> bmList);

    /**
     * 处理delete之后网络返回的数据
     *
     * @param bm
     * @return
     */
    public boolean onResultDelete(BaseModel bm) {
        return applyServerDataDeleteAsync(bm);
    }

    protected abstract boolean applyServerDataDeleteAsync(BaseModel bmDelete);

    /**
     * 处理Feedback的结果
     */
    public void onResultFeedback() { //...什么没有feedback成功？？？
        log.info("onResultFeedback()");
    }

    /**
     * 处理网络返回的数据
     *
     * @param bmList 从网络返回的数据，已经parse成功并且按照int2升序排列。每个元素的string1字段，代表C型、U型或D型数据
     * @return
     */
    public boolean onResultRetrieveN(List<BaseModel> bmList) {
        return applyServerListDataAsync(bmList);
    }

    /**
     * 处理网络返回的数据
     *
     * @param bmList 从网络返回的数据，已经parse成功.
     * @return
     */
    public boolean onResultRetrieveNC(List<BaseModel> bmList) {
        return applyServerListDataAsyncC(bmList);
    }

    /**
     * 向SQLite同步服务器返回的CUD型对象
     *
     * @param bmList 服务器返回的数据，按照int2升序排列
     * @return
     */
    protected abstract boolean applyServerListDataAsync(List<BaseModel> bmList);

    /**
     * 向SQLite同步服务器返回的对象
     *
     * @param bmList 服务器返回的数据，
     * @return
     */
    protected abstract boolean applyServerListDataAsyncC(List<BaseModel> bmList);

    /**
     * 向SQLite发送DB操作请求: 删除对象
     *
     * @param bm 想要删除的对象
     * @return
     */
    public abstract boolean deleteAsync(int iUseCaseID, BaseModel bm);

    /**
     * 在SQLite中同步查询所有数据
     *
     * @return
     */
    public abstract List<?> retrieveNSync(int iUseCaseID, BaseModel bm);

    /**
     * 在SQLite中异步查询单条数据
     */
    public boolean retrieve1ASync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    /**
     * 在SQLite中异步查询所有数据
     */
    public boolean retrieveNASync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    /**
     * 在SQLite中创建表
     */
    public abstract void createTableSync();

    public void setSqLiteEvent(BaseSQLiteEvent sqLiteEvent) {
        this.sqLiteEvent = sqLiteEvent;
    }

    public void setHttpEvent(BaseHttpEvent httpEvent) {
        this.httpEvent = httpEvent;
    }
}
