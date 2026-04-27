package project.backend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.backend.model.WasteBin;
import project.backend.repository.WasteBinRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BinSimulator {

    private final WasteBinRepository repo;

    public BinSimulator(WasteBinRepository repo) {
        this.repo = repo;
    }

    @Scheduled(fixedRate = 10800000) // every 3 hours
    public void updateGrowthRate() {

        List<WasteBin> bins = repo.findAll();

        for (WasteBin bin : bins) {

            int lastFill = bin.getLastFillLevel();   // stored previous value
            int currentFill = bin.getFillLevel();    // latest from ESP32

            LocalDateTime lastTime = bin.getLastUpdated();
            double hours = 1;

            if (lastTime != null) {
                hours = Duration.between(lastTime, LocalDateTime.now()).toMinutes() / 60.0;
            }

            double growthRate = 0;
            if (hours > 0) {
                growthRate = (currentFill - lastFill) / hours;
            }

            // update tracking values
            bin.setGrowthRate(growthRate);
            bin.setLastFillLevel(currentFill); // shift current → last
            bin.setLastUpdated(LocalDateTime.now());

            repo.save(bin);
        }
    }
}