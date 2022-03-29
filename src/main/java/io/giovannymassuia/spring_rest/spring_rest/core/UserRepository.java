package io.giovannymassuia.spring_rest.spring_rest.core;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    void save(User user);

    Optional<User> findById(String userId);

    boolean delete(String userId);

    Optional<User> findByName(String name);
}
