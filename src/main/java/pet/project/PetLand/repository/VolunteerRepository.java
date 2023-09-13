package pet.project.PetLand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.project.PetLand.model.Volunteer;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
}
