package com.test.bx.app;

import android.test.AndroidTestCase;

import com.bx.erp.model.BaseModel;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class BaseModelTest extends AndroidTestCase {
    protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
        throw new RuntimeException("Not yet implemented!");
    }

    /** 修改对象的任一可比较的字段，令对象比较时不要相等 */
    protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
        throw new RuntimeException("Not yet implemented!");
    }

    protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException {
        throw new RuntimeException("Not yet implemented!");
    }

    /** 修改对象的任一可比较的字段，令对象比较时不要相等 */
    protected List<BaseModel> updateSlaveTableObject(List<BaseModel> slave) {
        throw new RuntimeException("Not yet implemented!");
    }

    public void compareTo_Case1() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case1: 俩个对象数据一样 ");
        BaseModel master = getMasterTableObject();
        List<BaseModel> slave = getSlaveTableObject();
        master.setListSlave1(slave);
        //
        BaseModel master1 = master.clone();
        BaseModel master2 = master.clone();
        Assert.assertTrue("比较失败", master1.compareTo(master2) == 0);
    }

    public void compareTo_Case2() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case2: 俩个对象数据不一样 ");
        BaseModel master = getMasterTableObject();
        List<BaseModel> slave = getSlaveTableObject();
        master.setListSlave1(slave);
        //
        BaseModel master1 = master.clone();
        BaseModel master2 = updateMasterTableObject(master);
        Assert.assertTrue("比较失败", master1.compareTo(master2) == -1);

    }

    public void compareTo_Case3() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case3: 俩个对象的主表信息相同，从表A和B都为null");
        BaseModel master = getMasterTableObject();
        //
        BaseModel master1 = master.clone();
        BaseModel master2 = master.clone();
        Assert.assertTrue("比较失败", master1.compareTo(master2) == 0);

    }

    public void compareTo_Case4() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case4: 俩个对象的主表信息相同，从表A为null，B不为null，B的size为0，返回0。");
        BaseModel master = getMasterTableObject();
        //
        BaseModel master1 = master.clone();
        BaseModel master2 = master.clone();
        List<BaseModel> slave = new ArrayList<BaseModel>();
        master2.setListSlave1(slave);
        Assert.assertTrue("比较失败", master1.compareTo(master2) == 0);

    }

    public void compareTo_Case5() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case5: 俩个对象的主表信息相同，从表A为null，B不为null，B的size大于0，返回-1。");
        BaseModel master = getMasterTableObject();
        //
        BaseModel master1 = master.clone();
        BaseModel master2 = master.clone();
        List<BaseModel> slave = getSlaveTableObject();
        master2.setListSlave1(slave);
        Assert.assertTrue("比较失败", master1.compareTo(master2) == -1);

    }

    public void compareTo_Case6() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case6: 俩个对象的主表信息相同，从表A不为null，B为null，A的size为0，返回0。");
        BaseModel master = getMasterTableObject();
        //
        BaseModel master1 = master.clone();
        List<BaseModel> slave = new ArrayList<BaseModel>();
        master1.setListSlave1(slave);
        BaseModel master2 = master.clone();
        Assert.assertTrue("比较失败", master1.compareTo(master2) == 0);
    }

    public void compareTo_Case7() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case7: 俩个对象的主表信息相同，从表A不为null，B为null，A的size大于0，返回-1。");
        BaseModel master = getMasterTableObject();
        //
        BaseModel master1 = master.clone();
        List<BaseModel> slave = getSlaveTableObject();
        master1.setListSlave1(slave);
        BaseModel master2 = master.clone();
        Assert.assertTrue("比较失败", master1.compareTo(master2) == -1);
    }

    public void compareTo_Case8() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case8: 俩个对象的主表信息相同，从表A和B都不为null，A和B的size相等，从表循环compare通过，返回0。");
        BaseModel master = getMasterTableObject();
        List<BaseModel> slave = getSlaveTableObject();
        master.setListSlave1(slave);
        //
        BaseModel master1 = master.clone();
        BaseModel master2 = master.clone();
        Assert.assertTrue("比较失败", master1.compareTo(master2) == 0);
    }

    public void compareTo_Case9() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case9: 俩个对象的主表信息相同，从表A和B都不为null，A和B的size相等，从表循环compare不通过，返回-1。");
        BaseModel master = getMasterTableObject();
        List<BaseModel> slave = getSlaveTableObject();
        master.setListSlave1(slave);
        //
        BaseModel master1 = master.clone();
        BaseModel master2 = master.clone();
        master2.setListSlave1(updateSlaveTableObject(slave));
        Assert.assertTrue("比较失败", master1.compareTo(master2) == -1);
    }

    public void compareTo_Case10() throws CloneNotSupportedException, InterruptedException {
        System.out.println("case10: 俩个对象的主表信息相同，从表信息不相同 但设置为不比较从表信息");
        BaseModel master = getMasterTableObject();
        List<BaseModel> slave = getSlaveTableObject();
        master.setListSlave1(slave);
        //
        BaseModel master1 = master.clone();
        BaseModel master2 = master.clone();
        master2.setListSlave1(updateSlaveTableObject(slave));
        //
        master1.setIgnoreSlaveListInComparision(true);
        Assert.assertTrue("比较失败", master1.compareTo(master2) == 0);
    }
}
