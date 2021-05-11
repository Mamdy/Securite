package com.mamdy.web;

import com.mamdy.dao.UserDaoRepository;
import com.mamdy.entities.AppUser;
import com.mamdy.form.UpdatedUserForm;
import com.mamdy.form.UserCredantialForm;
import com.mamdy.form.UserRegisterForm;
import com.mamdy.form.UserResetPasswordForm;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final AccountService accountService;

    private final UserDaoRepository userDaoRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    UserController(final AuthenticationManager authenticationManager,
                   final JwtProvider jwtProvider,
                   final AccountService accountService,
                   final UserDaoRepository userDaoRepository,
                   final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.accountService = accountService;
        this.userDaoRepository = userDaoRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/getUserByEmail/{email}")
    AppUser getUserByEmail(@PathVariable("email")final String email) {
        return  this.accountService.loadUserByEmail(email);

    }

    @GetMapping("/profile/{email}")
    AppUser getUserEmail(@PathVariable("email")final String email) {
        return  this.accountService.loadUserByEmail(email);
    }

    @GetMapping("/{id}")
    AppUser gerUserById(@PathVariable("id")  final Long id) {
        return  this.accountService.findUserById(id);

    }
    @PostMapping("/register")
    AppUser register(@RequestBody UserRegisterForm userFom) {
        return accountService.saveUser(
                userFom.getCivilite(),
                userFom.getEmail(),
                userFom.getPassword(),
                userFom.getConfirmedPassword(),
                userFom.getFirstName(),
                userFom.getLastName(),
                userFom.getPhone(),
                userFom.getAddress(),
                userFom.getCodePostal(),
                userFom.getVille(),
                userFom.getPays()
        );


    }

    @PutMapping(value = "/profile/{id}")
    public  AppUser updateUser(
                                @RequestBody UpdatedUserForm newUser,
                                @PathVariable("id") final Long id){
        return  this.userDaoRepository.findById(id)
                    .map(user->{
                        user.setCivilite(newUser.getCivilite());
                        user.setEmail(newUser.getEmail());
                        if(!newUser.getPassword().isEmpty()){
                            user.setPassword(this.bCryptPasswordEncoder.encode(newUser.getPassword()));
                        }

                        user.setFirstName(newUser.getFirstName());
                        user.setLastName(newUser.getLastName());
                        user.setPhone(newUser.getPhone());
                        user.setAddress(newUser.getAddress());
                        user.setCodePostal(newUser.getCodePostal());
                        user.setVille(newUser.getVille());
                        user.setPays(newUser.getPays());
                        return userDaoRepository.save(user);
                    })
                .orElseGet(()->{
                    newUser.setId(id);
                    AppUser user = new AppUser();
                    user.setId(newUser.getId());
                    return userDaoRepository.save(user);
                });



    }

    @PatchMapping(value = "/passwordReset/{id}")
    public boolean resetUserPassword(@RequestBody UserResetPasswordForm userResetPasswordFormValue,
                                     @PathVariable("id") final Long id){
        AtomicBoolean isUserPasswordReseated = new AtomicBoolean(false);
        Optional<AppUser> user = this.userDaoRepository.findById(id);
        user.ifPresent(user1-> {
            this.accountService.updateUserPassword(user1,userResetPasswordFormValue);
                    isUserPasswordReseated.set(true);
        });


        return isUserPasswordReseated.get();
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<JwtResponse> login(@RequestBody UserCredantialForm credential) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generate(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            AppUser user = accountService.loadUserByEmail(userDetails.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwt, user));


        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }


}

