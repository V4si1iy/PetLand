package pet.project.PetLand.service;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pet.project.PetLand.handler.CallBackQueryHandler;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.model.Pet;
import pet.project.PetLand.model.Report;
import pet.project.PetLand.model.ReportPhoto;
import pet.project.PetLand.repository.ReportPhotoRepository;
import pet.project.PetLand.repository.ReportRepository;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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
    private final Pattern patternReport = Pattern.compile("(Имя:)(\\s)([\\W+]+)(\\n)(Отчет:)(\\s)([\\W+]+)");
    private final Pattern patternReportForm = Pattern.compile("(Name:)(\\W+)(\\n)(First:)(\\W+)(\\n)(Second:)(\\W+)(\\n)(Third:)(\\W+)(\\n)(Link:)(.+)");


    private final ReportRepository reportRepository;
    private final ReportPhotoRepository reportPhotoRepository;
    private final PetService petService;

    public ReportService(ReportRepository reportRepository, ReportPhotoRepository reportPhotoRepository, PetService petService) {
        this.reportRepository = reportRepository;
        this.reportPhotoRepository = reportPhotoRepository;
        this.petService = petService;
    }


    /**
     * Метод ищет все отчеты в базе данных. (Table - Report)
     * @return - найденные отчеты
     */
    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Метод ищет отчет по ID в базе данных. (Table - Report)
     * @param id - ID искомого отчета.
     * @return - найденный отчет
     */
    public Report findReportById(long id) {
        return reportRepository.findById(id).orElse(null);
    }

    /**
     * Метод удаляет отчет по ID из базы данных. (Table - Report)
     * @param id - ID удаляемого отчеты.
     * @return - удаленный отчет
     */
    public Report deleteReport(long id) {
        Report report = findReportById(id);
        if (report != null) {
            reportRepository.delete(report);
        }
        return report;
    }

    /**
     * Метод добавляет отчет в базу данных. (Table - Report)
     * @param report - отчет, который надо добавить.
     * @return - добавленный отчет
     */
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public ReportPhoto createReportPhoto(ReportPhoto report) {
        return reportPhotoRepository.save(report);
    }

    /**
     * <p>Метод с помощью регулярных выражений получает строку, которая сформированна телеграм ботом</p>
     * Pattern: <b>(Имя:)(\s)([\W+]+)(\n)(Отчет:)(\s)([\W+]+)</b>
     * @param message - сообщение полученное от пользователя
     */
    public void createReport(Message message) {
        LOGGER.info("Was invoked method to create new Report in db by Telegram bot");
        LOGGER.debug(message.text());
        Matcher matcher = patternReport.matcher(message.text());
        if (matcher.matches()) {
            String name = matcher.group(3);
            String report = matcher.group(7);
            Pet pet = petService.findByName(name);
            createReport(new Report(report, LocalDateTime.now(), pet));
        }
    }

    /**
     * Метод с помощью регулярных выражений получает строку, которая сформированна яндекс формой
     * form:<p>Name: Name of pet</p><p>First:First paragraph</p><p>Second:Second paragraph</p><p>Third:Third paragraph</p><p>Link:Link on photo by Yandex</p>
     * Pattern: <b>(Name:)(\W+)(\n)(First:)(\W+)(\n)(Second:)(\W+)(\n)(Third:)(\W+)(\n)(Link:)(.+)</b>
     * <a href="https://forms.yandex.ru/u/650ab773068ff033bb7a0a2f/">YandexForm</a>
     * @param form особый вид формы сделанный Vasiliy
     * @throws IOException ошибка обработки фото через url
     */
    public void createReportYandexForm(String form) throws IOException {
        LOGGER.info("Was invoked method to create new Report in db by Yandex Form");
        LOGGER.debug(form);
        Matcher matcher = patternReportForm.matcher(form);
        if (matcher.matches()) {
            String name = matcher.group(2);
            String first = matcher.group(5);
            String second = matcher.group(8);
            String third = matcher.group(11);

            Pet pet = petService.findByName(name);
            Report report = new Report(first + " " + second + " " + third, LocalDateTime.now(), pet);
            createReport(report);

//            Реализация получения фото через яндекс форму(пока не прикручена не получается адекватно скачать фотку из инета по ссылке)
//             URL url = new URL(matcher.group(14));
//             RenderedImage link = ImageIO.read(url);
//             ReportPhoto reportPhoto = new ReportPhoto(((DataBufferByte) link.getData().getDataBuffer()).getData());
//              reportPhoto.addReportPhoto(report);
//              createReportToPhoto(reportPhoto);
        }
    }


    /**
     * Метод обновляет отчет в базе данных.
     * @param report - ID отчета, который надо обновить.
     * @return - обновленный отчет
     */
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
