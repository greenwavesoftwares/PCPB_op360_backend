//package in.co.greenwave.assetapi.utility;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Component
//public class JwtService {
//
//    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    public Boolean validateToken(String token) {
//        return !isTokenExpired(token);
//    }
//
//    public String generateToken(String userName) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, userName);
//    }
//
//    private String createToken(Map<String, Object> claims, String userName) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(userName)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Key getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String extendTokenExpiration(String token) {
//        final String username = extractUsername(token);
//        if (isTokenExpired(token)) {
//            return "Token has already expired";
//        }
//        Date newExpirationDate = new Date(System.currentTimeMillis() + (1000 * 60 * 30));
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", username);
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(newExpirationDate)
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//}


package in.co.greenwave.assetapi.utility;

// This class helps us create and manage special keys called tokens, which are used to confirm who you are in the application.
import io.jsonwebtoken.Claims; // This helps us work with the information inside the token
import io.jsonwebtoken.Jwts; // This is the main library for working with tokens
import io.jsonwebtoken.SignatureAlgorithm; // This tells us what method to use for securing our token
import io.jsonwebtoken.io.Decoders; // This helps us decode the secret key
import io.jsonwebtoken.security.Keys; // This helps us create a secure key
import org.springframework.stereotype.Component; // This marks the class as a component in the Spring framework

import java.security.Key; // This represents our secure key
import java.util.Date; // This helps us work with dates and times
import java.util.HashMap; // This is a special type of map for storing key-value pairs
import java.util.Map; // This is a general map interface
import java.util.function.Function; // This is used for functions that take an input and return an output

@Component // This tells Spring that we want to use this class as a component
public class JwtService {

    // This is our secret key. It's like a password used to create and check our tokens.
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // This method gets the username from the token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // We use extractClaim to get the subject (username) from the token.
    }

    // This method gets the expiration date from the token.
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // We use extractClaim to get the expiration date from the token.
    }

    // This method extracts specific information (claims) from the token.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // First, we get all the claims from the token.
        return claimsResolver.apply(claims); // Then, we apply the claimsResolver function to get the specific claim we want.
    }

    // This method extracts all claims from the token.
    private Claims extractAllClaims(String token) {
        return Jwts // We use the Jwts library to parse the token
                .parserBuilder() // We start building our parser
                .setSigningKey(getSignKey()) // We set our signing key (the secret key)
                .build() // We build the parser
                .parseClaimsJws(token) // We parse the token to get the claims
                .getBody(); // We get the body of claims
    }

    // This method checks if the token has expired.
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // We check if the expiration date is before now.
    }

    // This method checks if the token is still valid.
    public Boolean validateToken(String token) {
        return !isTokenExpired(token); // We return true if the token is not expired.
    }

    // This method generates a new token for a user.
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>(); // We create a new map for claims
        return createToken(claims, userName); // We create a token with the claims and username
    }

    // This method creates a token with the given claims and username.
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder() // We start building our token
                .setClaims(claims) // We set the claims in the token
                .setSubject(userName) // We set the subject (username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // We set the issue date (now)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // We set the expiration date (30 minutes from now)
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // We sign the token with our secret key using HS256 algorithm
                .compact(); // We finalize the token
    }

    // This method gets our signing key from the secret.
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET); // We decode the secret key from Base64
        return Keys.hmacShaKeyFor(keyBytes); // We create a secure HMAC key using the decoded bytes
    }

    // This method extends the expiration time of a token.
    public String extendTokenExpiration(String token) {
        final String username = extractUsername(token); // We get the username from the token
        if (isTokenExpired(token)) { // If the token is expired
            return "Token has already expired"; // We return a message saying the token is expired
        }
        Date newExpirationDate = new Date(System.currentTimeMillis() + (1000 * 60 * 30)); // We set a new expiration date (30 minutes from now)
        Map<String, Object> claims = new HashMap<>(); // We create a new map for claims
        claims.put("username", username); // We add the username to the claims
        return Jwts.builder() // We start building a new token
                .setClaims(claims) // We set the claims in the new token
                .setSubject(username) // We set the subject (username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // We set the issue date (now)
                .setExpiration(newExpirationDate) // We set the new expiration date
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // We sign the new token
                .compact(); // We finalize the new token
    }
}
