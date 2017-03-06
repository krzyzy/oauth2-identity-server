package com.solidify.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAuthorizationServer
@EnableOAuth2Client
@EnableWebSecurity
@Configuration
public class IdentityServer extends WebMvcConfigurerAdapter {

	@Value("${ui.location}")
	private String uiLocation;

	public static void main(String[] args) {
		SpringApplication.run(IdentityServer.class, args);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("redirect:/app/");
        registry.addViewController("/app/").setViewName("forward:/app/index.html");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/app/**")
                .setCacheControl(CacheControl.noCache())
                .addResourceLocations(uiLocation);
	}

}
