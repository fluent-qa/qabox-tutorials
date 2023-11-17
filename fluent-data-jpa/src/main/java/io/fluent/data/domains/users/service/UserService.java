package io.fluent.data.domains.users.service;


import io.fluent.data.domains.users.model.entity.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
