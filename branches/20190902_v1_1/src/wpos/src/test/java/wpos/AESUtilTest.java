package wpos;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.utils.AESUtil;
import wpos.utils.Shared;

public class AESUtilTest extends BaseTestCase {

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    @Override
    public void tearDown() {
        super.tearDown();
        Shared.printTestClassEndInfo();
    }


    @Test
    public void test_a_AESUtil() {
        Shared.printTestMethodStartInfo();

        //纯大写英文
        AESUtil aes = new AESUtil();
        String str1 = "GFREFGJKMKOYRE";
        String str1Encryption = aes.encrypt(str1);
        String str1Decrypt = aes.decrypt(str1Encryption);
        Assert.assertNotNull(str1Encryption, "加密后不应该为空");
        Assert.assertNotNull(str1Decrypt, "解密后不应该为空");
        Assert.assertEquals(str1Decrypt, str1, "加密前与解密后的字符串应该一样");

        //特殊字符串
        String str2 = "@#￥%……&&*（（）~？》《";
        String str2Encryption = aes.encrypt(str2);
        String str2Decrypt = aes.decrypt(str2Encryption);
        Assert.assertNotNull(str2Encryption, "加密后不允许为空");
        Assert.assertNotNull(str2Decrypt, "解密后不应该为空");
        Assert.assertEquals(str2Decrypt, str2, "加密前与解密后的字符串应该一样");

        //纯数字
        String str3 = "12346546498416516549879846";
        String str3Encryption = aes.encrypt(str3);
        String str3Decrypt = aes.decrypt(str3Encryption);
        Assert.assertNotNull(str3Encryption, "加密后不允许为空");
        Assert.assertNotNull(str3Decrypt, "解密后不应该为空");
        Assert.assertEquals(str3Decrypt, str3, "加密前与解密后的字符串应该一样");

        //中文
        String str4 = "输入法时得高高的很容易坏人";
        String str4Encryption = aes.encrypt(str4);
        String str4Decrypt = aes.decrypt(str4Encryption);
        Assert.assertNotNull(str4Encryption, "加密后不允许为空");
        Assert.assertNotNull(str4Decrypt, "解密后不应该为空");
        Assert.assertEquals(str4Decrypt, str4, "加密前与解密后的字符串应该一样");

        //纯小写英文
        String str5 = "asdasdasdasdasdasdasdsgrtyrteueyrj";
        String str5Encryption = aes.encrypt(str5);
        String str5Decrypt = aes.decrypt(str5Encryption);
        Assert.assertNotNull(str5Encryption, "加密后不应该为空");
        Assert.assertNotNull(str5Decrypt, "解密后不应该为空");
        Assert.assertEquals(str5Decrypt, str5, "加密前与解密后的字符串应该一样");

        //多种混合
        String str6 = "输入法时得高高的很容易坏人1234654649asdasdasdasdasdasdasdsgrtyrteueyrj8416516549879846@#￥%……&&*（（）~？》《GFREFGJKMKOYRE";
        String str6Encryption = aes.encrypt(str6);
        String str6Decrypt = aes.decrypt(str6Encryption);
        Assert.assertNotNull(str6Encryption, "加密后不允许为空");
        Assert.assertNotNull(str6Decrypt, "解密后不应该为空");
        Assert.assertEquals(str6Decrypt, str6, "加密前与解密后的字符串应该一样");
    }
}
