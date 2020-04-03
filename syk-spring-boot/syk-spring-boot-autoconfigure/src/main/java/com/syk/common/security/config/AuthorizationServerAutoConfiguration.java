package com.syk.common.security.config;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.syk.common.security.enhancer.CustomTokenEnhancer;
import com.syk.common.security.property.AuthorizationServerProperty;

/**
 * @author zhouxiajie
 * @date 2019-01-23
 */
@Configuration
@ConditionalOnClass({AuthorizationServerProperty.class, CustomTokenEnhancer.class})
@EnableAuthorizationServer
@EnableConfigurationProperties(AuthorizationServerProperty.class)
public class AuthorizationServerAutoConfiguration extends AuthorizationServerConfigurerAdapter {
    private static final String JKS_PATH = "nse-iov.jks";

    private static final String JKS_PASSWORD = "nse-iov";

    private static final String JKS_ALIAS = "nse-iov";

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final AuthorizationServerProperty authorizationServerProperty;

    public AuthorizationServerAutoConfiguration(AuthenticationManager authenticationManager,
        UserDetailsService userDetailsService, AuthorizationServerProperty authorizationServerProperty) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.authorizationServerProperty = authorizationServerProperty;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(authorizationServerProperty.getClientId())
            .secret(authorizationServerProperty.getClientSecret())
            .authorizedGrantTypes(authorizationServerProperty.getAuthorizedGrantTypes().toArray(new String[] {}))
            .scopes(authorizationServerProperty.getScopes().toArray(new String[] {}))
            .accessTokenValiditySeconds(authorizationServerProperty.getAccessTokenValiditySeconds())
            .refreshTokenValiditySeconds(authorizationServerProperty.getRefreshTokenValiditySeconds());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        endpoints.tokenStore(jwtTokenStore()).tokenEnhancer(tokenEnhancerChain).reuseRefreshTokens(true)
            .authenticationManager(authenticationManager).userDetailsService(userDetailsService);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyStoreKeyFactory =
            new KeyStoreKeyFactory(new ClassPathResource(JKS_PATH), JKS_PASSWORD.toCharArray());
        jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair(JKS_ALIAS));
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients().tokenKeyAccess("denyAll()").checkTokenAccess("isAuthenticated()");
    }
}
