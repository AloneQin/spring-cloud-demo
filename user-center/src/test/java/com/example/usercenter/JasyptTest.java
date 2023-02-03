package com.example.usercenter;

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;

public class JasyptTest extends BaseTest {

    @Test
    public void test() {
        DefaultLazyEncryptor defaultLazyEncryptor = new DefaultLazyEncryptor(new StandardEnvironment());
        System.out.println("加密：" + defaultLazyEncryptor.encrypt("redis#default"));
        System.out.println("解密：" + defaultLazyEncryptor.decrypt("JvTlxyvdlViaMZrE9pt9pHdVxjrgm3Stwe+goD85PVBF4mrTn+HUVdbhlA3DsgR3"));
    }

}
