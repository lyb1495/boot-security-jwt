/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.iyboklee.api.model.ApiResult;
import com.github.iyboklee.core.model.ApiUser;
import com.github.iyboklee.core.service.ApiUserService;
import com.github.iyboklee.exception.NotFoundException;
import com.github.iyboklee.security.SecurityService;

@RestController
@RequestMapping("user")
public class ApiUserRestController {

    @Autowired private SecurityService securityService;
    @Autowired private ApiUserService apiUserService;

    @RequestMapping(path = "me", method = RequestMethod.GET)
    public ApiResult<ApiUser> me() {
        ApiUser me = apiUserService.findOne(securityService.me());
        if (me == null)
            throw new NotFoundException(ApiUser.class.getSimpleName(), securityService.me());
        me.setPassword("[PROTECTED]");
        return new ApiResult<>(me);
    }

}
