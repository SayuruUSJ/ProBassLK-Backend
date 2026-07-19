package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ApplicationsForWantedAdvertisements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationForWantedADRepository extends JpaRepository<ApplicationsForWantedAdvertisements, Integer> {
//    boolean existsByAdvertisementAdvertisementIdAndWorkerWorkerId(
//            String advertisementId,
//            String workerId);

    @Query(value = """
    SELECT COUNT(*)
    FROM service_providers_requests_for_wanted_advertisements
    WHERE advertisement_id = :advertisementId
      AND worker_id = :workerId
    """, nativeQuery = true)
Long countRequest(@Param("advertisementId") String advertisementId,
                  @Param("workerId") String workerId);

    @Query(value = """
    SELECT COUNT(*)
    FROM service_providers_requests_for_wanted_advertisements
    WHERE advertisement_id = :advertisementId
     
    """, nativeQuery = true)
    Long countApplicantsRequests(@Param("advertisementId") String advertisementId
                      );

    @Query("""
        SELECT a 
        FROM ApplicationsForWantedAdvertisements a 
        WHERE a.advertisement.client.id = :clientId
    """)
    Page<ApplicationsForWantedAdvertisements> findByClientId(
            @Param("clientId") String clientId,
            Pageable pageable
    );
}
