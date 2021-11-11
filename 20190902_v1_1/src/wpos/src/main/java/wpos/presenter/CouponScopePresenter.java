package wpos.presenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.dao.CouponScopeMapper;
import wpos.model.ConfigGeneral;
import wpos.model.CouponScope;

import javax.annotation.Resource;
import javax.persistence.Table;

@Component("couponScopePresenter")
public class CouponScopePresenter extends BasePresenter{
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private CouponScopeMapper couponScopeMapper;

    public static final String QUERY_CouponScope_TABLE = "SELECT F_ID,F_CouponID,F_CommodityID,F_CommodityName FROM T_CouponScope ";

    @Override
    public void createTableSync() {
        couponScopeMapper.createTable();
    }

    @Override
    protected String getTableName() {
        if (CouponScope.class.isAnnotationPresent(Table.class)) {
            Table annotation = CouponScope.class.getAnnotation(Table.class);
            return annotation.name();
        } else {
            log.error("无法获取表名，未给" + CouponScope.class.getSimpleName() + "添加@Table。");
        }
        return "";
    }

    @Override
    protected String getQueryTable() {
        return QUERY_CouponScope_TABLE;
    }
}
