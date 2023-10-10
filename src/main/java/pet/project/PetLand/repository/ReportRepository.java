package pet.project.PetLand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.project.PetLand.model.Report;

import java.time.LocalDateTime;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findReportByPetIdAndDateBetween(Long petId, LocalDateTime startTime, LocalDateTime finishTime);

    Report findFirstByPetIdAndPetReportNotNullAndDateBetween(Long petId, LocalDateTime startTime, LocalDateTime finishTime);

}
