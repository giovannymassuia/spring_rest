package io.giovannymassuia.spring_rest.spring_rest.core;

import io.giovannymassuia.spring_rest.spring_rest.infra.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public UUID create(String name, int age) {
        User user = new User(UUID.randomUUID(), name, age, null);
        userRepository.save(user);
        return user.id();
    }

    public User findById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public void delete(String userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(userId);
    }

    public List<User> findByProfile(Role profile) {
        return findAll().stream().filter(user -> profile.equals(user.role())).toList();
    }
}
