package com.betrybe.agrix.controllers.dto;

import com.betrybe.agrix.models.entities.Crop;
import java.time.LocalDate;

/**
 * CropDTO.
 */
public record CropDto(Long id, String name, double plantedArea,
      LocalDate plantedDate, LocalDate harvestDate) {
  /**
   * Set crops.
   */
  public Crop toCrop() {
    Crop crop = new Crop();
    crop.setName(name);
    crop.setPlantedArea(plantedArea);
    return crop;
  }

  /**
   * FromEntity.
   */
  public static record ToResponse(Long id, String name, double plantedArea, LocalDate plantedDate,
      LocalDate harvestDate, Long farmId) {
  }

  public static ToResponse fromEntity(Crop crop) {
    return new ToResponse(crop.getId(), crop.getName(), crop.getPlantedArea(),
      crop.getPlantedDate(), crop.getHarvestDate(), crop.getFarm().getId());
  }

  public LocalDate getPlantedDate() {
    return plantedDate;
  }

  public LocalDate getHarvestDate() {
    return harvestDate;
  }
}
