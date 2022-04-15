package org.springseed.resource.config;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@RequestMapping("/api/v1/user")
public class CustomUserAttrController {
    /**
     * 获取登录用户dob（day of birthday）属性
     */
    @GetMapping("/custom-attr")
	public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt principal) {
        Map<String, String> map = new Hashtable<String, String>();
		map.put("user_name", principal.getClaimAsString("preferred_username"));
		map.put("organization", principal.getClaimAsString("organization"));
        map.put("bod", principal.getClaimAsString("DOB"));
        return Collections.unmodifiableMap(map);
	}
}
