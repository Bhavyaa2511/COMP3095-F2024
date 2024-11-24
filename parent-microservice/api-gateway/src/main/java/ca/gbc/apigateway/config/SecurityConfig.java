package ca.gbc.apigateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final String[] noauthResourceuris = {
            "/swagger-ui",
            "swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resource/**",
            "/api-docs/**",
            "/aggregate/**",

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {



        log.info("security filter chain ...");

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)               //disable CSRF protection
                //  .authorizeHttpRequests(authorize -> authorize
                //                        .anyRequest().permitAll()) //all requests temporarily permitted
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(noauthResourceuris)
                        .permitAll()
                        .anyRequest().authenticated())                //all requests require authentication
                .oauth2ResourceServer(ouath2 -> ouath2
                        .jwt(Customizer.withDefaults()))
                .build();


    }


}
