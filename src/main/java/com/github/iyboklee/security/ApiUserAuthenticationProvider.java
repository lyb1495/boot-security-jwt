/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.github.iyboklee.core.model.ApiUser;
import com.github.iyboklee.core.service.ApiUserService;

@Component
public class ApiUserAuthenticationProvider implements AuthenticationProvider {

    @Value("${jwt.token.role}") private String role;

    @Autowired private ApiUserService apiUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();

        ApiUser apiUser = apiUserService.login(principal, credentials);
        //-- 인증 성공 처리
        apiUser.setPassword("[PROTECTED]");
        ApiUserAuthenticationToken authenticated = new ApiUserAuthenticationToken(apiUser.getUsername(), null, generateAuthorities());
        authenticated.setDetails(apiUser);
        return authenticated;
    }

    private Collection<GrantedAuthority> generateAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_" + role);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiUserAuthenticationToken.class.isAssignableFrom(authentication);
    }

}