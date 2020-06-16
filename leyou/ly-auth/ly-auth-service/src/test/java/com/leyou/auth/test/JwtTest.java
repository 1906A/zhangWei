package com.leyou.auth.test;

import com.leyou.auth.common.JwtUtils;
import com.leyou.auth.common.RsaUtils;
import com.leyou.auth.common.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {
    private static final String pubKeyPath = "C:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "C:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU5MjMyMjA4MX0.Rho4rFyxL5Gnbq0Z4ibYNJod64Hsf4FvOosQ7sByjriT29mKfmQ0nfFKH9CfyBGIcT0oZBSJc-O5yYbnTIDwtLaKXa_F5XqAmZ0gAvoyuMla9JqHBES5DNSvXjB2zUkx7B11e8S1ZNcYquZhbT_6g6Go3tAs6o_FoZQWOeyj04A";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
