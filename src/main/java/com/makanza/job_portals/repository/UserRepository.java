package com.makanza.job_portals.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.makanza.job_portals.model.MyUser;

public interface UserRepository extends JpaRepository<MyUser, Long>{
Optional<MyUser> findByUsername(String username);
}
