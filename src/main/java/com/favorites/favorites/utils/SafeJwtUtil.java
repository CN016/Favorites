package com.favorites.favorites.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.*;

public class SafeJwtUtil {

    private static final String key = UUID.randomUUID().toString();

    public static String generate(Map<String, Object> claims) {
        String generate = JwtUtil.generate(claims);
        try {
            return EncryptionUtils.encrypt(generate,key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务器异常，请联系管理员");
        }
    }

    public static Claims getClaim(String token) {
        try {
            token = EncryptionUtils.decrypt(token,key);
            return JwtUtil.getClaim(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务器异常，请联系管理员");
        }
    }

    private static class JwtUtil {

        private static final long EXPIRE = 600 * 3 * 1000; //过期时间

        public static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);//密钥，动态生成的密钥

        /**
         * 生成token
         *
         * @param claims 要传送消息map
         * @return
         */
        public static String generate(Map<String, Object> claims) {
            Date nowDate = new Date();
            //过期时间,设定为三十分钟
            Date expireDate = new Date(System.currentTimeMillis() + EXPIRE);
            //头部信息,可有可无
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "jwt");

            return Jwts.builder().setHeader(header)
                    .setClaims(claims)  //自定义claims
                    .setIssuedAt(nowDate)//当前时间
                    .setExpiration(expireDate) //过期时间
                    .signWith(key)//签名算法和key
                    .compact();
        }

        /**
         * 生成token
         *
         * @param header 传入头部信息map
         * @param claims 要传送消息map
         * @return
         */
        public static String generate(Map<String, Object> header, Map<String, Object> claims) {
            Date nowDate = new Date();
            //过期时间,设定为十分钟
            Date expireDate = new Date(2099,12,20);

            return Jwts.builder().setHeader(header)
                    .setClaims(claims)  //自定义claims
                    .setIssuedAt(nowDate)//当前时间
                    .setExpiration(expireDate) //过期时间
                    .signWith(key)//签名算法和key
                    .compact();
        }

        /**
         * 校验是不是jwt签名
         *
         * @param token
         * @return
         */
        public static boolean isSigned(String token) {
            return Jwts.parser()
                    .setSigningKey(key)
                    .isSigned(token);
        }

        /**
         * 校验签名是否正确
         *
         * @param token
         * @return
         */
        public static boolean verify(String token) {
            try {
                Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        /**
         * 获取payload 部分内容（即要传的信息）
         * 使用方法：如获取userId：getClaim(token).get("userId");
         *
         * @param token
         * @return
         */
        public static Claims getClaim(String token) {
            Claims claims = null;
            try {
                claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token)
                        .getBody();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return claims;
        }

        /**
         * 获取头部信息map
         * 使用方法 : getHeader(token).get("alg");
         *
         * @param token
         * @return
         */
        public static JwsHeader getHeader(String token) {
            JwsHeader header = null;
            try {
                header = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token)
                        .getHeader();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return header;
        }

        /**
         * 获取jwt发布时间
         */
        public static Date getIssuedAt(String token) {
            return getClaim(token).getIssuedAt();
        }

        /**
         * 获取jwt失效时间
         */
        public static Date getExpiration(String token) {
            return getClaim(token).getExpiration();
        }

        /**
         * 验证token是否失效
         *
         * @param token
         * @return true:过期   false:没过期
         */
        public static boolean isExpired(String token) {
            try {
                final Date expiration = getExpiration(token);
                return expiration.before(new Date());
            } catch (ExpiredJwtException expiredJwtException) {
                return true;
            }
        }

        /**
         * 直接Base64解密获取header内容
         *
         * @param token
         * @return
         */
        public static String getHeaderByBase64(String token) {
            String header = null;
            if (isSigned(token)) {
                try {
                    byte[] header_byte = Base64.getDecoder().decode(token.split("\\.")[0]);
                    header = new String(header_byte);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return header;
        }

        /**
         * 直接Base64解密获取payload内容
         *
         * @param token
         * @return
         */
        public static String getPayloadByBase64(String token) {
            String payload = null;
            if (isSigned(token)) {
                try {
                    byte[] payload_byte = Base64.getDecoder().decode(token.split("\\.")[1]);
                    payload = new String(payload_byte);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return payload;
        }

    }

}
