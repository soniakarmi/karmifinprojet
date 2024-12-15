package com.example.Backend.Config.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                })
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/register");
                    request.requestMatchers("/login").permitAll();
                    request.requestMatchers("/verify").permitAll();
                    request.requestMatchers("/mot-de-passe-oublier").permitAll();
                    request.requestMatchers("/réinitialiser-mot-de-passe").permitAll();
                    request.requestMatchers("/roles").permitAll();
                    request.requestMatchers("/downloads/{fileName:.+}").permitAll();
                    request.requestMatchers("/cours/**").permitAll();
                    request.requestMatchers("/support-cours/TypeQuiz").permitAll();
                    request.requestMatchers("/support-cours/creat").permitAll();
                    request.requestMatchers("/classe/**").permitAll();
                    request.requestMatchers("/**").permitAll();

                    request.requestMatchers("/all/**").permitAll();

                    request.requestMatchers("/quiz-response/**").permitAll();
                    request.requestMatchers("/api/quizForms/create").permitAll();
                    request.requestMatchers("/delete/{id}/**").permitAll();
                    request.requestMatchers("/enseignants/**").permitAll();
                    request.requestMatchers("/reclamation/**").hasAnyAuthority("ADMIN","ETUDIANT");
                 //   request.requestMatchers("/etudiant/**","/classe/**","/cours/**","/enseignants").hasAnyAuthority("ADMIN","ETUDIANT","ENSEIGNANT");
                  //  request.requestMatchers("/support-cours/**","/quiz-question/**","/quiz-proposition/**").hasAnyAuthority("ENSEIGNANT");
                    request.requestMatchers("/submit/{quizFormId}").hasAnyAuthority("ETUDIANT");
                    request.requestMatchers("/note/**").hasAnyAuthority("ADMIN","ETUDIANT","ENSEIGNANT");
                    request.requestMatchers("/notification/**").hasAnyAuthority("ADMIN","ENSEIGNANT");
                    request.requestMatchers("/paiements/**","/parent-etudiant/{code}").hasAnyAuthority("ETUDIANT","PARENT","ADMIN");





                }).sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200/","http://localhost:5173/")); // You can set specific origins instead of "*" for security
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
