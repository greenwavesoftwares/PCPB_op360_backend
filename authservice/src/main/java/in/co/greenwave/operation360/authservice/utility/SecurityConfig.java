//package in.co.greenwave.assetapi.utility;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Autowired
package in.co.greenwave.operation360.authservice.utility;

import java.util.Arrays; // This helps work with arrays
import java.util.Collections; // This helps work with collections (like lists)

// This class sets up security for our application, making sure that only authorized users can access certain parts.
import org.springframework.beans.factory.annotation.Autowired; // This helps us get other parts of the application
import org.springframework.context.annotation.Bean; // This helps us create objects that can be used in our application
import org.springframework.context.annotation.Configuration; // This tells Spring that this class is a configuration class
import org.springframework.security.authentication.AuthenticationManager; // This is used for managing user authentication
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // This helps with authentication configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // This allows method-level security
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // This helps configure security for web requests
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // This enables web security
import org.springframework.security.config.http.SessionCreationPolicy; // This helps with session management
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // This is used to securely encode passwords
import org.springframework.security.crypto.password.PasswordEncoder; // This is a general interface for encoding passwords
import org.springframework.security.web.SecurityFilterChain; // This helps create a chain of security filters
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // This is used for username and password authentication
import org.springframework.web.cors.CorsConfiguration; // This helps configure Cross-Origin Resource Sharing (CORS)
import org.springframework.web.cors.CorsConfigurationSource; // This helps provide CORS configuration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // This helps map CORS configurations to specific URLs

// This class configures the security settings for our application.
@Configuration
@EnableWebSecurity // This enables web security in our application
@EnableMethodSecurity // This enables method-level security
public class SecurityConfig {

    // This allows us to use the JwtAuthFilter for checking tokens
    @Autowired
    private JwtAuthFilter authFilter;

    // This method sets up the security filter chain for handling web requests
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // We set the session management to be stateless
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // We define which requests are allowed without authentication
                .authorizeHttpRequests(requests -> requests
                        // These paths can be accessed without logging in
                        .requestMatchers("/auth/extendTokenExpiration", "/auth/signin", "/auth/admin/signup", "/auth/user/signup","/auth/register-new-tenant", "/auth/userWiseTenant",
                                "/v2/api-docs", // Documentation path for API version 2
                                "/swagger-resources", // Resources for Swagger UI
                                "/swagger-resources/**", // All resources under this path
                                "/configuration/ui", // UI configuration for Swagger
                                "/configuration/security", // Security configuration for Swagger
                                "/swagger-ui.html", // Main page for Swagger UI
                                "/webjars/**", // Resources for web jars (libraries)
                                "/v3/api-docs/**", // All documentation for API version 3
                                "/swagger-ui/**" // All resources under Swagger UI
                        ).permitAll() // Permit access to the above paths without authentication
                        .anyRequest().authenticated()) // All other requests need authentication
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // We add our custom filter before the default authentication filter
                .csrf(csrf -> csrf.disable()) // We disable CSRF protection 
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // We configure CORS to allow requests from different origins
                .build(); // We build the security filter chain
    }

    // This method sets up the CORS configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); // Create a new CORS configuration
        configuration.setAllowedOrigins(Arrays.asList("*")); // Allow requests from any origin
        configuration.setAllowedMethods(Collections.singletonList("*")); // Allow any HTTP method (GET, POST, etc.)
        configuration.setAllowedHeaders(Collections.singletonList("*")); // Allow any header in requests
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // Allow the Authorization header to be exposed
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Create a source for CORS configurations
        source.registerCorsConfiguration("/**", configuration); // Register the configuration for all paths
        return source; // Return the CORS configuration source
    }

    // This method creates a password encoder to securely encode passwords
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // We use BCrypt to encode passwords
    }

    // This method creates an AuthenticationManager to handle user authentication
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // We get and return the authentication manager
    }
}
