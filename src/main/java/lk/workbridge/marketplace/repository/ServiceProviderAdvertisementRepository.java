package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ServiceProviderAdvertisementRepository extends JpaRepository<ServiceProviderAdvertisement, String> {
    @Query("""
                SELECT spa
                FROM ServiceProviderAdvertisement spa
                JOIN FETCH spa.worker
                WHERE spa.serviceId = :serviceId
            """)
    Optional<ServiceProviderAdvertisement> findByIdWithWorker(String serviceId);

    @Query("""
                SELECT s
                FROM ServiceProviderAdvertisement s
                WHERE s.status = "PUBLISHED"
            """)
    Page<ServiceProviderAdvertisement> findAllVerifiedAdvertisements(Pageable pageable);

    @Query("""
                SELECT s
                FROM ServiceProviderAdvertisement s
                
            """)
    Page<ServiceProviderAdvertisement> findAllAdvertisements(Pageable pageable);

    @Query("""
                SELECT s
                FROM ServiceProviderAdvertisement s
                WHERE s.status = "PENDING"
            """)
    Page<ServiceProviderAdvertisement> findAllPendingAdvertisements(Pageable pageable);

    @Query("""
                SELECT s
                FROM ServiceProviderAdvertisement s
                WHERE s.worker.id = :workerId
            """)
    Optional<ServiceProviderAdvertisement> findAdvertisementByWorker(
            @Param("workerId") String workerId
    );
}

