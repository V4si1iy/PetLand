package pet.project.PetLand.service;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.project.PetLand.handler.CallBackQueryHandler;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReportService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private final Pattern patternReport = Pattern.compile("(Имя:)(\\s)([\\W+]+)(\\r\\n)(Отчет:)(\\s)([\\W+]+)");


    private final ReportRepository reportRepository;
    private final ReportPhotoRepository reportPhotoRepository;
    private final PetService petService;

    public ReportService(ReportRepository reportRepository, ReportPhotoRepository reportPhotoRepository, PetService petService) {
        this.reportRepository = reportRepository;
        this.reportPhotoRepository = reportPhotoRepository;
        this.petService = petService;
    }


    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }

    public Report findReportById(long id) {
        return reportRepository.findById(id).orElse(null);
    }

    public Report deleteReport(long id) {
        Report report = findReportById(id);
        if (report != null) {
            reportRepository.delete(report);
        }
        return report;
    }

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public void createReport(Message message) {
        Matcher matcher = patternReport.matcher(message.text());
        if (matcher.matches()) {
            String name = matcher.group(1);
            String report = matcher.group(7);
            Pet pet = petService.findByName(name);
            createReport(new Report(report, LocalDateTime.now(), pet));
        }
    }


    public Report updateReport(Report report) {
        return reportRepository.save(report);
    }

    /**
     * Метод ищет сегодняшний ЗАВЕРШЕННЫЙ отчет по id питомца
     *
     * @param petId id питомца
     * @return найденный отчет
     */
    public Report findTodayCompletedReportsByPetId(Long petId) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime startTime = localDate.atStartOfDay();
        LocalDateTime finishTime = LocalTime.MAX.atDate(localDate);
        Report todayReport = reportRepository.findFirstByPetIdAndPetReportNotNullAndDateBetween(petId, startTime, finishTime);
        if (todayReport == null) {
            return null;
        }
        ReportPhoto todayReportPhoto = reportPhotoRepository.findFirstByReportId(todayReport.getId());
        if (todayReportPhoto == null) {
            return null;
        }
        return todayReport;
    }

    /**
     * Метод ищет сегодняшние отчеты по id питомца
     *
     * @param petId id питомца
     * @return найденный отчет
     */
    public Report findTodayReportByPetId(Long petId) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime startTime = localDate.atStartOfDay();
        LocalDateTime finishTime = LocalTime.MAX.atDate(localDate);
        return reportRepository.findReportByPetIdAndDateBetween(petId, startTime, finishTime);
    }

    /**
     * Метод ищет питомцев пользователя, для которых сегодня не был сдан отчет.
     *
     * @return список питомцев
     */
    public List<Pet> findPetsWithoutTodayReport(Customer customer) {
        List<Pet> petWithoutReportList = new ArrayList<>();
        for (Pet pet : petService.findPetsByCustomer(customer)) {
            Report report = findTodayCompletedReportsByPetId(pet.getId());
            if (null == report) {
                petWithoutReportList.add(pet);
            }
        }
        return petWithoutReportList;
    }

    public List<ReportPhoto> getAllPhotoByReportId(Long reportId) {
        Report report = reportRepository.findById(reportId).orElse(null);
        return reportPhotoRepository.findAllByReport(report);
    }

    public ReportPhoto getPhotoById(Long id) {
        return reportPhotoRepository.findById(id).orElse(null);
    }

    /**
     * Формирую список пользователей, которые не сдали сегодня отчет
     *
     * @return список пользователей без отчета сегодня
     */
    public List<Customer> findCustomersWithoutTodayReport() {
        List<Customer> customerWithoutReportList = new ArrayList<>();
        for (Pet pet : petService.findPetsWithCustomer()) {
            Report report = findTodayCompletedReportsByPetId(pet.getId());
            if (null == report) {
                customerWithoutReportList.add(pet.getCustomer());
            }
        }
        return customerWithoutReportList;
    }
}
