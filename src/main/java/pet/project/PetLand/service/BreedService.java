package pet.project.PetLand.service;

import org.springframework.stereotype.Service;
import pet.project.PetLand.model.Breed;
import pet.project.PetLand.repository.BreedRepository;

import java.util.List;

@Service
public class BreedService {
    private final BreedRepository breedRepository;

    public BreedService(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }


    public List<Breed> findAll() {
        return breedRepository.findAll();
    }

    public Breed findById(Long id) {
        return breedRepository.findById(id).orElse(null);
    }


    public Breed create(Breed breed) {
        return breedRepository.save(breed);
    }


    public Breed update(Breed breed) {
        return (findById(breed.getId()) != null) ? breedRepository.save(breed) : null;
    }


    public Breed delete(Long id) {
        Breed breed = findById(id);
        if (breed != null) {
            breedRepository.delete(breed);
        }
        return breed;
    }
}
