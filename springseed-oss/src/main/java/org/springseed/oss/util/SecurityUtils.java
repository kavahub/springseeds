package org.springseed.oss.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import cn.hutool.core.text.StrFormatter;
import lombok.experimental.UtilityClass;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class SecurityUtils {
    public Optional<String> getCurrentUserInfo() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(SecurityUtils::isJwt)
                .map(authentication -> {
                    final Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();

                    return StrFormatter.format("{}({})", jwt.getClaimAsString("name"),
                            jwt.getClaimAsString("preferred_username"));
                });
    }

    private boolean isJwt(final Authentication authentication) {
        return authentication instanceof JwtAuthenticationToken;
    }
}
