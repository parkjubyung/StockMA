package com.kkongdak.authentication.controller.config.jwt;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.kkongdak.authentication.config.jwt.JwtProperties;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtFactory {
    private String subject = "test@gmail.com";
    private Date issuedAt = new Date();
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = Collections.emptyMap();

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .setIssuer(jwtProperties.getIssuer())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .addClaims(claims)
                .compact();
    }
}
