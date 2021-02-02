package com.sbm.config.security;

import java.time.Duration;
import java.util.Arrays;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sbm.config.security.service.UrlAuthenticationSuccessHandler;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableWebSecurity
@Order(1)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private PasswordEncoder passwordEncoder;
	@Value("${ui.url}")
	private String uiUrl;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().antMatcher("/**").authorizeRequests()
				.antMatchers("/api/**", "/admin/**", "/h2-console/**", "/user/status", "/css/**", "/js/**",
						"/resourses/**", "/images/**", "/user/**login**", "/user/two_factor_authentication",
						"/user/second-factor", "/oauth/authorize**", "/oauth/confirm_access**", "/confirm", "/error**")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/user/login")
				.failureUrl("/user/login?error").successHandler(myAuthenticationSuccessHandler()).permitAll().and()
				.logout().permitAll();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		//        configuration.setAllowedOrigins(Arrays.asList(uiUrl));
		configuration.setAllowedOrigins(Arrays.asList("*"));

		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new UrlAuthenticationSuccessHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		if (this.passwordEncoder == null) {
			this.passwordEncoder = DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder();
		}
		return this.passwordEncoder;
	}

	@Bean
	public FilterRegistrationBean twoFactorAuthenticationFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(twoFactorAuthenticationFilter());
		registration.setName("twoFactorAuthenticationFilter");
		return registration;
	}

	@Bean
	public TwoFactorAuthenticationFilter twoFactorAuthenticationFilter() {
		return new TwoFactorAuthenticationFilter();
	}


	@Bean
	public RestTemplate restTemplate(
			RestTemplateBuilder restTemplateBuilder) {

		return restTemplateBuilder
				.setConnectTimeout(Duration.ofSeconds(70))
				.setReadTimeout(Duration.ofSeconds(70))
				.build();
	}
}
