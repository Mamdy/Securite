package com.mamdy.request;

import com.mamdy.entities.AppUser;
import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
//    private String account;
//    private String name;
//    private String role;
    private AppUser user;

//    public JwtResponse(String token, String account, String name, String role) {
//        this.account = account;
//        this.name = name;
//        this.token = token;
//        this.role = role;
//    }

    public JwtResponse(String token, AppUser user){
        this.token = token;
        this.user = user;
    }
}
