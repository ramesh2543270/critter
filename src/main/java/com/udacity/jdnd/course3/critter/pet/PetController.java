package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        System.out.println("Calling Save Pet");
        Pet newPet = convertFromPetDTO(petDTO);
        Pet pet = petService.savePet(convertFromPetDTO(petDTO),petDTO.getOwnerId());
        return convertToPetDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.findPetById(petId);
        return convertToPetDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> petList = petService.retrieveAllPets();
        return convertToPetDTOList(petList);
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {

        List<Pet> petList = petService.getPetsByOwner(ownerId);
        return convertToPetDTOList(petList);
    }

    private Pet convertFromPetDTO(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }

    private PetDTO convertToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getOwner().getId());
        return petDTO;
    }

    private List<PetDTO> convertToPetDTOList(List<Pet> petList) {
        List<PetDTO> petDTOList = new ArrayList<>();
        for (Pet aPet: petList) {
            PetDTO cd = new PetDTO();
            BeanUtils.copyProperties(aPet, cd);
            cd.setOwnerId(aPet.getOwner().getId());
            petDTOList.add(cd);
        }
        return petDTOList;
    }

}
