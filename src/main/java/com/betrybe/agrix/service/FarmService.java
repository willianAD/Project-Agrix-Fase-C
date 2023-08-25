package com.betrybe.agrix.service;

import com.betrybe.agrix.models.entities.Farm;
import com.betrybe.agrix.models.repositories.FarmRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class FarmService.
 */
@Service
public class FarmService {
  private final FarmRepository farmRepository;

  @Autowired
  public FarmService(FarmRepository farmRepository) {
    this.farmRepository = farmRepository;
  }

  //Método insertFarm.
  public Farm insertFarm(Farm farm) {
    return farmRepository.save(farm);
  }

  //Método getAllFarms.
  public List<Farm> getAllFarms() {
    return farmRepository.findAll();
  }

  //Método getFarmById.
  public Optional<Farm> getFarmById(Long id) {
    return farmRepository.findById(id);
  }
}
