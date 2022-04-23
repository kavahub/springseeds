package org.springseed.oauth2;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@SpringBootTest(classes = { OAuth2Application.class })
@AutoConfigureMockMvc
public class OAuth2AuthorizationServerApplicationITests {
    protected static final String CLIENT_ID = "fooClient";
    protected static final String CLIENT_SECRET = "secret";
    protected static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    protected static final String DEFUALT_USERNAME = "zhangs";
    protected static final String DEFAULT_PASSWORD = "123";

    @Autowired
    protected MockMvc mockMvc;

    /**
     * 客户端正常登录测试
     * @throws Exception
     */
    @Test
    void performTokenRequestWhenClientCredentialsThenOk() throws Exception {
        this.mockMvc.perform(post("/oauth/token")
                .param("grant_type", "client_credentials")
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.expires_in").isNumber())
                .andExpect(jsonPath("$.token_type").value("bearer"));
    }

    /**
     * 客户端密码错误测试
     * 
     * @throws Exception
     */
    @Test
    void performTokenRequestWhenInvalidClientCredentialsThenUnauthorized() throws Exception {
        this.mockMvc.perform(post("/oauth/token")
                .param("grant_type", "client_credentials")
                .with(httpBasic(CLIENT_ID, "wrong_password"))
                .accept(CONTENT_TYPE))
                .andExpect(status().isUnauthorized());
    }

    /**
     * 用户登录正常测试
     * 
     * @throws Exception
     */
    @Test
    void performTokenRequestWhenPassowrdThenOk() throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", DEFUALT_USERNAME);
        params.add("password", DEFAULT_PASSWORD);

        this.mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").isString())
                .andExpect(jsonPath("$.expires_in").isNumber())
                .andExpect(jsonPath("$.token_type").value("bearer"));
    }

    /**
     * 用户登录密码错误测试
     * 
     * @throws Exception
     */
    @Test
    void performTokenRequestWhenInvalidPassowrdThenBadRequest() throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", DEFUALT_USERNAME);
        params.add("password", "1");

        this.mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_grant"));
    }

    @Test
    void performTokenRequestWhenInvalidUsernameThenBadRequest() throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "ABC");
        params.add("password", DEFAULT_PASSWORD);

        this.mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_grant"));
    }
}
