package in.co.greenwave.taskapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;



//public class BasicAuthenticationSecurityConfiguration {
	
	//Filter chain
	// authenticate all requests
	//basic authentication
	//disabling csrf
	//stateless rest api
	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		//1: Response to preflight request doesn't pass access control check
//		//2: basic auth
//		return
//				http
//					.authorizeHttpRequests(
//						auth ->
//							auth
//							.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//							.anyRequest().authenticated()
//						)
//					.httpBasic(Customizer.withDefaults())
//					.sessionManagement(
//						session -> session.sessionCreationPolicy
//						(SessionCreationPolicy.STATELESS))
//						// .csrf().disable() Deprecated in SB 3.1.x
//					.csrf(csrf -> csrf.disable()) // Starting from SB 3.1.x using Lambda DSL
//			     // .csrf(AbstractHttpConfigurer::disable) // Starting from SB 3.1.x using Method Reference
//					.build();
//	}
// 
//}