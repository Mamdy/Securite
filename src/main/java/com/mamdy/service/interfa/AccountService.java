package com.mamdy.service.interfa;

import com.mamdy.entities.AppRole;
import com.mamdy.entities.AppUser;

public interface AccountService
{
    public AppUser saveUser(String username, String password, String confirmedPassword, String name, String phone, String adress);

    public AppRole saveRole(AppRole role);
    public AppUser loadUserByUsername(String username);
    public void addRoleToUser(String username, String rolename );


}
