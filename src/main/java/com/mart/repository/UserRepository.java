package com.mart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mart.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findById(long userId);

	Optional<User> findByUsername(String username);

}
