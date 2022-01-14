package com.example.jwt.config;

import com.example.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfig corsConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsConfig.corsFilter())     //  @CrossOrigin(인증X), 필터에 등록 => 인증(O)
                .formLogin().disable()
                .httpBasic().disable()                  // header에 authorization에 ID와 PW를 넣어서 가져가는 방식.(http의 경우에 노출), Token의 경우에는 Bearer 방식임.
                .authorizeHttpRequests()
                .antMatchers("/api/v1/user/**").hasAnyRole("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")
                .antMatchers("/api/v1/manager/**").hasAnyRole("ROLE_USER", "ROLE_MANAGER")
                .antMatchers("/api/v1/admin/**").hasAnyRole("ROLE_USER")
                .anyRequest().permitAll();
    }
}
