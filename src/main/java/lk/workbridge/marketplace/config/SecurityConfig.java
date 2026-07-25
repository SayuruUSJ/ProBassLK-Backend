package lk.workbridge.marketplace.config;

import lk.workbridge.marketplace.service.Impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    private final AuthServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(@Lazy AuthServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

                .csrf(csrf -> csrf.disable())


                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                        .expiredUrl("/api/auth/expired")
                        .sessionRegistry(sessionRegistry())
                )

                .sessionManagement(session -> session
                        .invalidSessionUrl("/api/auth/invalid-session")
                )


                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/login-json").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/session-info").permitAll()
                        .requestMatchers("/api/auth/verify-code").permitAll()
                        .requestMatchers("/api/auth/send-verification").permitAll()
                        .requestMatchers("/api/auth/expired").permitAll()
                        .requestMatchers("/api/auth/invalid-session").permitAll()
                        .requestMatchers("/api/auth/login-failed").permitAll()
                        .requestMatchers("/api/auth/logout-success").permitAll()
                        .requestMatchers("/api/auth/send-otp").permitAll()
                        .requestMatchers("/api/auth/verify-otp").permitAll()
                        .requestMatchers("/api/auth/reset-passsowrd").permitAll()
                        .requestMatchers("/api/service-wanted-advertisements/get-all-wanted-ads").permitAll()
                        .requestMatchers("/api/service-provider-advertisements/get-all-ads").permitAll()

                        .requestMatchers("/error").permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/worker/**").hasRole("WORKER")
                        .requestMatchers("/api/employer/**").hasRole("EMPLOYER")

                        .anyRequest().authenticated()
                )


                .formLogin(form -> form
                        .disable()
                )


                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/api/auth/logout-success")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );


        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            var authorities = authentication.getAuthorities();

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                System.out.println("Admin logged in successfully");

            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_WORKER"))) {

                System.out.println("Worker logged in successfully");
            } else {

                System.out.println("Employer logged in successfully");
            }
        };
    }
}