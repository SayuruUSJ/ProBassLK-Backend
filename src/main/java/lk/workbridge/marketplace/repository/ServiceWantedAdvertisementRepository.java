package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceWantedAdvertisementRepository extends JpaRepository<ServiceWantedAdvertisement, String> {
    Page<ServiceWantedAdvertisement> findAll(Pageable pageable);

    @Query("""
                SELECT s
                FROM ServiceWantedAdvertisement s
                WHERE s.status = "PENDING"
            """)
    Page<ServiceWantedAdvertisement> findAllPendingAdvertisements(Pageable pageable);

    @Query("""
                SELECT s
                FROM ServiceWantedAdvertisement s
                WHERE s.status = "PUBLISHED"
            """)
    Page<ServiceWantedAdvertisement> findAllVerifiedAdvertisements(Pageable pageable);

    @Query("""
                SELECT s
                FROM ServiceWantedAdvertisement s
               
            """)
    Page<ServiceWantedAdvertisement> findAllAdvertisements(Pageable pageable);


    @Query("""
                SELECT s
                FROM ServiceWantedAdvertisement s
                WHERE s.client.id = :clientId
            """)
    Page<ServiceWantedAdvertisement> findAllAdvertisementByClientId(@Param("clientId") String clientId,
                                                                    Pageable pageable);

}
