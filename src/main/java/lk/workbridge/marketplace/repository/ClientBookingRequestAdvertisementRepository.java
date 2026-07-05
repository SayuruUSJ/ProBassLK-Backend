package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ClientBookingRequestedAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClientBookingRequestAdvertisementRepository extends JpaRepository<ClientBookingRequestedAdvertisement,String> {
    boolean existsByWorkerIdAndRequestedDate(String workerId, LocalDate requestedDate);
    List<ClientBookingRequestedAdvertisement> findByWorkerId(String workerId);
}
