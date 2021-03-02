package com.mamdy.request;

import com.mamdy.entities.AppUser;
import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";

    private AppUser user;

    public JwtResponse(String token, AppUser user){
        this.token = token;
        this.user = user;
    }
}
