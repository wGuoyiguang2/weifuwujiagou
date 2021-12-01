package com.authorize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login.html", "/css/**", "/js/**", "/images/**");
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                //.antMatchers("/user/p1").hasAnyAuthority("p1")
//                //.antMatchers("/user/p2").hasAnyAuthority("p2")
//                //.antMatchers("/login").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//        ;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                // 对认证相关的端点放行
                //.antMatchers("/login")
                // /sso-login 在本服务中根本没有
                .antMatchers("/sso-login")
                .antMatchers("/oauth/authorize")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                // 配置一下登录页面和登录接口
                // 这个自己页面提供的
                .loginPage("/login.html")
                // 实际上 这个接口在本服务中没有，因为这个链接会被重定向 到 /oauth/authorize去获取授权码，
                // 最后调用回调 eg http://localhost:8091/login?code=MPsHXe&state=d0cDdR
                .loginProcessingUrl("/sso-login")
                .permitAll()
                .and()
                .csrf().disable();
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
