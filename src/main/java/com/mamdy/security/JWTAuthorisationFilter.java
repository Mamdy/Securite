package com.mamdy.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class JWTAuthorisationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        //authoriser les requête provenant d'un domaine x=> vers mon domaine
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, authorization");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
        response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH");
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON));
        if (request.getMethod().equals("OPTIONS") || request.getMethod().equals("GET") ) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;

        } else if (request.getRequestURI().equals("/api/login") || request.getRequestURI().equals("/api/register") || request.getRequestURI().contains("/api/passwordReset") || request.getRequestURI().contains("/api/profile") || request.getRequestURI().contains("/api/profileUpdate")) {
            filterChain.doFilter(request, response);
            return;

        } else {
            String jwtToken = getToken(request);
            log.info("TOKEN=" + jwtToken);
            if (jwtToken != null && jwtProvider.validate(jwtToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
            String jwt = jwtToken.substring(SecurityParams.HEADER_PREFIX.length());

            DecodedJWT decodedJWT = verifier.verify(jwt);
            log.info("jwt= " + jwt);
            String username = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);
            log.info("username= " + username);
            log.info("roles= " + roles);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role));

            });

            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(user);
            filterChain.doFilter(request, response);

        }


    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        return null;
    }
}
