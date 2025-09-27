package com.url.shortner.repository;

import com.url.shortner.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

}
