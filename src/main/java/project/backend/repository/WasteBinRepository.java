package project.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.model.WasteBin;

import java.util.List;
import java.util.Optional;

public interface WasteBinRepository extends JpaRepository<WasteBin, Long> {
    List<WasteBin> findByFillLevelGreaterThan(int level);
    List<WasteBin> findByGrowthRateGreaterThan(double threshold);
//    Optional<WasteBin> findByBinId(Long binId);
}