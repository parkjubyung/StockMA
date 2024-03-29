package com.kkongdak.authentication.config;

import ch.qos.logback.core.pattern.color.BoldCyanCompositeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailsService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())
                .requestMatchers("/static/**")
                .requestMatchers("/api/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/login", "/signup", "/account")
                .authenticated()
        );

        http.formLogin((loginConfigure) -> loginConfigure
                .loginPage("/login")
                .defaultSuccessUrl("/")
        );

        http.logout((logoutConfigure) -> logoutConfigure
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
        );

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailsService userDetailsService
                                                       )
            throws Exception {

        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

        return builder.build();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
