package com.betrybe.agrix.service;

import com.betrybe.agrix.models.entities.Crop;
import com.betrybe.agrix.models.repositories.CropRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class CropService.
 */
@Service
public class CropService {

  private final CropRepository cropRepository;

  @Autowired
  public CropService(CropRepository cropRepository) {
    this.cropRepository = cropRepository;
  }

  //Método insertFarm.
  public Crop insertCrop(Crop crop) {
    return cropRepository.save(crop);
  }

  //Método getAllFarms.
  public List<Crop> getAllCrops() {
    return cropRepository.findAll();
  }

  //Método getFarmById.
  public Optional<Crop> getCropById(Long farmId) {
    return cropRepository.findById(farmId);
  }

  public List<Crop> getCropsByHarvestDateRange(LocalDate start, LocalDate end) {
    return cropRepository.findByHarvestDateBetween(start, end);
  }
}
