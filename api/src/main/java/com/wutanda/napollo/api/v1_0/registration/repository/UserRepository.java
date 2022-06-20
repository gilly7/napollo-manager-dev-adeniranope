package com.wutanda.napollo.api.v1_0.registration.repository;

import com.wutanda.napollo.api.v1_0.registration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	 User findByEmail(String email);
}