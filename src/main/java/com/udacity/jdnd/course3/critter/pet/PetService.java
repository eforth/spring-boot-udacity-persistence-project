package com.udacity.jdnd.course3.critter.pet;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getPetsByIds(List<Long> ids) {
        return petRepository.findAllById(ids);
    }

    public Pet findById(Long id) {
        return petRepository.findById(id).orElseThrow(null);
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public List<Pet> findPetsByOwner(Long ownerId) {
        return petRepository.findAllByCustomer_Id(ownerId);
    }
}
