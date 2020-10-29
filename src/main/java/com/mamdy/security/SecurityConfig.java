package com.mamdy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserdetailsServiceImpl userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //authentification par la session
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    //personnalisation de l'authentification
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //1e methode: Autheneitfication par sessions STATEFULL
      /*  http.formLogin(); //on utilise le fomulare fait par spring
        //ici seul les le user quia le role ADMIN n'a le droit de gerer l'api des utilisateurs
        http.authorizeRequests().antMatchers("/appUsers/**","/appRoles/**").hasAuthority("ADMIN");
        //autrement, pour les autres users on leur authorise de naviguer dans l'appli si ils sont connectés
        http.authorizeRequests().anyRequest().authenticated();*/

        //2eme methode, on veut utiliser jwt json web token aulieu d'une authentification par session(STATELESS)
        //ça veut dire spring ne garde pas en memoire la session, mais le token

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS.STATELESS);
        //pas besoins de s'authentifier pour se loger ou s'enregistrer
        http.authorizeRequests().antMatchers("/api/login/**", "/api/register/**", "/api/test/**").permitAll();
//        http.authorizeRequests().antMatchers("/cart/**").hasAuthority("USER");
//        http.authorizeRequests().antMatchers("/profile/**").authenticated();
        http.authorizeRequests().antMatchers("/appUsers/**", "/appRoles/**").hasAuthority("ADMIN");
        //http.authorizeRequests().antMatchers("/api/login/**").hasAnyAuthority("ADMIN","ROLE_CUSTOMER");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(new JWTAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
