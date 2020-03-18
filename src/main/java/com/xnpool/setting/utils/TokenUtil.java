package com.xnpool.setting.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    final static String base64EncodedSecretKey = "base64EncodedSecretKey";//私钥final

    public static String getToken(String userName, long TOKEN_EXP) {
        return Jwts.builder()
                .setSubject("LoginToken")
                .claim("userName", userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXP)) /*过期时间*/
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey)
                .compact();
    }

    public static String getTokenByMap(Map<String, Object> var1, Date expDate) {
        return Jwts.builder()
                .setSubject("LoginToken")
                .setClaims(var1)
                .setIssuedAt(new Date())
                .setExpiration(expDate) /*过期时间*/
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey)
                .compact();
    }


    public static String getTokenByMapNoTim(Map<String, Object> var1) {
        return Jwts.builder()
                .setClaims(var1)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey)
                .compact();
    }

    //校验token
    public static Claims parseJWT(String token){
        return Jwts.parser()
                .setSigningKey(base64EncodedSecretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    //解析token
    public static JSONObject checkToken(String token){
        int success = 500;
        Claims claims = null;
        String msg = "解析失败";
        try {
            claims = parseJWT(token);
        } catch (ExpiredJwtException e1) {
            e1.printStackTrace();
            msg="token已失效,请重新登录!";
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> reusltMap = new HashMap<>();
        if (claims != null) {
            success = 200;
            msg = "解析成功";
        }
        reusltMap.put("success", success);
        reusltMap.put("msg", msg);
        reusltMap.put("data", claims);
        return JSONObject.parseObject(JSON.toJSONString(reusltMap));
    }




}
