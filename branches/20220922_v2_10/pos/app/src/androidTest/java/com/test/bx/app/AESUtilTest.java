package com.test.bx.app;

import com.base.BaseAndroidTestCase;
import com.bx.erp.utils.AESUtil;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AESUtilTest extends BaseAndroidTestCase {
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
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void test_a_AESUtil() {
        Shared.printTestMethodStartInfo();

        //纯大写英文
        AESUtil aes = new AESUtil();
        String str1 = "GFREFGJKMKOYRE";
        String str1Encryption = aes.encrypt(str1);
        String str1Decrypt = aes.decrypt(str1Encryption);
        Assert.assertTrue("加密后不应该为空", str1Encryption != null);
        Assert.assertTrue("解密后不应该为空", str1Decrypt != null);
        Assert.assertTrue("加密前与解密后的字符串应该一样", str1.equals(str1Decrypt));

        //特殊字符串
        String str2 = "@#￥%……&&*（（）~？》《";
        String str2Encryption = aes.encrypt(str2);
        String str2Decrypt = aes.decrypt(str2Encryption);
        Assert.assertTrue("加密后不允许为空", str2Encryption != null);
        Assert.assertTrue("解密后不应该为空", str2Decrypt != null);
        Assert.assertTrue("加密前与解密后的字符串应该一样", str2.equals(str2Decrypt));

        //纯数字
        String str3 = "12346546498416516549879846";
        String str3Encryption = aes.encrypt(str3);
        String str3Decrypt = aes.decrypt(str3Encryption);
        Assert.assertTrue("加密后不允许为空", str3Encryption != null);
        Assert.assertTrue("解密后不应该为空", str3Decrypt != null);
        Assert.assertTrue("加密前与解密后的字符串应该一样", str3.equals(str3Decrypt));

        //中文
        String str4 = "输入法时得高高的很容易坏人";
        String str4Encryption = aes.encrypt(str4);
        String str4Decrypt = aes.decrypt(str4Encryption);
        Assert.assertTrue("加密后不允许为空", str4Encryption != null);
        Assert.assertTrue("解密后不应该为空", str4Decrypt != null);
        Assert.assertTrue("加密前与解密后的字符串应该一样", str4.equals(str4Decrypt));

        //纯小写英文
        String str5 = "asdasdasdasdasdasdasdsgrtyrteueyrj";
        String str5Encryption = aes.encrypt(str5);
        String str5Decrypt = aes.decrypt(str5Encryption);
        Assert.assertTrue("加密后不应该为空", str5Encryption != null);
        Assert.assertTrue("解密后不应该为空", str5Decrypt != null);
        Assert.assertTrue("加密前与解密后的字符串应该一样", str5.equals(str5Decrypt));

        //多种混合
        String str6 = "输入法时得高高的很容易坏人1234654649asdasdasdasdasdasdasdsgrtyrteueyrj8416516549879846@#￥%……&&*（（）~？》《GFREFGJKMKOYRE";
        String str6Encryption = aes.encrypt(str6);
        String str6Decrypt = aes.decrypt(str6Encryption);
        Assert.assertTrue("加密后不允许为空", str6Encryption != null);
        Assert.assertTrue("解密后不应该为空", str6Decrypt != null);
        Assert.assertTrue("加密前与解密后的字符串应该一样", str6.equals(str6Decrypt));
    }
}
