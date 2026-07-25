package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);


    Optional<User> findByEmail(String email);


    boolean existsByUsername(String username);


    boolean existsByEmail(String email);

    @Query(value = "SELECT CASE WHEN verification_status = 1 THEN true ELSE false END " +
            "FROM users WHERE username = :username",
            nativeQuery = true)
    Integer isUserVerified(@Param("username") String username);

    @Query("SELECT u FROM User u")
    Page<User> findAllUsersWithBasicInfo(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdJPQL(@Param("id") String id);
}
