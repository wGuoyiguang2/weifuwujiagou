package com.authorize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *  将AuthenticationManager 注入IoC容器（ps： 认证管理器）
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("/user/p1").hasAnyAuthority("p1")
                //.antMatchers("/user/p2").hasAnyAuthority("p2")
                //.antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // authenticate 认证
        auth.inMemoryAuthentication()
                // 登录的账号和密码
                .withUser("guoyiguang")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .authorities("ROOT")
        ;
    }
}
