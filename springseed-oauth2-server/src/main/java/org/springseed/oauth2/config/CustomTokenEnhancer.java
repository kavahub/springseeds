package org.springseed.oauth2.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springseed.oauth2.operator.OperatorQueryHandler;

/**
 * 
 * 在token中附加其他信息
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
public class CustomTokenEnhancer implements TokenEnhancer {
    private final OperatorQueryHandler operatorQueryHandler;

    public CustomTokenEnhancer(OperatorQueryHandler operatorQueryHandler) {
        this.operatorQueryHandler = operatorQueryHandler;
    }
    
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        operatorQueryHandler.findByUsername(authentication.getName()).ifPresent(e -> {
            final Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("email", e.getEmail());
            additionalInfo.put("nickname", e.getNickname());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        });

        return accessToken;
    }
    
}
