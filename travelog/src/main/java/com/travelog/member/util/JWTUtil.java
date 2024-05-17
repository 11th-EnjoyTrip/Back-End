package com.travelog.member.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;


@Slf4j
@Component
public class JWTUtil {
    @Value("${jwt.salt}")
    private String salt;
    @Value("${jwt.access-token.expiretime}")
    private long accessTokenExpireTime;
    @Value("${jwt.refresh-token.expiretime}")
    private long refreshTokenExpireTime;

    public String createAccessToken(String id) {
        return create(id, "access-token", accessTokenExpireTime);
    }

    public String createRefreshToken(String id) {
        return create(id, "refresh-token", refreshTokenExpireTime);
    }

    private String create(String id, String subject, long expireTime) {
        Claims claims = Jwts.claims()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime));
        claims.put("Id", id);

        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT").setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact();

        return jwt;
    }

    private byte[] generateKey() {
        byte[] key = null;
        try {
            key = salt.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return key;
    }

    public boolean checkToken(String token) {
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public String getUserId(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(token);
            return claims.getBody().get("Id").toString();
        } catch (Exception e) {
            return "fail";
        }
    }
}
