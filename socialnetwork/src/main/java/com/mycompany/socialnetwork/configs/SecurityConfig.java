package com.mycompany.socialnetwork.configs;

import com.mycompany.socialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.Security;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return username -> {
            var userEntity = userRepository.findByMail(username)
                    .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
            return User.builder()
                    .username(userEntity.getMail())
                    .password(userEntity.getPasswor())
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth ->
                    auth.requestMatchers(HttpMethod.GET, "/**") //Solo va a permitir los metodos GET sin autorizacion. el /** es que admita cualquier cosa en el endPoint de los GET
                            .permitAll()
                            .requestMatchers("/h2/**")
                            .permitAll()
                            .anyRequest()// para los otros metodos solicita autenticacion
                            .authenticated()
                ).httpBasic(Customizer.withDefaults());
        return  http.build();
    }
}
