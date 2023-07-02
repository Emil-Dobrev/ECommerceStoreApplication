package emildobrev.ecommerce.store.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static emildobrev.ecommerce.store.constants.Constants.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests((authz) ->
                    authz
                            .requestMatchers("/api/v1/auth/**", API_V_1_PRODUCTS ).permitAll()
                            .requestMatchers(HttpMethod.PATCH  , API_V_1_PRODUCTS , "http://localhost:8080/api/v1/products/vote").hasAnyAuthority(ADMIN, USER)
                            .requestMatchers(HttpMethod.POST  , API_V_1_PRODUCTS, "/api/v1/coupons").hasAnyAuthority(ADMIN)
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/*").hasAnyAuthority(ADMIN)
                            .requestMatchers(HttpMethod.POST, "/api/v1/order").hasAnyAuthority(USER)
                            .anyRequest().authenticated()

                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
