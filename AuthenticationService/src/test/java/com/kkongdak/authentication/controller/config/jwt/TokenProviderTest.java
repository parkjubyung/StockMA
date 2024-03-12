package com.kkongdak.authentication.controller.config.jwt;

import com.kkongdak.authentication.config.jwt.JwtProperties;
import com.kkongdak.authentication.config.jwt.TokenProvider;
import com.kkongdak.authentication.domain.Account;
import com.kkongdak.authentication.repository.AccountRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    void generateToken() {
        Account account = accountRepository.save(Account.builder()
                .email("test@gmail.com")
                .password("test")
                .passwordExpiryDays(4)
                .build());

        String token = tokenProvider.generateToken(account, Duration.ofDays(14));

        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(account.getId());
    }

    @Test
    void validToken_invalidToken() {
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);
        assertThat(result).isFalse();
    }

    @Test
    void validToken_validToken() {
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() + Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);
        assertThat(result).isTrue();
    }

    @Test
    void getAuthentication() {
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        Authentication authentication = tokenProvider.getAuthentication(token);
        assertThat(((UserDetails)(authentication.getPrincipal())).getUsername()).isEqualTo(userEmail);
    }
}
