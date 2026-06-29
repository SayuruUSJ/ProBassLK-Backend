package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    // ✅ CORRECT - matches 'username' field in User entity
    Optional<User> findByUsername(String username);

    // ✅ CORRECT - matches 'email' field
    Optional<User> findByEmail(String email);

    // ✅ CORRECT - checks existence by username
    boolean existsByUsername(String username);

    // ✅ CORRECT - checks existence by email
    boolean existsByEmail(String email);
     @Query(value = "SELECT CASE WHEN verification_status = 1 THEN true ELSE false END " +
           "FROM users WHERE username = :username", 
           nativeQuery = true)
    Integer isUserVerified(@Param("username") String username);

}
