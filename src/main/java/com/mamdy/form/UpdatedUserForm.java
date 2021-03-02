package com.mamdy.form;

import lombok.Data;

@Data
public class UpdatedUserForm {
    private Long id;
    private String email;
    private String password;
    private String civilite;
    private String lastName;
    private String firstName;
    private String phone;
    private String address;
    private String codePostal;
    private String ville;
    private String pays;
}