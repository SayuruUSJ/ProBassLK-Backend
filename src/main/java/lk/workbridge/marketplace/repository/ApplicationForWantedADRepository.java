package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ApplicationsForWantedAdvertisements;
import lk.workbridge.marketplace.entity.HireRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    @Query(value = """
            SELECT COUNT(*)
            FROM service_providers_requests_for_wanted_advertisements
            WHERE worker_id = :workerId
                        AND status=:status
            
            """, nativeQuery = true)
    Long countApplicantsRequestsServiceProvider(@Param("workerId") String workerId
            ,@Param("status") String status
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

    @Query(value = """
            SELECT * 
            FROM service_providers_requests_for_wanted_advertisements sp
           
            WHERE sp.worker_id = :workerId
            """,
            nativeQuery = true)
    Page<ApplicationsForWantedAdvertisements> findByWorkerId(
            @Param("workerId") String workerId,
            Pageable pageable
    );
    @Query(value = """
    SELECT * 
    FROM service_providers_requests_for_wanted_advertisements sp
    WHERE sp.worker_id = :workerId
    AND sp.status IN (:statuses)
    """,
            nativeQuery = true)
    List<ApplicationsForWantedAdvertisements> findByWorkerIdAndStatuses(
            @Param("workerId") String workerId,
            @Param("statuses") List<String> statuses
    );


    @Query("""
                SELECT c
                FROM ApplicationsForWantedAdvertisements c
                WHERE c.advertisement.client.id= :clientId
                  AND c.status = :status
                ORDER BY c.createdAt DESC
            """)
    List<ApplicationsForWantedAdvertisements> findByClientIdAndStatus(
            @Param("clientId") String clientId,
            @Param("status") String status
    );

    @Query("SELECT COUNT(a) FROM ApplicationsForWantedAdvertisements a WHERE a.advertisement.client.id = :clientId AND a.status = :status")
    long countByClientIdAndStatus(@Param("clientId") String clientId,
                                  @Param("status") String status);

}
