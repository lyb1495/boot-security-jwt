/**
 * @Author yboklee (iyboklee@gmail.co.kr)
 */
package com.github.iyboklee.core.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
public class ApiUser {

    private String username;
    private String password;
    private String email;
    private String apiToken;
    private LocalDateTime logintime;

    public ApiUser(String username, String email) {
        this(username, null, email);
    }

    public ApiUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}