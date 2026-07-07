package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceProviderAdvertisementRepository extends JpaRepository<ServiceProviderAdvertisement,String> {
 @Query("""
        SELECT spa
        FROM ServiceProviderAdvertisement spa
        JOIN FETCH spa.worker
        WHERE spa.serviceId = :serviceId
    """)
    Optional<ServiceProviderAdvertisement> findByIdWithWorker(String serviceId);
}

