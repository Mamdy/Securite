package com.mamdy.web;

import com.mamdy.entities.AppUser;
import com.mamdy.request.JwtResponse;
import com.mamdy.security.JwtProvider;
import com.mamdy.service.interfa.AccountService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class UserController {

    private AuthenticationManager authenticationManager;

    private JwtProvider jwtProvider;

    private AccountService accountService;

    UserController(final AuthenticationManager authenticationManager, final JwtProvider jwtProvider, final AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.accountService = accountService;


    }

    @PostMapping("/register")
    AppUser register(@RequestBody UserFom userFom) {
        return accountService.saveUser(userFom.getUsername(), userFom.getPassword(), userFom.getConfirmedPassword(), userFom.getName(), userFom.getPhone(), userFom.getAddress());

    }

    //    @PostMapping("/login")
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<JwtResponse> login(@RequestBody UserFom userFom) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userFom.getUsername(), userFom.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generate(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            AppUser appUser = accountService.loadUserByUsername(userDetails.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwt, appUser.getEmail(), appUser.getName(), appUser.getRole()));


        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //return  accountService.saveUser(userFom.getUsername(),userFom.getPassword(), userFom.getConfirmedPassword(),userFom.getName(),userFom.getPhone(),userFom.getAddress());
    }


}

@Data
class UserFom {
    private String username;
    private String password;
    private String confirmedPassword;
    private String name;
    private String phone;
    private String address;
}
