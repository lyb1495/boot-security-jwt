/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.api.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.iyboklee.api.model.ApiResult;
import com.github.iyboklee.core.model.ApiUser;
import com.github.iyboklee.exception.UnauthorizedException;
import com.github.iyboklee.security.ApiUserAuthenticationToken;
import com.github.iyboklee.security.JWT;

@RestController
@RequestMapping("auth")
public class AuthenticationRestController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JWT jwt;

    private Map<String, Object> claims(ApiUser apiUser, Collection<? extends GrantedAuthority> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", apiUser.getUsername());
        claims.put("roles", roles);
        return claims;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResult<ApiUser> login(@RequestBody ApiUser apiUser) throws UnauthorizedException {
        try {
            Authentication authentication = new ApiUserAuthenticationToken(apiUser.getUsername(), apiUser.getPassword());
            authentication = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            apiUser = (ApiUser) authentication.getDetails();
            String token = jwt.generateToken(claims(apiUser, authentication.getAuthorities()));
            apiUser.setApiToken(token);

            return new ApiResult<>(apiUser);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

}