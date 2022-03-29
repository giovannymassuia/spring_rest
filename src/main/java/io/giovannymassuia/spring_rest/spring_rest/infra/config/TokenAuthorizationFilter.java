package io.giovannymassuia.spring_rest.spring_rest.infra.config;

import io.giovannymassuia.spring_rest.spring_rest.core.User;
import io.giovannymassuia.spring_rest.spring_rest.core.UserService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private final UserService userService;

    public TokenAuthorizationFilter(UserService userService) {this.userService = userService;}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        try {
            String token = resolveToken(request);

            // Using the token as the user's name for testing purposes
            Optional<User> userOptional = userService.findByName(token);

            userOptional.ifPresentOrElse(user -> {
                Authentication auth = new UsernamePasswordAuthenticationToken(user, "token", null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }, SecurityContextHolder::clearContext);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write(e.getMessage());
        }
    }

    public String resolveToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}