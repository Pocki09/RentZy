package com.rentzy.repository;

import com.rentzy.enums.user.UserRole;
import com.rentzy.entity.UserEntity;
import com.rentzy.enums.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    // AUTHENTICATION
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByNationalId(String nationalId);

    // EMAIL VERIFICATION
    Optional<UserEntity> findByEmailVerificationToken(String token);

    @Query("{ 'emailVerificationExpiry': { $lt: ?0 }, 'emailVerified': false }")
    List<UserEntity> findExpiredEmailVerifications(Instant now);

    // USER MANAGEMENT
    Page<UserEntity> findByStatus(UserStatus status, Pageable pageable);
    Page<UserEntity> findByRole(UserRole role, Pageable pageable);
    Page<UserEntity> findByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable);

    // Admin xem danh sách cần verify identity
    Page<UserEntity> findByIdentityVerifiedFalseAndStatusOrderByCreatedAtDesc(UserStatus status, Pageable pageable);

    // STUDENT SPECIFIC (nghiệp vụ SV HCM)
    List<UserEntity> findByUniversityAndRoleOrderByCreatedAtDesc(String university, UserRole role);

    @Query("{ 'role': 'STUDENT', 'university': ?0, 'status': 'ACTIVE' }")
    List<UserEntity> findActiveStudentsByUniversity(String university);

    // Tìm SV theo ngân sách (để suggest bạn cùng thuê)
    @Query("{ 'role': 'STUDENT', 'status': 'ACTIVE', 'maxBudget': { $gte: ?0, $lte: ?1 } }")
    List<UserEntity> findStudentsByBudgetRange(Double minBudget, Double maxBudget);

    // SEARCH & FILTER
    @Query("{ 'role': ?0, 'status': ?1, " +
            "$or: [" +
            "  { 'fullName': { $regex: ?2, $options: 'i' } }," +
            "  { 'email': { $regex: ?2, $options: 'i' } }," +
            "  { 'username': { $regex: ?2, $options: 'i' } }" +
            "]}")
    Page<UserEntity> searchUsersByRoleAndStatus(UserRole role, UserStatus status, String keyword, Pageable pageable);

    // SECURITY
    @Query("{ 'lockoutUntil': { $gt: ?0 } }")
    List<UserEntity> findLockedUsers(Instant now);

    // STATISTICS
    long countByRole(UserRole role);
    long countByStatus(UserStatus status);
    long countByRoleAndStatus(UserRole role, UserStatus status);

    @Query("{ 'createdAt': { $gte: ?0, $lt: ?1 } }")
    long countUsersCreatedBetween(Instant start, Instant end);

    // CLEANUP TASKS
    @Query("{ 'status': 'PENDING', 'emailVerified': false, 'createdAt': { $lt: ?0 } }")
    List<UserEntity> findUnverifiedUsersOlderThan(Instant cutoffDate);
}
