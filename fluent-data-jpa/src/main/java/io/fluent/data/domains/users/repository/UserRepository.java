package io.fluent.data.domains.users.repository;

import io.fluent.data.domains.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
