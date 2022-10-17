package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;
    private final UserService userService;
    private final static Logger logger = LoggerFactory.getLogger(PetController.class);

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Customer customer = userService.findByCustomerId(petDTO.getOwnerId());
        Pet savedPet = petService.save(toPetEntity(petDTO));
        customer.addPet(savedPet);
        userService.saveCustomer(customer);
        return toPetDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return toPetDTO(petService.findById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return petService.findAll().stream().map(this::toPetDTO).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return petService.findPetsByOwner(ownerId).stream().map(this::toPetDTO).collect(Collectors.toList());
    }

    private PetDTO toPetDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        BeanUtils.copyProperties(pet, dto);
        dto.setOwnerId(pet.getCustomer().getId());
        return dto;
    }

    private Pet toPetEntity(PetDTO dto) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(dto, pet);
        return pet;
    }
}
