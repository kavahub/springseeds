package org.springseed.oss.local.util;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springseed.core.util.typeof.TypeOf;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 安全工具
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
@Slf4j
public class SecurityUtils {
    public final static String PREFERRED_USERNAME = "preferred_username";

    public String getCurrentUsername() {
        return getCurrentUser().map(loginUser -> loginUser.getUsername())
                .orElseThrow(() -> new InternalAuthenticationServiceException("未登录"));
    }

    public String getCurrentUserInfo() {
        return getCurrentUser().map(loginUser -> loginUser.info()).orElse(null);
    }

    public Optional<LoginUser> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (log.isDebugEnabled()) {
            final Authentication authentication = securityContext.getAuthentication();
            log.debug("Get current user by {}",
                    authentication == null ? null : authentication.getClass().getSimpleName());
        }

        final LoginUser user = TypeOf.whenTypeOf(securityContext.getAuthentication())
                // jwt令牌
                .is(JwtAuthenticationToken.class).thenReturn(authentication -> {
                    final Jwt jwt = authentication.getToken();
                    return LoginUser.builder().username(jwt.getClaimAsString(PREFERRED_USERNAME))
                            .name(jwt.getClaimAsString("name")).build();
                })
                // 普通令牌
                .is(UsernamePasswordAuthenticationToken.class).thenReturn(authentication -> {
                    return TypeOf.whenTypeOf(authentication.getPrincipal())
                            .is(UserDetails.class)
                            .thenReturn(principal -> LoginUser.builder().username(principal.getUsername()).build())
                            .is(DefaultOidcUser.class).thenReturn(principal -> {
                                Map<String, Object> attributes = principal.getAttributes();
                                if (attributes.containsKey(PREFERRED_USERNAME)) {
                                    return LoginUser.builder().username((String) attributes.get(PREFERRED_USERNAME))
                                            .build();
                                }
                                return null;
                            })
                            .is(String.class).thenReturn(principal -> LoginUser.builder().username(principal).build())
                            .get();
                }).orElse((LoginUser) null);
        return Optional.ofNullable(user);
    }

    @Data
    @Builder
    public static class LoginUser {
        private String username;
        private String name;

        public String info() {
            final StringBuilder sb = new StringBuilder(username);
            if (StringUtils.hasText(name)) {
                sb.append("(").append(name).append(")");
            }
            return sb.toString();
        }
    }
}
