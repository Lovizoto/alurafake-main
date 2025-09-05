package br.com.alura.AluraFake.security.config;


import br.com.alura.AluraFake.security.filter.JwtAuthFilter;
import br.com.alura.AluraFake.security.service.JwtTokenService;
import br.com.alura.AluraFake.security.service.UserDetailServiceImpl;
import br.com.alura.AluraFake.user.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//Padrão de Projeto de outros projetos meus
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailServiceImpl userDetailService;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(UserDetailServiceImpl userDetailService, JwtAuthFilter jwtAuthFilter) {
        this.userDetailService = userDetailService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(this::configureAuthorization)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
                .requestMatchers(Endpoint.AUTH.getPath()).permitAll()
                .requestMatchers(HttpMethod.GET, Endpoint.USER_ALL.getPath(), Endpoint.COURSE_ALL.getPath()).authenticated()
                .requestMatchers(Endpoint.TASK.getPath()).hasRole(Role.INSTRUCTOR.name())
                .requestMatchers(HttpMethod.POST, Endpoint.COURSE_NEW.getPath(), "/course/{id}/publish").hasRole(Role.INSTRUCTOR.name())
                .requestMatchers(Endpoint.INSTRUCTOR.getPath()).hasRole(Role.INSTRUCTOR.name())
                .anyRequest().authenticated();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
