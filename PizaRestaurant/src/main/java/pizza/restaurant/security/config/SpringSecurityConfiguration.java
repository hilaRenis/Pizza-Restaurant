package pizza.restaurant.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pizza.restaurant.security.entity.Role;
import pizza.restaurant.security.filter.CustomJwtAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static pizza.restaurant.security.config.AccessControlList.*;

/**
 * Spring Security Configuration
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
    /**
     * CustomJwtAuthenticationFilter instance
     */
    private final CustomJwtAuthenticationFilter customJwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Dependency injection
     *
     * @param customJwtAuthenticationFilter CustomJwtAuthenticationFilter
     * @param authenticationEntryPoint
     */
    public SpringSecurityConfiguration(CustomJwtAuthenticationFilter customJwtAuthenticationFilter, JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.customJwtAuthenticationFilter = customJwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults()).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                                .requestMatchers(COMMON_PERMISSIONS).hasAnyAuthority(Role.Customer.name(), Role.Employee.name())
                                .requestMatchers(CUSTOMER_PERMISSIONS).hasAnyAuthority(Role.Customer.name())
                                .anyRequest()
                                .hasAnyAuthority(Role.Employee.name()))
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exc -> exc.authenticationEntryPoint(authenticationEntryPoint))
                .build();
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }
}
