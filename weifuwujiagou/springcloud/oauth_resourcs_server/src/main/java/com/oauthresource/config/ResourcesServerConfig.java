package com.oauthresource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;


@Configuration
@EnableResourceServer
public class ResourcesServerConfig extends ResourceServerConfigurerAdapter {
    // 访问资源的编号
    public static final String RESOURCE_ID = "res1";


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID) // 资源编号
                .tokenServices(tokenServices()) // 验证token信息的服务
                .stateless(true);
    }

    /**
     *
     * 远程验证Token信息的服务
     *
     * 因为资源服务器和授权服务器是分开的 所以需要配置了一个 RemoteTokenServices 的实例，
     * 当用户来资源服务器请求资源时，会携带上一个 access_token，通过这里的配置，就能够校验出 token 是否正确等
     *
     */
    @Bean
    public ResourceServerTokenServices tokenServices(){
        RemoteTokenServices services = new RemoteTokenServices();
        // 去 Token的授权服务检查token是否合法(ps：资源服务器和授权服务器关联)
        services.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        services.setClientId("c1");
        services.setClientSecret("secret");
        return services;
    }

    /**
     * 资源服务的权限
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 配置一下资源的拦截规则(Spring Security 中的基本写法)
                //.antMatchers("/**").access("#oauth2.hasScope('all')")
                .antMatchers("/admin/**").hasRole("admin")
                .anyRequest().permitAll()
                .and().csrf().disable()
                // 设置Session为无状态服务 提升效率
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
