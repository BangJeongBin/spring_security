package com.example.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()    // URL 기반 인가 설정
                .antMatchers("/user/**").hasRole("USER")    // USER 권한 사용자만 접근 가능
                .antMatchers("/admin/**").hasRole("ADMIN")    // ADMIN 권한 사용자만 접근 가능
                .antMatchers("/logout").authenticated()    // 인증받은 사용자만 접근 가능
                .antMatchers("/**").permitAll()    // 인증/인가 여부와 상관 없이 접근 가능
            .and()
                .logout()   // 로그아웃 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))    // 로그아웃 URL 지정
                .logoutSuccessHandler((req, res, auth) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 로그아웃을 위해 401 응답
                })
                .invalidateHttpSession(true)    // 세션 무효화
                .deleteCookies("JSESSIONID")  // JSESSIONID
                .and()// 쿠키 삭제
            .httpBasic();   // Http Basic 인증 사용

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
