package com.mamdy.service.interfa;

import com.mamdy.entities.AppRole;
import com.mamdy.entities.AppUser;

public interface AccountService {
    AppUser saveUser(final String email, final String password, final String confirmedPassword, final String firstName, final String lastName, final String phone, final String address);

    AppRole saveRole(AppRole role);

    AppUser loadUserByEmail(final String email);

}
