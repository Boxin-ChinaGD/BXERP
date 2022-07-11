package com.test.bx.app;

import com.bx.erp.utils.PoiUtils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoiUtilsTest {
    @Test
    public void readExcelTest() throws IOException {
        //这个路径是pos机的路径，excel文件不能太大，否则会出现内存不足的问题
        //可以在cmd的d:路径下用：adb push D:\BXERP\trunk\doc\手动测试文档\DEV\元粒度测试（单元测试）_nbr\machine.xlsx /sdcard/Excel/machine.xlsx 导入
        String file = "/sdcard/Excel/machine.xlsx";
        Map<String, Object> params = new HashMap<String, Object>();
        PoiUtils poiXls = new PoiUtils(file);
        List<String> listRow = poiXls.readExcelRow(file, 1, 0);
        List<String> listRow1 = poiXls.readExcelRow(file, 1, 1);
        Assert.assertTrue("读取xls文件失败！", listRow != null && listRow1 != null);
        for (int i = 0; i < listRow.size(); i++) {
            params.put(listRow.get(i), listRow1.get(i));
        }
        System.out.println(params);
    }

}

