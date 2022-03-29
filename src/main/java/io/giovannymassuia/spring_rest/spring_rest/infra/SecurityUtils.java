package io.giovannymassuia.spring_rest.spring_rest.infra;

import io.giovannymassuia.spring_rest.spring_rest.core.User;
import java.security.Principal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class SecurityUtils {

    private static final String ADMIN_ROLE = "admin";

    public static boolean isSameUser(Principal principal, String userId) {
        User user = getUser(principal);
        return userId.equals(user.id().toString());
    }

    public static boolean isAdmin(Principal principal) {
        User user = getUser(principal);
        return user.role() != null && user.role().name().equals(ADMIN_ROLE);
    }

    public static User getUser(Principal principal) {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

}
