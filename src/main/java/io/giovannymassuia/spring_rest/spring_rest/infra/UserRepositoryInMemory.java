package io.giovannymassuia.spring_rest.spring_rest.infra;

import io.giovannymassuia.spring_rest.spring_rest.core.Role;
import io.giovannymassuia.spring_rest.spring_rest.core.User;
import io.giovannymassuia.spring_rest.spring_rest.core.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryInMemory implements UserRepository {

    private final List<User> users = new ArrayList<>();

    {
        users.add(new User(UUID.fromString("3c4b5254-27bb-4709-b529-6c076f8c1e50"), "admin", 99, new Role("admin")));
        users.add(new User(UUID.fromString("bd750490-c8d6-4629-8bf9-3e70883bf154"), "user", 10, new Role("user")));
        users.add(new User(UUID.fromString("bd750490-c8d6-4629-8bf9-3e70883bf151"), "user_support", 25, new Role("support")));
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public Optional<User> findById(String userId) {
        return users.stream()
                .filter(user -> user.id().toString().equals(userId))
                .findFirst();
    }

    @Override
    public boolean delete(String userId) {
        return users.removeIf(user -> user.id().toString().equals(userId));
    }

    @Override
    public Optional<User> findByName(String name) {
        return users.stream()
                .filter(user -> user.name().equals(name))
                .findFirst();
    }
}
