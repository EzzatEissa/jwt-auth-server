package com.sbm.config.security;

import com.sbm.config.security.service.UrlAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Order(1)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

//    private final DataSource dataSource;

    private PasswordEncoder passwordEncoder;
    
    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Value("${ui.url}")
    private String uiUrl;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/api/**", "/admin/**", "/h2-console/**", "/user/status","/css/**","/resourses/**","/images/**", "/login**","/secure/two_factor_authentication", "/oauth/authorize**", "/oauth/confirm_access**", "/error**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().formLogin().loginPage("/login").failureUrl("/login?error").successHandler(myAuthenticationSuccessHandler())
                .permitAll().and().logout().permitAll();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(uiUrl));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new UrlAuthenticationSuccessHandler();
    }
    
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        if (passwordEncoder == null) {
            passwordEncoder = DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
        return passwordEncoder;
    }


    @Bean
    public FilterRegistrationBean twoFactorAuthenticationFilterRegistration(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(twoFactorAuthenticationFilter());
        registration.setName("twoFactorAuthenticationFilter");
        return registration;
    }

    @Bean
    public TwoFactorAuthenticationFilter twoFactorAuthenticationFilter(){
        return new TwoFactorAuthenticationFilter();
    }

}
