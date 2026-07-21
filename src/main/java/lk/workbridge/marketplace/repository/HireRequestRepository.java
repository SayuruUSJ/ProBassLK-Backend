package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.HireRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface HireRequestRepository extends JpaRepository<HireRequest,String> {
    boolean existsByWorkerIdAndRequestedDate(String workerId, LocalDate requestedDate);
    List<HireRequest> findByWorkerId(String workerId);
    @Query("""
    SELECT COUNT(c)
    FROM HireRequest c
    WHERE c.worker.id = :workerId
      AND c.status = :status
""")
    Long countCompletedBookings(
            @Param("workerId") String workerId,
            @Param("status") String status);

    @Query("""
    SELECT COUNT(c)
    FROM HireRequest c
    WHERE c.worker.id = :workerId
      AND c.status = :status
""")
    Long countHireRequests(
            @Param("workerId") String workerId,
            @Param("status") String status);

    List<HireRequest> findByClientId(String clientId);

    @Query("""
        SELECT c
        FROM HireRequest c
        WHERE c.worker.id = :workerId
         AND c.status IN (:statuses)
    """)
    List<HireRequest> findByWorkerIdAndStatus(@Param("workerId") String workerId
            ,@Param("statuses") List<String> statuses);

    @Query("""
        SELECT c
        FROM HireRequest c
        WHERE c.client.id = :clientId
          AND c.status = :status
        ORDER BY c.createdAt DESC
    """)
    List<HireRequest> findByClientIdAndStatus(
            @Param("clientId") String clientId,
            @Param("status") String status
    );



    @Query("SELECT COUNT(h) FROM HireRequest h WHERE h.client.id = :clientId AND h.status=:status")
    long countByClientId(@Param("clientId") String clientId,
      @Param("status") String status
    );
}
