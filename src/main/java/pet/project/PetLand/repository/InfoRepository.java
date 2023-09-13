package pet.project.PetLand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.project.PetLand.model.Info;

@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {
}
