


package in.co.greenwave.UserGroup.utility;

//This class helps to check if a user's token (like a special key) is valid when they try to access a part of our application.
import jakarta.servlet.FilterChain; // This helps in processing requests
import jakarta.servlet.ServletException; // This handles exceptions related to servlets
import jakarta.servlet.http.HttpServletRequest; // This represents a request from the user
import jakarta.servlet.http.HttpServletResponse; // This represents a response back to the user
import org.springframework.beans.factory.annotation.Autowired; // This helps to automatically connect the class with other classes
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // This is used for checking user authentication
import org.springframework.security.core.context.SecurityContextHolder; // This keeps track of the user's authentication status
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // This helps to build details for the authentication
import org.springframework.stereotype.Component; // This marks the class as a component in the Spring framework
import org.springframework.web.filter.OncePerRequestFilter; // This is a special type of filter that processes each request only once

import java.io.IOException; // This handles input-output exceptions

@Component // This tells Spring that we want to use this class as a component
public class JwtAuthFilter extends OncePerRequestFilter { // We create a new filter by extending the OncePerRequestFilter

	@Autowired // This connects our JwtService automatically
	private JwtService jwtService; // This is a service that helps us work with tokens

	@Override // This means we are overriding a method from the parent class
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization"); // We check for the "Authorization" header in the request
		String token = null; // This will hold our token
		String username = null; // This will hold the username
		if (authHeader != null && authHeader.startsWith("Bearer ")) { // If we found an Authorization header and it starts with "Bearer " (like a special label for our key)
			token = authHeader.substring(7); // We get the actual token by removing "Bearer " from the start
			username = jwtService.extractUsername(token); // We use our service to get the username from the token
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // If we found a username and the user is not already authenticated
			if (jwtService.validateToken(token)) { // If the token is valid
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, null); // We create a new authentication token with the username
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // We set the details for the authentication token
				SecurityContextHolder.getContext().setAuthentication(authToken); // We save the authentication token in the security context
			} else { // If the token is not valid
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // We send back a 401 Unauthorized status
				response.getWriter().write("Token expired or invalid"); // We write a message to explain the problem
				return; // We stop here and don't continue to the next filter
			}
		}
		filterChain.doFilter(request, response); // Finally, we pass the request and response to the next filter in the chain
	}
}

