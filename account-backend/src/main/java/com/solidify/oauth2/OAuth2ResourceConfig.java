package com.solidify.oauth2;

import com.solidify.oauth2.social.*;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.*;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.social.facebook.connect.FacebookServiceProvider;
import org.springframework.social.github.connect.GitHubServiceProvider;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by tomasz on 05.10.16.
 */
@Configuration
@RestController
public class OAuth2ResourceConfig extends WebSecurityConfigurerAdapter {

    private int order = 3;

    @Autowired(required = false)
    private TokenStore tokenStore;

    @Autowired(required = false)
    private AuthenticationEventPublisher eventPublisher;

    @Autowired(required = false)
    private Map<String, ResourceServerTokenServices> tokenServices;

    @Autowired
    private ApplicationContext context;

    private List<ResourceServerConfigurer> configurers = Collections.emptyList();

    @Autowired(required = false)
    private AuthorizationServerEndpointsConfiguration endpoints;

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Autowired(required = false)
    public void setConfigurers(List<ResourceServerConfigurer> configurers) {
        this.configurers = configurers;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .permitAll()
                .and();

        ResourceServerSecurityConfigurer resources = new ResourceServerSecurityConfigurer();
        resources.stateless(false);
        ResourceServerTokenServices services = this.resolveTokenServices();
        if (services != null) {
            resources.tokenServices(services);
        } else if (this.tokenStore != null) {
            resources.tokenStore(this.tokenStore);
        } else if (this.endpoints != null) {
            resources.tokenStore(this.endpoints.getEndpointsConfigurer().getTokenStore());
        }

        if (this.eventPublisher != null) {
            resources.eventPublisher(this.eventPublisher);
        }

        Iterator var4 = this.configurers.iterator();

        ResourceServerConfigurer configurer;
        while (var4.hasNext()) {
            configurer = (ResourceServerConfigurer) var4.next();
            configurer.configure(resources);
        }

        http
                .authenticationProvider(new AnonymousAuthenticationProvider("default"))
                    .exceptionHandling()
                    .accessDeniedHandler(resources.getAccessDeniedHandler())
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/login")
                    .and()
                .authorizeRequests()
                    .antMatchers("/img*//**", "/webjars*//**").permitAll()
                    .antMatchers("/oauth*//**").fullyAuthenticated()
                    .antMatchers("/app*//**").fullyAuthenticated()
                    .anyRequest().fullyAuthenticated()
                    .and()
                .httpBasic()
                    .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .permitAll()
                    .and()
                .csrf()
                    .disable();

        http.apply(resources);

        var4 = this.configurers.iterator();

        while (var4.hasNext()) {
            configurer = (ResourceServerConfigurer) var4.next();
            configurer.configure(http);
        }

        if (this.configurers.isEmpty()) {
            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.authorizeRequests().anyRequest()).authenticated();
        }


        http.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    private ResourceServerTokenServices resolveTokenServices() {
        if(this.tokenServices != null && this.tokenServices.size() != 0) {
            if(this.tokenServices.size() == 1) {
                return this.tokenServices.values().iterator().next();
            } else {
                if(this.tokenServices.size() == 2) {
                    Iterator iter = this.tokenServices.values().iterator();
                    ResourceServerTokenServices one = (ResourceServerTokenServices)iter.next();
                    ResourceServerTokenServices two = (ResourceServerTokenServices)iter.next();
                    if(this.elementsEqual(one, two)) {
                        return one;
                    }
                }

                return this.context.getBean(ResourceServerTokenServices.class);
            }
        } else {
            return null;
        }
    }

    private boolean elementsEqual(Object one, Object two) {
        if(one == two) {
            return true;
        } else {
            Object targetOne = this.findTarget(one);
            Object targetTwo = this.findTarget(two);
            return targetOne == targetTwo;
        }
    }

    private Object findTarget(Object item) {
        Object current = item;

        while(current instanceof Advised) {
            try {
                current = ((Advised)current).getTargetSource().getTarget();
            } catch (Exception var4) {
                ReflectionUtils.rethrowRuntimeException(var4);
            }
        }

        return current;
    }

    private static class NotOAuthRequestMatcher implements RequestMatcher {
        private FrameworkEndpointHandlerMapping mapping;

        public NotOAuthRequestMatcher(FrameworkEndpointHandlerMapping mapping) {
            this.mapping = mapping;
        }

        public boolean matches(HttpServletRequest request) {
            String requestPath = this.getRequestPath(request);
            Iterator var3 = this.mapping.getPaths().iterator();

            String path;
            do {
                if(!var3.hasNext()) {
                    return true;
                }

                path = (String)var3.next();
            } while(!requestPath.startsWith(this.mapping.getPath(path)));

            return false;
        }

        private String getRequestPath(HttpServletRequest request) {
            String url = request.getServletPath();
            if(request.getPathInfo() != null) {
                url = url + request.getPathInfo();
            }

            return url;
        }
    }


    @RequestMapping("/api/user")
    public Object user() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @Bean
    @ConfigurationProperties("github")
    public OAuth2ServerConfig.OAuth2ClientResources github() {
        return new OAuth2ServerConfig.OAuth2ClientResources();
    }

    @Bean
    @ConfigurationProperties("facebook")
    public OAuth2ServerConfig.OAuth2ClientResources facebook() {
        return new OAuth2ServerConfig.OAuth2ClientResources();
    }


    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<Filter>();
        final OAuth2ServerConfig.OAuth2ClientResources client1 = facebook();
        OAuth2ClientAuthenticationProcessingFilter filter2 = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
        OAuth2RestTemplate template1 = new OAuth2RestTemplate(client1.getClient(), oauth2ClientContext);
        filter2.setRestTemplate(template1);
        filter2.setTokenServices(new SocialTokenService(
                new ProfileProviderFactory() {
                    public ProfileProvider createProfileProvider(AccessGrant accessGrant) {
                        return new FacebookProfileProvider(
                                new FacebookServiceProvider(client1.getClient().getClientId(), client1.getClient().getClientSecret(), null).getApi(accessGrant.getAccessToken())
                        );
                    }
                },
                client1.getClient().getClientId()
        ));

        OAuth2ServerConfig.OAuth2ClientResources client = github();
        OAuth2ClientAuthenticationProcessingFilter filter1 = new OAuth2ClientAuthenticationProcessingFilter("/login/github");
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter1.setRestTemplate(template);
        filter1.setTokenServices(new SocialTokenService(
                new ProfileProviderFactory() {
                    public ProfileProvider createProfileProvider(AccessGrant accessGrant) {
                        return new GithubProfileProvider(
                                new GitHubServiceProvider(client1.getClient().getClientId(), client1.getClient().getClientSecret()).getApi(accessGrant.getAccessToken())
                        );
                    }
                },
                client1.getClient().getClientId()
        ));


        filters.add(filter2);
        filters.add(filter1);
        filter.setFilters(filters);
        return filter;
    }

}
