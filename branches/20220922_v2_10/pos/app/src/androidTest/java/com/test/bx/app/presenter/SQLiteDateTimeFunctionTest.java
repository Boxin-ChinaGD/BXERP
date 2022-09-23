package com.test.bx.app.presenter;


import com.bx.erp.utils.Shared;
import com.base.BaseAndroidTestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;

public class SQLiteDateTimeFunctionTest extends BaseAndroidTestCase {
    //    private static RetailTradeAggregationPresenter presenter;
    private static String date = "2018-11-01 00:00:01";
    private static String date2 = "2018-10-01 00:00:01";
    private static String date3 = "1970-01-01 00:00:01";

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

//        presenter = new RetailTradeAggregationPresenter(getContext(), Constants.RETAILTRADEAGGREGATION_TABLE_NAME);
    }

    @Override
    public void tearDown() throws Exception {
        //AppApplication.syncThread.exit();
        //AppApplication.syncThread.join();

        super.tearDown();
    }

    @Test
    public void test_BetweenDate() throws CloneNotSupportedException, ParseException {
//        TaskScheduler.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
                   // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

//                    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), Constants.RETAILTRADEAGGREGATION_TABLE_NAME);
//                    Database db = helper.getReadableDb();
//                    DaoSession mDaoSession = new DaoMaster(db).newSession(IdentityScopeType.None);
//                    mDaoSession.getRetailTradeAggregationDao().deleteAll();
//
//                    List<RetailTradeAggregation> rtaList = mDaoSession.getRetailTradeAggregationDao().loadAll();
//
//                    RetailTradeAggregation rta = RetailTradeAggregationPresenterTest.DataInput.getRetailTradeAggregation();
//                    rta.setWorkTimeEnd(sdf.parse(date));
//
//                    long id = mDaoSession.getRetailTradeAggregationDao().insert(rta);
//                    rta.setID(id);
//                    System.out.println("插入数据的日期为：" + rta == null ? "null" : rta.getWorkTimeEnd());
//
//                    RetailTradeAggregation rta2 = RetailTradeAggregationPresenterTest.DataInput.getRetailTradeAggregation();
//                    rta2.setWorkTimeEnd(sdf.parse(date2));
//
//                    long id2 = mDaoSession.getRetailTradeAggregationDao().insert(rta2);
//                    rta2.setID(id2);
//                    System.out.println("插入数据的日期为：" + rta2 == null ? "null" : rta2.getWorkTimeEnd());
//
//                    RetailTradeAggregation rta3 = RetailTradeAggregationPresenterTest.DataInput.getRetailTradeAggregation();
//                    rta3.setWorkTimeEnd(sdf.parse(date3));
//
//                    long id3 = mDaoSession.getRetailTradeAggregationDao().insert(rta3);
//                    rta3.setID(id3);
//                    System.out.println("插入数据的日期为：" + rta3 == null ? "null" : rta3.getWorkTimeEnd());
//
//                    rtaList = mDaoSession.getRetailTradeAggregationDao().loadAll();
                    // 三个数据的时间段 "2018-11-01 00:00:00";  "2018-10-01 00:00:00";  "1970-01-01 00:00:00"
//                    BaseModel baseModel = new BaseModel();
//        baseModel.setSql(" where F_WorkTimeEnd between '2018-01-01 00:00:00' and '2018-12-31 00:00:00'");
//        baseModel.setSql(" where date(F_WorkTimeEnd) > date('now', 'start of month', '-2 month')");
//        baseModel.setSql(" where date(F_WorkTimeEnd) < '1970-01-01'");
//        baseModel.setSql(" where F_PosID < 3");
//                    baseModel.setSql(" WHERE julianday('now','localtime') * 86400 - julianday(F_WorkTimeEnd) * 86400 <= 864000");
//                    baseModel.setConditions(null);
//                    List<RetailTradeAggregation> list = mDaoSession.getRetailTradeAggregationDao().queryRaw(baseModel.getSql(), baseModel.getConditions());
//
//                    System.out.println("queryRaw数据=" + list == null ? "null" : list.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
          // 上面注释掉了 此段毫无意义
//        int i = 10;
//        while (i-- > 0) {
//            try {
//                Thread.sleep(1000);
//                System.out.println("XXXXXXXXXXXXXXXXX1111：");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("XXXXXXXXXXXXXXXXX：");
    }
}
