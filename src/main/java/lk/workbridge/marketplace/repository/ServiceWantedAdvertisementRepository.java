package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ServiceProviderAdvertisement;
import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceWantedAdvertisementRepository extends JpaRepository<ServiceWantedAdvertisement,String> {
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
    WHERE s.status = "VERIFIED"
""")
    Page<ServiceWantedAdvertisement> findAllVerifiedAdvertisements(Pageable pageable);
}
