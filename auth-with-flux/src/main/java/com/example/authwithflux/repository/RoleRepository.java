package com.example.authwithflux.repository;

import com.example.authwithflux.domain.model.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RoleRepository extends ReactiveCrudRepository<Role, Integer> {
}
