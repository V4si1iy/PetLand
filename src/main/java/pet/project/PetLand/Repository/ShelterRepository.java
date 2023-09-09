package pet.project.PetLand.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.project.PetLand.model.Shelter;

import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    Optional<Shelter> findById(Long id);

    Shelter save(Shelter shelter);

    void deleteById(Long id);

}
