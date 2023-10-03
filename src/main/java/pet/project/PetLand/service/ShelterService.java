package pet.project.PetLand.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pet.project.PetLand.repository.ShelterRepository;
import pet.project.PetLand.model.Shelter;

import java.util.List;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    @Cacheable("shelters")
    public List<Shelter> findAll() {
        return shelterRepository.findAll();
    }

    public Shelter findByName(String name) {
        return shelterRepository.findByName(name);
    }

    public Shelter findById(Long id) {
        return shelterRepository.findById(id).orElse(null);
    }

    @CachePut("shelters")
    public Shelter create(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    @CachePut("shelters")
    public Shelter update(Shelter shelter) {
        return (findById(shelter.getId()) != null) ? shelterRepository.save(shelter) : null;
    }

    @CacheEvict("shelters")
    public Shelter delete(Long id) {
        Shelter shelter = findById(id);
        if (shelter != null) {
            shelterRepository.delete(shelter);
        }
        return shelter;
    }
}
