package com.bx.erp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bx.erp.model.DaoMaster;
import com.bx.erp.model.DaoSession;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class ExampleInstrumentedTest {
  @Test
  public void useAppContext() throws Exception {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();

    assertEquals(10, sum(5,5));
  }
  public int sum(int a,int b){
    return a+b;
  }

  @Test
  public void addition_isCorrect() throws Exception {
    System.out.print("QQQQQQQQQQQQQQQQQQQQQQ1");
    assertEquals(4, 2 + 2);
    System.out.print("QQQQQQQQQQQQQQQQQQQQQQ2");

    DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(InstrumentationRegistry.getTargetContext(), "POS", null);
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
