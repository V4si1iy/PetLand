package pet.project.PetLand.service;

import org.springframework.stereotype.Service;
import pet.project.PetLand.repository.ShelterRepository;
import pet.project.PetLand.model.Shelter;

import java.util.Collection;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;
    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public Collection<Shelter> findAll() {
        return shelterRepository.findAll();
    }

    public Shelter findById(Long id) {
        return shelterRepository.findById(id).orElse(null);
    }

    public Shelter create(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    public Shelter update(Shelter shelter) {
        return (findById(shelter.getId()) != null) ? shelterRepository.save(shelter) : null;
    }

    public void delete(Long id) {
        shelterRepository.deleteById(id);
    }
}
