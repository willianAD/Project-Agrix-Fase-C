package com.betrybe.agrix.controllers;

import com.betrybe.agrix.controllers.dto.PersonsDto;
import com.betrybe.agrix.models.entities.Person;
import com.betrybe.agrix.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class PersonController.
 */
@RestController
@RequestMapping("/persons")
public class PersonController {

  private final PersonService personService;
  
  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }
  
  /**
   * Método createPerson.
   */
  @PostMapping
  public ResponseEntity<?> createPerson(@RequestBody PersonsDto personsDto) {
    Person person = personsDto.toPerson();
    Person savedPerson = personService.create(person);
    PersonsDto.ToResponse response = PersonsDto.ToResponse.fromEntity(savedPerson);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
