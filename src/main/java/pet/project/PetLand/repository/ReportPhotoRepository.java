package pet.project.PetLand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.project.PetLand.model.Report;
import pet.project.PetLand.model.ReportPhoto;

import java.util.List;

@Repository
public interface ReportPhotoRepository extends JpaRepository<ReportPhoto, Long> {
    ReportPhoto findFirstByReportId(Long id);

    ReportPhoto findByReport(Report report);
}
