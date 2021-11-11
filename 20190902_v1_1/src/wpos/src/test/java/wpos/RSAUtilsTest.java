package wpos;

import org.testng.Assert;
import org.testng.annotations.Test;
import wpos.base.BaseTestCase;
import wpos.utils.RSAUtils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

public class RSAUtilsTest extends BaseTestCase {
    @Test
    public void testRSAEncryption() throws Exception {
        final String pwd = "000000";

        HashMap<String, Object> map = RSAUtils.getKeys();
        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");


        String mi = RSAUtils.encryptByPublicKey(pwd, publicKey);
        System.out.println("加密后：" + mi);

        String ming = RSAUtils.decryptByPrivateKey(mi, privateKey);
        System.out.println("解密后：" + ming);

        Assert.assertTrue(ming.equals(pwd),"解密失败");
    }
}
