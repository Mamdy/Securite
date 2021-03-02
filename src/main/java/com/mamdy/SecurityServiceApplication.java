package com.mamdy;

import com.mamdy.dao.RolesDaoRepositrory;
import com.mamdy.dao.UserDaoRepository;
import com.mamdy.entities.AppRole;
import com.mamdy.service.interfa.AccountService;
import net.bytebuddy.utility.RandomString;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class SecurityServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(SecurityServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountService accountService, UserDaoRepository userDaoRepository, RolesDaoRepositrory rolesDaoRepositrory) {
        return args -> {
            rolesDaoRepositrory.deleteAll();
            accountService.saveRole(new AppRole(null, "CUSTOMER"));
            accountService.saveRole(new AppRole(null, "ADMIN"));
            userDaoRepository.deleteAll();
            Stream.of("balphamamoudou@yahoo.fr", "ismatbarry@yahoo.fr", "balphamamoudou2013@gmail.com", "alpha.barry@gfi.fr").forEach(email -> {
                accountService.saveUser(
                                "Mr",
                                email,
                            "1234",
                        "1234",
                        "ALpha" + RandomString.make(2),
                        "Barry" + RandomString.make(2),
                        "0666383403",
                        "04 allee pauline isabelle utiles",
                        "44200",
                        "Nantes",
                        "France");
            });
        };

    }

    //Definition du Bean Bcrypt au demarrage et de l'injecter
    @Bean
    BCryptPasswordEncoder getBCPE(){

        return new BCryptPasswordEncoder();
    }

}
