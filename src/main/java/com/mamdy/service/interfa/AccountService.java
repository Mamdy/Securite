package com.mamdy.service.interfa;

import com.mamdy.entities.AppRole;
import com.mamdy.entities.AppUser;
import com.mamdy.form.UserResetPasswordForm;

public interface AccountService {
    AppUser saveUser(
            final String civilite,
            final String email,
            final String password,
            final String confirmedPassword,
            final String firstName,
            final String lastName,
            final String phone,
            final String address,
            final String codePostal,
            final String ville,
            final String pays
            );
    AppUser updateUserPassword(AppUser user, UserResetPasswordForm userResetPasswordFormValue);
    AppUser updateUser(AppUser user);

    AppRole saveRole(AppRole role);

    AppUser loadUserByEmail(final String email);
    AppUser findUserById(final Long id);


}
