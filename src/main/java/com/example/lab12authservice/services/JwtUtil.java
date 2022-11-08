package com.example.lab12authservice.services;

import com.example.lab12authservice.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Date getExpirationDateFromToken(String token){
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generate(User userV0, String type){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",userV0.getId());
        claims.put("role",userV0.getRole());
        return doGenerateToken(claims,userV0.getEmail(),type);

    }

    private String doGenerateToken(Map<String,Object> claims, String username, String type){
        long expirationTimeLong;
        if("ACCESS".equals(type)){
            expirationTimeLong = Long.parseLong(expirationTime)*1000;
        }else{
            expirationTimeLong = Long.parseLong(expirationTime) * 1000 * 5;
        }
        final Date createDate = new Date();
        final Date expirationDate = new Date(createDate.getTime()+expirationTimeLong);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token){
        return !isTokenExpired(token);
    }
}
