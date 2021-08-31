package com.bike.ztd.util;

import com.google.common.collect.Lists;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


public class JWTUtils {
    /**
     * 用户id
     */
    private static final String USER_ID = Claims.SUBJECT;
    /**
     * 创建时间
     */
    private static final String CREATED = "created";
    /**
     * 权限列表
     */
    private static final String AUTHORITIES = "authorities";

    /**
     * 密钥
     */
    private static final String SECRET = "ZTD_SECRET";

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String userId) {
        String id = getUserIdFromToken(token);
        return (id.equals(userId) && !isTokenExpired(token));
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @Description: 生成令牌
     * @Author: fly
     * @Date: 2020/8/26 9:17
     */
    public static String generateToken(String userId, long expireTime) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(USER_ID, userId);
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, Lists.newArrayList());
        return generateToken(claims, expireTime);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims, long expireTime) {
        Long now = System.currentTimeMillis();
        Date expirationDate = new Date(now + expireTime);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public static String getToken(HttpServletRequest request) {

        String tokenHead = "Bearer ";

        String token = request.getHeader("Authorization");
        if (token == null) {
            token = request.getHeader("token");
            if (token == null) {
                token = request.getHeader("Mobile-Authorization");
            }
        }

        if (StringUtils.isBlank(token)) {
            return null;
        } else if (token.contains(tokenHead)) {
            token = token.substring(tokenHead.length());
        }

        if (StringUtils.isBlank(token)) {
            token = null;
        }

        return token;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUserIdFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * @Description: 获取token中的userId
     */
    public static String getUserIdByToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = getToken(request);
        if (token == null) {
            return null;
        } else {
            return getUserIdFromToken(token);
        }
    }

    public static String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = getToken(request);
        if (token == null) {
            return null;
        } else {
            if (!isTokenExpired(token)) {
                return token;
            }
        }
        return null;
    }
}
