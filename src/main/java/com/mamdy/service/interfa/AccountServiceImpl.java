package com.mamdy.service.interfa;

import com.mamdy.dao.RolesDaoRepositrory;
import com.mamdy.dao.UserDaoRepository;
import com.mamdy.entities.AppRole;
import com.mamdy.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserDaoRepository userDaoRepository;
    @Autowired
    private RolesDaoRepositrory rolesDaoRepositrory;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public AppUser saveUser(String username, String password, String confirmedPassword, String name, String phone, String adress) {
        //verifier dabord si le user existe deja:
        AppUser user = userDaoRepository.findByUsername(username);
        if (user != null) throw new RuntimeException("user already existe");
        if (!password.equals(confirmedPassword))
            throw new RuntimeException("Password not match, Please confirm your password");


        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUser.setName(name);
        appUser.setPhone(phone);
        appUser.setAddress(adress);
        appUser.setActived(true);
        appUser = userDaoRepository.save(appUser);
        if (appUser.getUsername() == "admin") {
            addRoleToUser(username, "ADMIN");

        } else {
            addRoleToUser(username, "CUSTOMER");
        }
        appUser = userDaoRepository.save(appUser);
        return appUser;
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return  rolesDaoRepositrory.save(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {

        return userDaoRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser user = userDaoRepository.findByUsername(username);
        AppRole role = rolesDaoRepositrory.findByRoleName(rolename);
        user.setRole(role.getRoleName());
        ;
        user.getRoles().add(role);

    }
}
