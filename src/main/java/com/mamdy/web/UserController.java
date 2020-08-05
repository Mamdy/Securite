package com.mamdy.web;

import com.mamdy.entities.AppUser;
import com.mamdy.form.UserCredantialForm;
import com.mamdy.form.UserRegisterForm;
import com.mamdy.request.JwtResponse;
import com.mamdy.security.JwtProvider;
import com.mamdy.service.interfa.AccountService;
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
    AppUser register(@RequestBody UserRegisterForm userFom) {
        return accountService.saveUser(userFom.getEmail(), userFom.getPassword(), userFom.getConfirmedPassword(), userFom.getFirstName(), userFom.getLastName(), userFom.getPhone(), userFom.getAddress());


    }

    //    @PostMapping("/login")
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<JwtResponse> login(@RequestBody UserCredantialForm credential) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generate(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            AppUser user = accountService.loadUserByEmail(userDetails.getUsername());
            String fullName = user.getFirstName() + " " + user.getLastName();
            return ResponseEntity.ok(new JwtResponse(jwt, user));


        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //return  accountService.saveUser(userFom.getUsername(),userFom.getPassword(), userFom.getConfirmedPassword(),userFom.getName(),userFom.getPhone(),userFom.getAddress());
    }


}

