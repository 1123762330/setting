package com.xnpool.setting.utils;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/8 21:04
 */
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    public static final String TOKEN_SECRET = "test_key";

    public JwtUtil() {
    }

    public static Map<String, String> verify(String token) {
        HashMap<String, String> reslut = new HashMap();
        if (token == null) {
            reslut.put("username", "匿名");
            //reslut.put("roles", "游客");
            reslut.put("tenant_id", "-1");
            reslut.put("mine_id", "-1");
            reslut.put("id", "-1");
            return reslut;
        } else {
            try {
                Claims claims = (Claims)Jwts.parser().setSigningKey("test_key".getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
                String username = claims.get("user_name").toString();
                //String roles = claims.get("authorities").toString();
                String tenant_id = claims.get("tenant_id").toString();
                String mine_id = claims.get("mine_id").toString();
                String id = claims.get("id").toString();
                reslut.put("username", username);
                //reslut.put("roles", roles);
                reslut.put("tenant_id", tenant_id);
                reslut.put("mine_id", mine_id);
                reslut.put("id", id);
                return reslut;
            } catch (Exception var8) {
                HashMap<String, String> err = new HashMap();
                err.put("username", "error");
                //err.put("roles", var8.getMessage());
                err.put("tenant_id", "-1");
                err.put("mine_id", "-1");
                err.put("id", "-1");
                return err;
            }
        }
    }
}
