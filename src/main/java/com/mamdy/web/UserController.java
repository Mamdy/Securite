package com.mamdy.web;

import com.mamdy.entities.AppUser;
import com.mamdy.service.interfa.AccountService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {
    @Autowired
    private AccountService accountService;
    @PostMapping("/register")
    AppUser register(@RequestBody UserFom userFom){
        return  accountService.saveUser(userFom.getUsername(),userFom.getPassword(), userFom.getConfirmedPassword());

    }
}

@Data
class UserFom{
    private  String username;
    private String password;
    private String confirmedPassword;
}
