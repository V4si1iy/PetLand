package pet.project.PetLand.service;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.model.Pet;
import pet.project.PetLand.model.Report;
import pet.project.PetLand.model.ReportPhoto;
import pet.project.PetLand.repository.ReportPhotoRepository;
import pet.project.PetLand.repository.ReportRepository;


import java.awt.*;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.Toolkit.getDefaultToolkit;

@Service
public class ReportService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private final Pattern patternReport = Pattern.compile("(Имя:)(\\s)([\\W+]+)(\\n)(Отчет:)(\\s)([\\W+]+)");

    private final ReportRepository reportRepository;
    private final ReportPhotoRepository reportPhotoRepository;
    private final PetService petService;
    private final TelegramSenderService telegramSenderService;

    public ReportService(ReportRepository reportRepository, ReportPhotoRepository reportPhotoRepository, PetService petService, TelegramSenderService telegramSenderService) {
        this.reportRepository = reportRepository;
        this.reportPhotoRepository = reportPhotoRepository;
        this.petService = petService;
        this.telegramSenderService = telegramSenderService;
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

    public ReportPhoto createReportPhoto(ReportPhoto report) {
        return reportPhotoRepository.save(report);
    }

    /**
     * <p>Метод с помощью регулярных выражений получает строку, которая сформированна телеграм ботом</p>
     * Pattern: <b>(Имя:)(\s)([\W+]+)(\n)(Отчет:)(\s)([\W+]+)</b>
     *
     * @param message - сообщение полученное от пользователя
     */
    public boolean createReport(Message message) {
        LOGGER.info("Was invoked method to create new Report in db by Telegram bot");
        LOGGER.debug(message.text());
        Matcher matcher = patternReport.matcher(message.text());
        if (matcher.matches()) {
            String name = matcher.group(3);
            String report = matcher.group(7);
            Pet pet = petService.findByName(name);
            if (Objects.isNull(pet)) {
                LOGGER.debug("Pet with name:" + name + "don't exist");
                telegramSenderService.send(message.chat().id(), "Неверное имя, введите отчет снова");
                return false;
            }
            createReport(new Report(report, LocalDateTime.now(), pet));
            return true;
        } else {
            LOGGER.info("Incorrect input of report Telegram");
            telegramSenderService.send(message.chat().id(), "Неверный формат, введите отчет снова");
            return false;
        }

    }

    /**
     * Метод с помощью регулярных выражений получает строку, которая сформированна яндекс формой
     * form:<p>Name</p><p>First paragraph</p><p>Second paragraph</p><p>Third paragraph</p><p>Link on photo by Yandex</p>
     * Pattern: <b>(\W+)(\n)(\W+)(\n)(\W+)(\n)(\W+)(\n)(.+)</b>
     * <a href="https://forms.yandex.ru/u/650ab773068ff033bb7a0a2f/">YandexForm</a>
     *
     * @param form особый вид формы сделанный Vasiliy
     * @throws IOException ошибка обработки фото через url
     */
    public void createReportYandexForm(String form) throws IOException {
        LOGGER.info("Was invoked method to create new Report in db by Yandex Form");
        LOGGER.debug(form);
        String[] parts = form.split("\n");
        if (parts.length == 5) {
            Pet pet = petService.findByName(parts[0]);

            if (!Objects.isNull(pet)) {
                Report report = new Report(parts[1] + ". " + parts[2] + ". " + parts[3]+ ".", LocalDateTime.now(), pet);
                URL url = new URL(parts[4]);
                BufferedInputStream in = new BufferedInputStream(url.openStream());
                ReportPhoto reportPhoto= new ReportPhoto(in.readAllBytes());
                reportPhoto.addReportToPhoto(report);
                createReport(report);
                createReportPhoto(reportPhoto);
            }

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
