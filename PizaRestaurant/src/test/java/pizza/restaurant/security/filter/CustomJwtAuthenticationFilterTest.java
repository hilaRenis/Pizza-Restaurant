package pizza.restaurant.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pizza.restaurant.security.util.JwtUtil;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomJwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtTokenUtil;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

    @Test
    public void testDoFilterInternalWithValidJwtToken() throws Exception {
        // Setup
        String jwtToken = "validJwtToken";
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtTokenUtil.isValidToken(jwtToken)).thenReturn(true);
        when(jwtTokenUtil.getUsernameFromToken(jwtToken)).thenReturn("username");
        when(jwtTokenUtil.getRolesFromToken(jwtToken)).thenReturn(Collections.emptyList());

        // Execute
        customJwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Verify
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternalWithExpiredJwtToken() throws Exception {
        // Setup
        String jwtToken = "expiredJwtToken";
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtTokenUtil.isValidToken(jwtToken)).thenThrow(new ExpiredJwtException(null, null, null));

        // Execute
        customJwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Verify
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);

        // Check the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertNull(authentication.getPrincipal());
        assertNull(authentication.getCredentials());
    }


    @Test
    public void testDoFilterInternalWithoutJwtToken() throws Exception {
        // Setup
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        // Execute
        customJwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Verify
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
        // Check the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertNull(authentication.getPrincipal());
        assertNull(authentication.getCredentials());    }


}
