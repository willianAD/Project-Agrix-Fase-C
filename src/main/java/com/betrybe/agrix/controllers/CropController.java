package com.betrybe.agrix.controllers;

import com.betrybe.agrix.controllers.dto.CropDto;
import com.betrybe.agrix.controllers.dto.FertilizerDto;
import com.betrybe.agrix.models.entities.Crop;
import com.betrybe.agrix.models.entities.Farm;
import com.betrybe.agrix.models.entities.Fertilizer;
import com.betrybe.agrix.service.CropService;
import com.betrybe.agrix.service.FarmService;
import com.betrybe.agrix.service.FertilizerService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class CropController.
 */
@RestController
@RequestMapping()
public class CropController {

  private final CropService cropService;
  private final FarmService farmService;
  private final FertilizerService fertilizerService;

  /**
 * Autowired.
 */
  @Autowired
  public CropController(CropService cropService, FarmService farmService,
      FertilizerService fertilizerService) {
    this.cropService = cropService;
    this.farmService = farmService;
    this.fertilizerService = fertilizerService;
  }

  /**
   * Método getAllCrops.
   */
  @GetMapping("/crops")
  public ResponseEntity<?> getAllCrops() {
    List<Crop> allCrops = cropService.getAllCrops();

    List<CropDto.ToResponse> cropDto = allCrops.stream().map(CropDto::fromEntity).toList();
    return ResponseEntity.ok(cropDto);
  }

  /**
   * Método getCropById.
   */
  @GetMapping("/crops/{id}")
  public ResponseEntity<?> getCropById(@PathVariable Long id) {
    Optional<Crop> optionalCrop = cropService.getCropById(id);
    if (optionalCrop.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plantação não encontrada!");
    }
    return optionalCrop.map(crop -> {
      CropDto.ToResponse response = CropDto.fromEntity(crop);
      return ResponseEntity.ok(response);
    }).orElse(ResponseEntity.notFound().build());
  }

  /**
   * Método createCrop.
   */
  @PostMapping("/farms/{farmId}/crops")
  public ResponseEntity<?> createCrop(@PathVariable Long farmId, @RequestBody CropDto cropDto) {
    Optional<Farm> optionalFarm = farmService.getFarmById(farmId);
    if (optionalFarm.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fazenda não encontrada!");
    }
    return optionalFarm.map(farm -> {
      Crop crop = cropDto.toCrop();
      crop.setFarm(farm);
      crop.setPlantedDate(cropDto.getPlantedDate());
      crop.setHarvestDate(cropDto.getHarvestDate());
      Crop savedCrop = cropService.insertCrop(crop);
      CropDto.ToResponse response = CropDto.fromEntity(savedCrop);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }).orElse(ResponseEntity.notFound().build());
  }

  /**
   * Método getCropFarm.
   */
  @GetMapping("/farms/{farmId}/crops")
  public ResponseEntity<?> getCropFarm(@PathVariable Long farmId) {
    Optional<Farm> optionalFarm = farmService.getFarmById(farmId);
    if (optionalFarm.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fazenda não encontrada!");
    }
    List<Crop> crops = optionalFarm.get().getCrops();
    List<CropDto.ToResponse> cropResponse = crops.stream().map(CropDto::fromEntity).toList();
    return ResponseEntity.ok(cropResponse);
  }

  /**
   * Método searchCropsByHarvestDate.
   */
  @GetMapping("/crops/search")
  public ResponseEntity<?> searchCropsByHarvestDate(@RequestParam String start,
      @RequestParam String end) {
    LocalDate startDate = LocalDate.parse(start);
    LocalDate endDate = LocalDate.parse(end);

    List<Crop> cropsInRange = cropService.getCropsByHarvestDateRange(startDate, endDate);

    List<CropDto.ToResponse> cropDto = cropsInRange.stream().map(CropDto::fromEntity).toList();
    return ResponseEntity.ok(cropDto);
  }

  /**
   * Método associateCropWithFertilizer.
   */
  @PostMapping("/crops/{cropId}/fertilizers/{fertilizerId}")
  public ResponseEntity<String> associateCropWithFertilizer(@PathVariable Long cropId,
      @PathVariable Long fertilizerId) {
    Optional<Crop> optionalCrop = cropService.getCropById(cropId);
    if (optionalCrop.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plantação não encontrada!");
    }

    Optional<Fertilizer> optionalFertilizer = fertilizerService.getFertilizerById(fertilizerId);
    if (optionalFertilizer.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fertilizante não encontrado!");
    }

    Crop crop = optionalCrop.get();
    Fertilizer fertilizer = optionalFertilizer.get();

    if (crop.getFertilizers().contains(fertilizer)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fertilizante não encontrado!");
    }

    crop.addFertilizer(fertilizer);
    cropService.insertCrop(crop);

    return ResponseEntity.status(HttpStatus.CREATED)
      .body("Fertilizante e plantação associados com sucesso!");
  }

  /**
   * Método getFertilizersByCropId.
   */
  @GetMapping("/crops/{cropId}/fertilizers")
  public ResponseEntity<?> getFertilizersByCropId(@PathVariable Long cropId) {
    Optional<Crop> optionalCrop = cropService.getCropById(cropId);
    if (optionalCrop.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plantação não encontrada!");
    }

    Crop crop = optionalCrop.get();
    List<Fertilizer> fertilizers = crop.getFertilizers();

    List<FertilizerDto.ToResponse> fertilizerDtos = fertilizers.stream()
        .map(FertilizerDto::fromEntity).toList();

    return ResponseEntity.ok(fertilizerDtos);
  }
}
