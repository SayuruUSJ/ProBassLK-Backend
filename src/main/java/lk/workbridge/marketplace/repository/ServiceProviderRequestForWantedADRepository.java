package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ServiceProvidersRequestsForWantedAdvertisements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceProviderRequestForWantedADRepository extends JpaRepository<ServiceProvidersRequestsForWantedAdvertisements, Integer> {
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
}
