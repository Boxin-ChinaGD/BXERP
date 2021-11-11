package com.bx.erp;

import android.test.AndroidTestCase;

import com.bx.erp.model.DaoMaster;
import com.bx.erp.model.DaoSession;

import org.junit.Test;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest extends AndroidTestCase {
  @Test
  public void addition_isCorrect() throws Exception {
      System.out.print("QQQQQQQQQQQQQQQQQQQQQQ1");
    assertEquals(4, 2 + 2);
      System.out.print("QQQQQQQQQQQQQQQQQQQQQQ2");

    DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(), "POS", null);
    DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
    DaoSession daoSession = daoMaster.newSession();
//    ClientModelDao vipDao = daoSession.getClientModelDao();
//        ClientModel cm = new ClientModel();
//        cm.setAccount("a/c");
//        cm.setBirthday("2008");
//    List<ClientModel> userList = vipDao.queryRaw("1=1");//.queryBuilder().where("1=1");//.where(1=1);
//    System.out.println("xxxxxxxxxxx=" + userList.toString());
//    Assert.assertNotNull(userList);
  }
}