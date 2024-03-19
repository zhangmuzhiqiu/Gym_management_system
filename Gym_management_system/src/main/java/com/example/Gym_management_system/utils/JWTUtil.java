package com.example.Gym_management_system.utils;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.exception.ServiceException;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    private static final String KEY = "zhuangmuzhiqiu";
    private static final long TIME = 1000 * 60 * 60 * 12;

    //    设置token令牌
    public static String getToken(JWTLoginEntity jwtLoginEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("password", jwtLoginEntity.getPassword());
        claims.put("username", jwtLoginEntity.getUsername());
        claims.put("roles", jwtLoginEntity.getRoles());
//        过期时间
        Date now = new Date();
        long expTime = now.getTime() + TIME;
        Date exp = new Date(expTime);

        String jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, KEY)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .compact();
        return jwtBuilder;
    }

    //    解析token令牌
    public static JWTLoginEntity getJWTUser(String token) {
        //校验token
        Claims claims = parseToken(token);
        //获取用户名称、密码
        String username = claims.get("username").toString();
        String password = claims.get("password").toString();
        Integer roles = (Integer) claims.get("roles".toString());
        JWTLoginEntity user = new JWTLoginEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(roles);
        return user;
    }

    //    检验token合法性
    public static Claims parseToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(KEY)//设置签名
                    .parseClaimsJws(token) //设置解析的token
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ServiceException("解析token异常:token已经失效");
        } catch (UnsupportedJwtException e) {
            throw new ServiceException("解析token异常:不支持的token解析");
        } catch (MalformedJwtException e) {
            throw new ServiceException("解析token异常:非法token格式");
        } catch (SignatureException e) {
            throw new ServiceException("解析token异常：token的签名不合法");
        } catch (IllegalArgumentException e) {
            throw new ServiceException("解析token异常：非法token参数");
        }
        return claims;
    }


}
