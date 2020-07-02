package com.mamdy.security;

import com.mamdy.entities.AppUser;
import com.mamdy.service.interfa.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserdetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String usernmame) throws UsernameNotFoundException {
        AppUser user = accountService.loadUserByUsername(usernmame);
        if(user==null) throw new UsernameNotFoundException("user not found");

        Collection<GrantedAuthority> authorities=new ArrayList<>();
        user.getRoles().forEach(r->{
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
        });

        return new User(user.getUsername(),user.getPassword(),authorities);
    }
}
