package com.syk.common.security.enhancer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.nse.common.constants.NseCommonConstants;
import com.syk.common.security.entity.NseUser;

/**
 * @author zhouxiajie
 * @date 2019-02-03
 */
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        NseUser user = (NseUser)authentication.getPrincipal();

        final Map<String, Object> additionalInfo = new HashMap<>(16);

        additionalInfo.put("user_id", user.getId());

        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}
