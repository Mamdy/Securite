package com.mamdy.form;

import lombok.Data;

@Data
public class UserRegisterForm {
    private String email;
    private String password;
    private String confirmedPassword;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}