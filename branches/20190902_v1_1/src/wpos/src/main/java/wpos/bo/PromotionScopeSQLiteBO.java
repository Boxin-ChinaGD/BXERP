package wpos.bo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wpos.dao.PromotionScopeMapper;
import wpos.model.BaseModel;

import javax.annotation.Resource;
import java.util.List;

@Component("promotionScopeSQLiteBO")
@Scope("prototype")
public class PromotionScopeSQLiteBO extends BaseSQLiteBO{
    @Resource
    private PromotionScopeMapper promotionScopeMapper;

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
        promotionScopeMapper.createTable();
    }
}
