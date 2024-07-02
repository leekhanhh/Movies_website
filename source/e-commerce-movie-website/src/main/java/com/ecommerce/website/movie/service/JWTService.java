package com.ecommerce.website.movie.service;

import com.ecommerce.website.movie.model.Account;
import io.jsonwebtoken.*;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JWTService {
    @Value("${jwt.secret-key}")
    private String secret;
    private static final long EXPIRE_DURATION = 31L * 24 * 60 * 60 * 1000;

    public String buildToken(
            Account account
    ) {
        return Jwts
                .builder()
                .claim("id", account.getId())
                .claim("role", Integer.parseInt(account.getAuthorities().toArray()[0].toString()))
                .claim("username", account.getUsername())
                .setSubject(account.getId() + "," + account.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired", ex);
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or has only whitespace", ex);
        } catch (MalformedJwtException ex) {
            log.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            log.error("Signature validation failed", ex);
        }
        return false;
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
