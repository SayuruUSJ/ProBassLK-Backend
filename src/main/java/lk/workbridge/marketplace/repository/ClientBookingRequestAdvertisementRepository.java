package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ClientBookingRequestedAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ClientBookingRequestAdvertisementRepository extends JpaRepository<ClientBookingRequestedAdvertisement,String> {
    boolean existsByWorkerIdAndRequestedDate(String workerId, LocalDate requestedDate);
    List<ClientBookingRequestedAdvertisement> findByWorkerId(String workerId);
    @Query("""
    SELECT COUNT(c)
    FROM ClientBookingRequestedAdvertisement c
    WHERE c.worker.id = :workerId
      AND c.status = :status
""")
    Long countCompletedBookings(
            @Param("workerId") String workerId,
            @Param("status") String status);

    List<ClientBookingRequestedAdvertisement> findByClientId(String clientId);
}
