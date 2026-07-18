package lk.workbridge.marketplace.repository;

import lk.workbridge.marketplace.entity.Rating;
import lk.workbridge.marketplace.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.worker.id = :workerId")
    Double getAverageStarsByWorkerId(@Param("workerId") String workerId);
    @Query("SELECT r FROM Rating r WHERE r.worker = :worker")
    List<Rating> findByWorker(@Param("worker") Worker worker);

}
