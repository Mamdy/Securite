package com.mamdy.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    public String generate(Authentication authentication) {
        //si authentification est reussi on genere le token
        User user = (User) authentication.getPrincipal();
        List<String> roles = new ArrayList<>();
        authentication.getAuthorities().forEach(a -> {
            roles.add(a.getAuthority());
        });
        String jwt;
        jwt = JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(new Date())
                .withArrayClaim("roles", roles.toArray(new String[roles.size()]))
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityParams.EXPIRATION))
                .sign(Algorithm.HMAC256(SecurityParams.SECRET));

        return jwt;


    }

    public boolean validate(String token) {
        try {

            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
            String jwt = token.substring(SecurityParams.HEADER_PREFIX.length());
            DecodedJWT decodedJWT = verifier.verify(jwt);

            return true;
        } catch (Exception e) {
            logger.error("JWT Authentication Failed");
        }
        return false;
    }

    public String getUserAccount(String token) {

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        String username = decodedJWT.getSubject();

        return username;
    }
}
