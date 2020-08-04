package com.cos.securityex01.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.securityex01.model.User;

// JpaRepository를 상속하면 자동으로 컴포넌트 스캔이 된다. 
public interface UserRepository extends JpaRepository<User, Integer>{ // 기본 CRUD 만들어져있음!
	
	// * Jpa Naming 전략 : 자동으로 쿼리 생성해줌
	// 1. SELECT * FROM user WHERE username = ?1 쿼리 동작 
	User findByUsername(String username);
	// 2. SELECT * FROM user WHERE username = 1? AND password = 2? 쿼리 동작 
	// User findByUsernameAndPassword(String username, String password);
	
    // @Query(value = "SELECT * FROM user", nativeQuery = true)
    // User find마음대로();
	
//	@Query(value="select * from user where email=1?",nativeQuery = true)
	// SELECT * FROM user WHERE email = ?1
	Optional<User> findByEmail(String email);
}
