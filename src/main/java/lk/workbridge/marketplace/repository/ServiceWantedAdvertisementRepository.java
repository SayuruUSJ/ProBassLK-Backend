package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceWantedAdvertisementRepository extends JpaRepository<ServiceWantedAdvertisement,String> {
    Page<ServiceWantedAdvertisement> findAll(Pageable pageable);

}
