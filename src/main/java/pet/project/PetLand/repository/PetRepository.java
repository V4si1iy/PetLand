package pet.project.PetLand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.project.PetLand.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
