package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    public Pet savePet(Pet pet, Long ownerId){
        Optional<Customer> optionalCustomer = customerRepository.findById(ownerId);
        Customer owner = optionalCustomer.orElseThrow(UserNotFoundException::new);
        pet.setOwner(owner);
        Pet p = petRepository.save(pet);

        owner.getPets().add(p);
        customerRepository.save(owner);

        return p;
    }

    public List<Pet> retrieveAllPets(){
        return (List<Pet>) petRepository.findAll();
    }

    public Pet findPetById(Long id){
        Optional<Pet> optionalPet = petRepository.findById(id);
        Pet pet = optionalPet.orElseThrow(PetNotFoundException::new);
        return pet;
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        List<Pet> petList = petRepository.findByOwnerId(ownerId);
        return petList;
    }

    public List<Pet> findPets(List<Long> ids){
        return petRepository.findAllById(ids);
    }
}
