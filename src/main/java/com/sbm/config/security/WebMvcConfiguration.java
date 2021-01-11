package com.sbm.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/login").setViewName("login");
        registry.addViewController("/user/two_factor_authentication").setViewName("loginSecret");
        registry.addViewController("/oauth/confirm_access").setViewName("confirmAccess");
//        registry.addViewController("/user/user-login").setViewName("secondFactorTypes");
    }
}
