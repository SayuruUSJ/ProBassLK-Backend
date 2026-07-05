package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.ServiceWantedAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ServiceWantedAdvertisementRepository extends JpaRepository<ServiceWantedAdvertisement,String> {


}
