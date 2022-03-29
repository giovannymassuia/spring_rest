package io.giovannymassuia.spring_rest.spring_rest.infra;

import io.giovannymassuia.spring_rest.spring_rest.core.User;
import io.giovannymassuia.spring_rest.spring_rest.core.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users")
@RestController
@RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(Map.of("data", users));
    }

    @GetMapping("me")
    @SecurityRequirement(name = "JWT Token")
    public ResponseEntity me(Principal principal) {
        String userId = SecurityUtils.getUser(principal).id().toString();
        User user = userService.findById(userId);
        return ResponseEntity.ok(Map.of("data", user));
    }

    @GetMapping("{userId}")
    @SecurityRequirement(name = "JWT Token")
    public ResponseEntity findOne(@PathVariable String userId, Principal principal) {

        if (SecurityUtils.isAdmin(principal) || SecurityUtils.isSameUser(principal, userId)) {
            User user = userService.findById(userId);
            return ResponseEntity.ok(Map.of("user", user));
        }

        return buildForbiddenResponse(HttpStatus.FORBIDDEN, "You are an admin user or requesting info about yourself.");
    }

    @PostMapping
    @SecurityRequirement(name = "JWT Token")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody UserRequest request, Principal principal) {

        if (!SecurityUtils.isAdmin(principal)) {
            return buildForbiddenResponse(HttpStatus.FORBIDDEN, "User must be admin.");
        }

        UUID userId = userService.create(request.name(), request.age());
        return ResponseEntity.created(URI.create("/users/" + userId)).build();
    }

    @DeleteMapping("{userId}")
    @SecurityRequirement(name = "JWT Token")
    public ResponseEntity delete(@PathVariable String userId, Principal principal) {

        if (!SecurityUtils.isAdmin(principal)) {
            return buildForbiddenResponse(HttpStatus.FORBIDDEN, "User must be admin.");
        }

        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFound(NotFoundException exception) {
        return buildForbiddenResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    private ResponseEntity buildForbiddenResponse(HttpStatus forbidden, String s) {
        return ResponseEntity.status(forbidden).body(Map.of("error", s));
    }

    public record UserRequest(String name, int age) {}
}
