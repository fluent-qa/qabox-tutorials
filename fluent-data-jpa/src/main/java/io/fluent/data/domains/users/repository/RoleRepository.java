package io.fluent.data.domains.users.repository;

import io.fluent.data.domains.users.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long>{
}
