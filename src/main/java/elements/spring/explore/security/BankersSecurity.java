package elements.spring.explore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
//@EnableWebSecurity
public class BankersSecurity {

    @Autowired
    UserNotRoleService service;

    AuthenticationManager authenticationManager;

    @Autowired
    BankerUserSuccessHandler bankerUserSuccessHandler;

    @Autowired
    BankerUserFailureHandler bankerUserFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // with random generated password

        httpSecurity.authorizeRequests().antMatchers("/users/sign").permitAll();
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        httpSecurity.httpBasic();
        httpSecurity.csrf().disable();
        httpSecurity.formLogin().usernameParameter("username").failureHandler(bankerUserFailureHandler).successHandler(bankerUserSuccessHandler);

//        httpSecurity
//                .csrf().disable() // CSRF protection is not needed for REST APIs
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No session will be created by Spring Security
//                .and()
//                .authorizeRequests()
//                .antMatchers("/users/sign").permitAll() // Allow sign up
//                .anyRequest().authenticated() // All other requests must be authenticated
//                .and()
//                .httpBasic()
//                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // Custom entry point to handle unauthorized access
//                .and()
//                .formLogin()
//                .permitAll()
//                .failureHandler(bankerUserFailureHandler) // Custom failure handler
//                .and()
//                .logout()
//                .permitAll();

        AuthenticationManagerBuilder builder=httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(service);
        authenticationManager=builder.build();
        httpSecurity.authenticationManager(authenticationManager);

        return httpSecurity.build();
    }
}