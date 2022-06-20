package com.wutanda.napollo.api.v1_0.registration.repository;

import com.wutanda.napollo.api.v1_0.registration.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer>{
	Role findByRole(String role);

}