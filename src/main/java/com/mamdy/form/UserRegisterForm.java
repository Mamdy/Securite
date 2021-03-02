package com.mamdy.form;

import lombok.Data;

@Data
public class UserRegisterForm {
    private String civilite;
    private String email;
    private String lastName;
    private String firstName;
    private String password;
    private String confirmedPassword;
    private String phone;
    private String address;
    private String codePostal;
    private String ville;
    private String pays;
}