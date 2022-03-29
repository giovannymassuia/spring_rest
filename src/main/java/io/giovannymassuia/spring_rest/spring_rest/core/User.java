package io.giovannymassuia.spring_rest.spring_rest.core;

import java.util.UUID;

public record User(UUID id, String name, int age, Role role) {}
