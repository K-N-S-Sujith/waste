package project.backend.controller;


import org.springframework.web.bind.annotation.*;
import project.backend.model.WasteBin;
import project.backend.service.WasteService;

import java.util.List;

@RestController
@RequestMapping("/api/bins")
@CrossOrigin("*")
public class WasteController {

    private final WasteService service;

    public WasteController(WasteService service) {
        this.service = service;
    }

    @PostMapping
    public WasteBin addBin(@RequestBody WasteBin bin) {
        return service.save(bin);
    }

    @GetMapping
    public List<WasteBin> getAll() {
        return service.getAll();
    }

    @GetMapping("/urgent")
    public List<WasteBin> getUrgent() {
        return service.getUrgentBins();
    }

    @GetMapping("/route")
    public List<WasteBin> route(
            @RequestParam double truckLat,
            @RequestParam double truckLng) {

        return service.getOptimizedRoute(truckLat, truckLng);
    }


    @GetMapping("/fast")
    public List<WasteBin> getFastBins() {
        return service.getFastFillingBins();
    }
    @PostMapping("/update")
    public WasteBin updateBin(@RequestBody WasteBin bin) {
        return service.addOrUpdate(bin);
    }

}