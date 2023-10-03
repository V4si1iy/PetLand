package pet.project.PetLand.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.model.Pet;
import pet.project.PetLand.model.Report;
import pet.project.PetLand.model.ReportPhoto;
import pet.project.PetLand.repository.ReportPhotoRepository;
import pet.project.PetLand.repository.ReportRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportPhotoRepository reportPhotoRepository;

    @Mock
    private PetService petService;

    @InjectMocks
    private ReportService reportService;
    private List<Report> listReportsTest() {
        LocalDateTime d = LocalDate.of(2023, 9, 20).atStartOfDay();
        Report report1 = createTestReport(1L, "отчет1", null, null);
        Report report2 = createTestReport(2L, "отчет2", null, null);

        List<Report> reports = new ArrayList<>();
        reports.add(report1);
        reports.add(report2);

        return reports;
    }

    private Report createTestReport(long id, String petReport, LocalDateTime date, Pet petId) {
        Report report = new Report();
        report.setId(id);
        report.setPetReport(petReport);
        report.setDate(date);
        report.setPet(petId);
        return (report);
    }
    private Report expectedReport() {
        Report rep1 = new Report();
        rep1.setId(1L);
        rep1.setPetReport("отчет1");
        rep1.setDate(LocalDate.of(2023, 2, 3).atStartOfDay());
        rep1.setPet(null);
        return rep1;

    }

    private Report testReportWrong() {
        Report rep2 = new Report();
        rep2.setId(3L);
        rep2.setPetReport("отчет2");
        rep2.setDate(LocalDate.of(2023, 9, 20).atStartOfDay());
        rep2.setPet(null);
        return rep2;
    }
    @Test
    public void findAllReportsTest() {
        List<Report> reports = listReportsTest();
        when(reportRepository.findAll()).thenReturn(reports);
        List<Report> actual = reportService.findAllReports();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(reports);
    }
    @Test
    public void findReportsById() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(expectedReport()));
        when(reportRepository.findById(3L)).thenReturn(Optional.of(testReportWrong()));

        Report actual = reportService.findReportById(1L);

        assertThat(actual.getId()).isEqualTo(expectedReport().getId());
        assertThat(actual.getPetReport()).isEqualTo(expectedReport().getPetReport());
        assertThat(actual.getDate()).isEqualTo(expectedReport().getDate());

        assertThat(reportService.findReportById(3L).getId()).isNotEqualTo(expectedReport().getId());
    }
    @Test
    public void deleteReport() {
        when(reportRepository.findById(2L)).thenReturn(Optional.empty());
        Report expected = null;
        Report actual = reportService.deleteReport(2L);
        assertThat(actual).isEqualTo(expected);
        Report report = createTestReport(1L, "отчет1", LocalDate.of(2023, 9, 20).atStartOfDay(), null);
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));

        expected = report;
        actual = reportService.deleteReport(1L);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void createReport() {
        when(reportRepository.save(expectedReport())).thenReturn(expectedReport());
        assertThat(reportService.createReport(expectedReport())).isEqualTo(expectedReport());
    }
    @Test
    public void updateReport() {
        Report expected = createTestReport(1L, "отчет1", LocalDate.of(2023, 9, 20).atStartOfDay(), null);
        when(reportRepository.save(expected)).thenReturn(expected);
        Report actual = reportService.updateReport(expected);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void findTodayCompletedReportsByPetId() {
        Pet pet = new Pet();
        pet.setId(1L);
        LocalDate localDate = LocalDate.now();
        LocalDateTime startTime = localDate.atStartOfDay();
        LocalDateTime finishTime = LocalTime.MAX.atDate(localDate);
        Report expected = expectedReport();
        ReportPhoto expectedPhoto = new ReportPhoto();

        when(reportRepository.findFirstByPetIdAndPetReportNotNullAndDateBetween(pet.getId(), startTime, finishTime)).thenReturn(expected);
        when(reportPhotoRepository.findFirstByReportId(expected.getId())).thenReturn(expectedPhoto);

        Report actual = reportService.findTodayCompletedReportsByPetId(pet.getId());

        Assertions.assertThat(actual.getId()).isEqualTo(expectedReport().getId());
        Assertions.assertThat(actual.getPetReport()).isEqualTo(expectedReport().getPetReport());
        Assertions.assertThat(actual.getDate()).isEqualTo(expectedReport().getDate());

        when(reportPhotoRepository.findFirstByReportId(expected.getId())).thenReturn(null);
        actual = reportService.findTodayCompletedReportsByPetId(pet.getId());
        Assertions.assertThat(actual).isNull();

        when(reportRepository.findFirstByPetIdAndPetReportNotNullAndDateBetween(pet.getId(), startTime, finishTime)).thenReturn(null);
        actual = reportService.findTodayCompletedReportsByPetId(pet.getId());
        Assertions.assertThat(actual).isNull();
    }
    @Test
    public void findTodayReportByPetId() {
        Pet pet = new Pet();
        pet.setId(1L);
        LocalDate localDate = LocalDate.now();
        LocalDateTime startTime = localDate.atStartOfDay();
        LocalDateTime finishTime = LocalTime.MAX.atDate(localDate);

        Report expected = expectedReport();
        when(reportRepository.findReportByPetIdAndDateBetween(pet.getId(), startTime, finishTime)).thenReturn(expected);
        Report actual = reportService.findTodayReportByPetId(pet.getId());

        Assertions.assertThat(actual.getId()).isEqualTo(expectedReport().getId());
        Assertions.assertThat(actual.getPetReport()).isEqualTo(expectedReport().getPetReport());
        Assertions.assertThat(actual.getDate()).isEqualTo(expectedReport().getDate());
    }
    @Test
    public void findPetsWithoutTodayReport() {
        Customer customer = new Customer();
        List<Pet> expectedPetList = new ArrayList<>(List.of(
                new Pet(1L, customer),
                new Pet(2L, customer)
        ));
        when(petService.findPetsByCustomer(customer)).thenReturn(expectedPetList);
        List<Pet> actual = reportService.findPetsWithoutTodayReport(customer);
        Assertions.assertThat(actual).isEqualTo(expectedPetList);
    }
}
