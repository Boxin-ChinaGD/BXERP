package wpos.presenter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wpos.dao.RememberLoginStaffMapper;
import wpos.event.UI.BaseSQLiteEvent;
import wpos.model.BaseModel;
import wpos.model.ErrorInfo;
import wpos.model.RememberLoginStaff;

import javax.annotation.Resource;
import java.util.List;

@Component("rememberLoginStaffPresenter")
public class RememberLoginStaffPresenter extends BasePresenter {
    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    RememberLoginStaffMapper rememberLoginStaffMapper;

    @Override
    public void createTableSync() {
        rememberLoginStaffMapper.createTable();
    }

    @Override
    protected boolean createAsync(int iUseCaseID, final BaseModel bm, BaseSQLiteEvent event) {
        log.debug("xxxxxxxxxxxxx  RememberLoginStaffPresenter  createAsync");

        new Thread() {
            @Override
            public void run() {
                globalWriteLock.writeLock().lock();
                // 因为只记住一个账号密码，所以删除了表中的其他账号密码，再插入新的，以后需要记住多个账号密码需要修改 deleteAll()；
                try {
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
                    lastErrorMessage = "";

//                    dao.getRememberLoginStaffDao().deleteAll();
//                    dao.getRememberLoginStaffDao().insert((RememberLoginStaff) bm);
                    rememberLoginStaffMapper.deleteAll();
                    rememberLoginStaffMapper.save((RememberLoginStaff) bm);
                } catch (Exception e) {
                    log.info("执行RememberLoginStaffPresenter.createAsync()时出错，错误信息：" + e.getMessage());
                    e.printStackTrace();
                    lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
                    lastErrorMessage = e.getMessage();
                    //... 由于记住密码失败也无所谓，所以此处不需要post event 出去给UI层接收。无非要求收银员下次再输入多一次密码而已
                }
                globalWriteLock.writeLock().unlock();
            }
        }.start();

        return true;
    }

    @Override
    protected List<?> retrieveNSync(int iUseCaseID, BaseModel bm) {
        log.debug("xxxxxxxxxxxxx  RememberLoginStaffPresenter  retrieveNSync");

        try {
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_NoError;
            lastErrorMessage = "";
            return rememberLoginStaffMapper.findAll();
        } catch (Exception e) {
            log.info("执行RememberLoginStaffPresenter.retrieveNSync()时出错，错误信息：" + e.getMessage());
            e.printStackTrace();
            lastErrorCode = ErrorInfo.EnumErrorCode.EC_OtherError;
            lastErrorMessage = e.getMessage();
        }
        return null;
    }

}
