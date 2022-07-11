package wpos.bo;


import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.common.GlobalController;
import wpos.event.BaseHttpEvent;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RetailTradeCoupon;
import wpos.presenter.BasePresenter;
import wpos.presenter.RetailTradeCouponPresenter;

import javax.annotation.Resource;
import java.util.List;

@Component("retailTradeCouponSQLiteBO")
@Scope("prototype")
public class RetailTradeCouponSQLiteBO extends BaseSQLiteBO{
    private Logger log = Logger.getLogger(this.getClass());

    public RetailTradeCouponSQLiteBO(BaseSQLiteEvent sEvent, BaseHttpEvent hEvent) {
        sqLiteEvent = sEvent;
        httpEvent = hEvent;
    }

    public RetailTradeCouponSQLiteBO() {
    }

    @Resource
    private RetailTradeCouponPresenter retailTradeCouponPresenter;

    public void setRetailTradeCouponPresenter(RetailTradeCouponPresenter retailTradeCouponPresenter) {
        this.retailTradeCouponPresenter = retailTradeCouponPresenter;
    }

    @Override
    public BaseModel createSync(int iUseCaseID, final BaseModel bm) {
        log.info("正在执行RetailTradeCouponSQLiteBO的createSync，bm=" + bm);

        if(bm != null){
            RetailTradeCoupon rtc = (RetailTradeCoupon) bm;
            String checkMsg = rtc.checkCreate(iUseCaseID);
            if (!"".equals(checkMsg)) {
                sqLiteEvent.setLastErrorCode(ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField);
                sqLiteEvent.setLastErrorMessage(checkMsg);
                return null;
            }

            switch (iUseCaseID) {
                case CASE_RetailTradeCoupon_CreateSync:
                    RetailTradeCoupon retailTradeCoupon = (RetailTradeCoupon) retailTradeCouponPresenter.createObjectSync(iUseCaseID, bm);
                    if (retailTradeCoupon != null) {
                        return retailTradeCoupon;
                    } else {
                        log.info("创建临时零售单优惠券使用表失败");
                    }
                    break;
                default:
                    log.info("未定义的事件！");
                    throw new RuntimeException("未定义的事件！");
            }
        }

        return null;
    }

    @Override
    public boolean createAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean applyServerDataListAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    public boolean updateAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsync(BaseModel bmNew) {
        return false;
    }

    @Override
    protected boolean applyServerDataUpdateAsyncN(List<?> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerDataDeleteAsync(BaseModel bmDelete) {
        return false;
    }

    @Override
    protected boolean applyServerListDataAsync(List<BaseModel> bmList) {
        return false;
    }

    @Override
    protected boolean applyServerListDataAsyncC(List<BaseModel> bmList) {
        return false;
    }

    @Override
    public boolean deleteAsync(int iUseCaseID, BaseModel bm) {
        return false;
    }

    @Override
    public List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        return null;
    }

    @Override
    public void createTableSync() {
        retailTradeCouponPresenter.createTableSync();
    }

}
