package com.test.bx.app;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Base64;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SmallSheetFrameTest extends BaseAndroidTestCase {

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_CheckCreate() {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setDelimiterToRepeat("-");
        //
        List<SmallSheetText> smallSheetTextList = new ArrayList<>();
        SmallSheetText smallSheetText;
        for(int i = 0; i < SmallSheetFrame.NO_SmallSheetTextItem; i++) {
            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("asd");
            smallSheetText.setSize(14);
            smallSheetText.setBold(1);
            smallSheetText.setFrameId(1L);
            smallSheetText.setGravity(1);
            smallSheetTextList.add(smallSheetText);
        }
        smallSheetFrame.setListSlave1(smallSheetTextList);
        String error = "";

        smallSheetFrame.setLogo("aaa");
        error = smallSheetFrame.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        smallSheetFrame.setLogo(Shared.getRandomString(17 * 10000));
        error = smallSheetFrame.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_logo);

    }

    @Test
    public void test_b_CheckUpdate() {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID(1l);
        smallSheetFrame.setDelimiterToRepeat("-");

        List<SmallSheetText> smallSheetTextList = new ArrayList<>();
        SmallSheetText smallSheetText;
        for(int i = 0; i < SmallSheetFrame.NO_SmallSheetTextItem; i++) {
            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("asd");
            smallSheetText.setSize(14);
            smallSheetText.setBold(1);
            smallSheetText.setFrameId(1L);
            smallSheetText.setGravity(1);
            smallSheetTextList.add(smallSheetText);
        }
        smallSheetFrame.setListSlave1(smallSheetTextList);
        String error = "";

        smallSheetFrame.setLogo("aaa");
        error = smallSheetFrame.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");

        smallSheetFrame.setLogo(Shared.getRandomString(17 * 10000));
        error = smallSheetFrame.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_logo);
    }

    @Test
    public void test_c_CheckRetrieveN() {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setPageIndex("1");
        smallSheetFrame.setPageSize("10");
        String error = smallSheetFrame.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // 测试PageIndex
        smallSheetFrame.setPageIndex("0");
        smallSheetFrame.setPageSize(Shared.PAGE_Size);
        error = smallSheetFrame.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
        // 测试PageSize
        smallSheetFrame.setPageIndex("1");
        smallSheetFrame.setPageSize("0");
        error = smallSheetFrame.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
    }

    @Test
    public void test_d_CheckDelete() {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID(0l);
        String error = smallSheetFrame.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        //
        smallSheetFrame.setID(-1l);
        error = smallSheetFrame.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        //
        smallSheetFrame.setID(1l);
        error = smallSheetFrame.checkDelete(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void test_e_CheckRetrieve1() {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID(1l);
        String error = smallSheetFrame.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        // 测试ID
        smallSheetFrame.setID(BaseSQLiteBO.INVALID_ID);
        error = smallSheetFrame.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
        smallSheetFrame.setID(1l);
    }

    @Test
    public void test_f_CheckCreate() {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setDelimiterToRepeat("-");
        //
        List<SmallSheetText> smallSheetTextList = new ArrayList<>();
        SmallSheetText smallSheetText;
        for(int i = 0; i < SmallSheetFrame.NO_SmallSheetTextItem; i++) {
            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("asd");
            smallSheetText.setSize(14);
            smallSheetText.setBold(1);
            smallSheetText.setFrameId(1L);
            smallSheetText.setGravity(1);
            smallSheetTextList.add(smallSheetText);
        }
        smallSheetFrame.setListSlave1(smallSheetTextList);
        String error = "";

        smallSheetFrame.setLogo("aaa");
        error = smallSheetFrame.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //小票底部空行数在0-5之间
        smallSheetFrame.setCountOfBlankLineAtBottom(1);
        error = smallSheetFrame.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //小票底部空行数小于0
        smallSheetFrame.setCountOfBlankLineAtBottom(-1);
        error = smallSheetFrame.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
        //小票底部空行数大于5
        smallSheetFrame.setCountOfBlankLineAtBottom(6);
        error = smallSheetFrame.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
    }

    @Test
    public void test_g_CheckUpdate() {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID(1l);
        smallSheetFrame.setDelimiterToRepeat("-");
        //
        List<SmallSheetText> smallSheetTextList = new ArrayList<>();
        SmallSheetText smallSheetText;
        for(int i = 0; i < SmallSheetFrame.NO_SmallSheetTextItem; i++) {
            smallSheetText = new SmallSheetText();
            smallSheetText.setID(2l);
            smallSheetText.setContent("asd");
            smallSheetText.setSize(14);
            smallSheetText.setBold(1);
            smallSheetText.setFrameId(1L);
            smallSheetText.setGravity(1);
            smallSheetTextList.add(smallSheetText);
        }
        smallSheetFrame.setListSlave1(smallSheetTextList);
        String error = "";

        smallSheetFrame.setLogo("aaa");
        error = smallSheetFrame.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //小票底部空行数在0-5之间
        smallSheetFrame.setCountOfBlankLineAtBottom(1);
        error = smallSheetFrame.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
        //小票底部空行数小于0
        smallSheetFrame.setCountOfBlankLineAtBottom(-1);
        error = smallSheetFrame.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
        //小票底部空行数大于5
        smallSheetFrame.setCountOfBlankLineAtBottom(6);
        error = smallSheetFrame.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom);
    }

    @Test
    public void test_h_CheckCreate() {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setLogo("aaa");
        smallSheetFrame.setCountOfBlankLineAtBottom(1);
        //
        List<SmallSheetText> smallSheetTextList = new ArrayList<>();
        SmallSheetText smallSheetText;
        for(int i = 0; i < SmallSheetFrame.NO_SmallSheetTextItem; i++) {
            smallSheetText = new SmallSheetText();
            smallSheetText.setContent("asd");
            smallSheetText.setSize(14);
            smallSheetText.setBold(1);
            smallSheetText.setFrameId(1L);
            smallSheetText.setGravity(1);
            smallSheetTextList.add(smallSheetText);
        }
        smallSheetFrame.setListSlave1(smallSheetTextList);
        String error = "";

        smallSheetFrame.setDelimiterToRepeat(null);
        error = smallSheetFrame.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, smallSheetFrame.FIELD_ERROR_delimiterToRepeat);
        smallSheetFrame.setDelimiterToRepeat("-");
        error = smallSheetFrame.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }

    @Test
    public void test_i_CheckUpdate() {
        Shared.printTestMethodStartInfo();

        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID(1l);
        //
        List<SmallSheetText> smallSheetTextList = new ArrayList<>();
        SmallSheetText smallSheetText;
        for(int i = 0; i < SmallSheetFrame.NO_SmallSheetTextItem; i++) {
            smallSheetText = new SmallSheetText();
            smallSheetText.setID(2l);
            smallSheetText.setContent("asd");
            smallSheetText.setSize(14);
            smallSheetText.setBold(1);
            smallSheetText.setFrameId(1L);
            smallSheetText.setGravity(1);
            smallSheetTextList.add(smallSheetText);
        }
        smallSheetFrame.setListSlave1(smallSheetTextList);
        String error = "";

        smallSheetFrame.setDelimiterToRepeat(null);
        error = smallSheetFrame.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, smallSheetFrame.FIELD_ERROR_delimiterToRepeat);
        smallSheetFrame.setDelimiterToRepeat("-");
        error = smallSheetFrame.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(error, "");
    }
}
