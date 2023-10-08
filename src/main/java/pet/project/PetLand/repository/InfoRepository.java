package pet.project.PetLand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.project.PetLand.model.Info;

import java.util.Optional;

@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {
    Optional<Info> findFirstByAreaContainingIgnoreCase(String key);
    Optional<Info> findInfoById(long id);
    Optional<Info> findInfoByArea(String area);
}
