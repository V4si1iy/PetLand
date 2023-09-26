package pet.project.PetLand.service;

import pet.project.PetLand.model.Customer;
import pet.project.PetLand.model.Pet;

import java.util.List;

public interface PetServiceImpl {
    Pet create(Pet pet);

    Pet read(Long id);

    List<Pet> read();

    Pet update(Long id, Pet pet);

    Pet delete(Long id);

    List<Pet> findPetsByCustomer(Customer customer);

    List<Pet> findPetsWithCustomer();
}
