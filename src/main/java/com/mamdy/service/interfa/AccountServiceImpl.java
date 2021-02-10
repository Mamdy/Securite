package com.mamdy.service.interfa;

import com.mamdy.dao.RolesDaoRepositrory;
import com.mamdy.dao.UserDaoRepository;
import com.mamdy.entities.AppRole;
import com.mamdy.entities.AppUser;
import com.mamdy.form.UserResetPasswordForm;
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
    public AppUser saveUser(final String email, final String password, final String confirmedPassword, final String firstName, final String lastName, final String phone, final String address) {
        //verifier dabord si le user existe deja:
        AppUser user = userDaoRepository.findByEmail(email);
        if (user != null) throw new RuntimeException("user already existe");
        if (!password.equals(confirmedPassword))
            throw new RuntimeException("Password not match, Please confirm your password");


        AppUser appUser = new AppUser();
        AppRole role = rolesDaoRepositrory.findByRoleName(appUser.getRole());
        appUser.getRoles().add(role);

        //ajouter le role adim à ce utilisateurs

        if ("balphamamoudou2013@gmail.com".equals(email)) {
            addRoleToUser(appUser, "ADMIN");

        }

        appUser.setEmail(email);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setPhone(phone);
        appUser.setAddress(address);
        appUser.setActived(true);
        appUser = userDaoRepository.save(appUser);

        return appUser;
    }

    @Override
    public AppUser updateUserPassword(AppUser user, UserResetPasswordForm userResetPasswordFormValue){
        if (!userResetPasswordFormValue.getNewPassword().equals(userResetPasswordFormValue.getConfirmedPassword()))
            throw new RuntimeException("Les mots de passe ne correspondent pas !,  Confirmez le mot de passe");

        user.setPassword(bCryptPasswordEncoder.encode(userResetPasswordFormValue.getNewPassword()));
        user = userDaoRepository.save(user);
        return user;
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return rolesDaoRepositrory.save(role);
    }

    @Override
    public AppUser loadUserByEmail(final String email) {

        return userDaoRepository.findByEmail(email);
    }


    private void addRoleToUser(final AppUser user, final String rolename) {

        AppRole role = rolesDaoRepositrory.findByRoleName(rolename);
        user.getRoles().add(role);
        user.setRole(role.getRoleName());

    }
}
