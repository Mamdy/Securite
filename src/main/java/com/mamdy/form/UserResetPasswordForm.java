package com.mamdy.form;

import lombok.Data;

@Data
public class UserResetPasswordForm {
    private String email;
    private String newPassword;
    private String confirmedPassword;
}