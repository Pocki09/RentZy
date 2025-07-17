package com.rentzy.repository;

import com.rentzy.enums.UserRole;
import com.rentzy.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    Page<UserEntity> findAll(Pageable pageable);
    Page<UserEntity> findByRole(UserRole role, Pageable pageable);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByFullNameIgnoreCase(String fullName);
    Optional<UserEntity> findByPhone(String phone);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteByUsername(String username);
}
