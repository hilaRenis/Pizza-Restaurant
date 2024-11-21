package pizza.restaurant.security.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pizza.restaurant.security.entity.Role;

import java.util.*;

@Service
@Slf4j
public class JwtUtil {
    /**
     * secret
     */
    private String secret;

    /**
     * jwtExpirationInMs
     */
    private int jwtExpirationInMs;

    /**
     * refreshExpirationDateInMs
     */
    private int refreshExpirationDateInMs;

    /**
     * secret setter
     *
     * @param secret
     */
    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * jwtExpirationInMs setter
     *
     * @param jwtExpirationInMs
     */
    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    /**
     * refreshExpirationDateInMs setter
     *
     * @param refreshExpirationDateInMs
     */
    @Value("${jwt.refreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    /**
     * This method takes user details and generates a token
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        if (roles.contains(new SimpleGrantedAuthority(Role.Employee.name()))) {
            claims.put("isEmployee", true);
        }
        if (roles.contains(new SimpleGrantedAuthority(Role.Customer.name()))) {
            claims.put("isCustomer", true);
        }

        return doGenerateToken(claims, userDetails.getUsername());
    }


    /**
     * This method generates a token
     *
     * @param claims
     * @param subject
     * @return
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * This method generates a refresh token
     *
     * @param claims
     * @param subject
     * @return
     */
    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    /**
     * getMapFromIoJsonwebtokenClaims method
     *
     * @param claims
     * @return
     */
    public Map<String, Object> getMapFromIoJsonwebtokenClaims(Claims claims) {
        return new HashMap<>(claims);
    }

    /**
     * This method validates a token
     *
     * @param authToken
     * @return
     */
    public boolean isValidToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    /**
     * getUsernameFromToken method
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

    /**
     * getRolesFromToken method
     *
     * @param token
     * @return
     */
    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        List<SimpleGrantedAuthority> roles = null;

        Boolean isEmployee = claims.get("isEmployee", Boolean.class);
        Boolean isCustomer = claims.get("isCustomer", Boolean.class);

        if (Boolean.TRUE.equals(isEmployee)) {
            roles = List.of(new SimpleGrantedAuthority(Role.Employee.name()));
        }

        if (Boolean.TRUE.equals(isCustomer)) {
            roles = List.of(new SimpleGrantedAuthority(Role.Customer.name()));
        }
        return roles;

    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

}
