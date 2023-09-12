package pet.project.PetLand.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.project.PetLand.model.Breed;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
}
