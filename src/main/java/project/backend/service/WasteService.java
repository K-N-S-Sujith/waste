package project.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

import project.backend.dto.BinPriority;
import project.backend.model.WasteBin;
import project.backend.repository.WasteBinRepository;

@Service
public class WasteService {

    private final WasteBinRepository repo;

    public WasteService(WasteBinRepository repo) {
        this.repo = repo;
    }

    public WasteBin save(WasteBin bin) {
        return repo.save(bin);
    }

    public List<WasteBin> getAll() {
        return repo.findAll();
    }

    public List<WasteBin> getUrgentBins() {
        return repo.findByFillLevelGreaterThan(80);
    }

    public List<WasteBin> getOptimizedRoute(double truckLat, double truckLng) {

        List<WasteBin> bins = repo.findAll();
        List<BinPriority> list = new ArrayList<>();

        for (WasteBin bin : bins) {

            double distance = distanceFromTruck(truckLat, truckLng, bin);

            double score = (0.5 * bin.getFillLevel()) +
                    (0.3 * bin.getGrowthRate()) -
                    (0.2 * distance);

            list.add(new BinPriority(bin, score));
        }

        // Sort by score descending
        list.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        // Extract bins in order
        List<WasteBin> result = new ArrayList<>();
        for (BinPriority bp : list) {
            result.add(bp.getBin());
        }

        return result;
    }

    private double distanceFromTruck(double truckLat, double truckLng, WasteBin bin) {
        return Math.sqrt(
                Math.pow(truckLat - bin.getLatitude(), 2) +
                        Math.pow(truckLng - bin.getLongitude(), 2)
        );
    }

    private double distance(WasteBin a, WasteBin b) {
        double R = 6371; // Earth radius in km

        double latDiff = Math.toRadians(b.getLatitude() - a.getLatitude());
        double lonDiff = Math.toRadians(b.getLongitude() - a.getLongitude());

        double aVal = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(a.getLatitude())) *
                Math.cos(Math.toRadians(b.getLatitude())) *
                Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);

        double c = 2 * Math.atan2(Math.sqrt(aVal), Math.sqrt(1 - aVal));

        return R * c;
    }
    public List<WasteBin> getFastFillingBins() {
        return repo.findByGrowthRateGreaterThan(5.0);
    }
    public WasteBin addOrUpdate(WasteBin newData) {

        WasteBin bin = repo.findById(newData.getId())
                .orElse(new WasteBin());

        bin.setId(newData.getId());
        bin.setFillLevel(newData.getFillLevel());
        bin.setLocation(newData.getLocation());
        bin.setLatitude(newData.getLatitude());
        bin.setLongitude(newData.getLongitude());
        bin.setLastUpdated(java.time.LocalDateTime.now());

        return repo.save(bin);
    }
}