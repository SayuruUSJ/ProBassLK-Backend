package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
