package pet.project.PetLand.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.tags.EditorAwareTag;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.model.Pet;
import pet.project.PetLand.model.Report;
import pet.project.PetLand.model.ReportPhoto;
import pet.project.PetLand.repository.ReportPhotoRepository;
import pet.project.PetLand.repository.ReportRepository;


import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReportService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private final Pattern patternReport = Pattern.compile("(Имя:)(\\s)([\\W+]+)(\\n)(Отчет:)(\\s)([\\W+]+)");

    private final ReportRepository reportRepository;
    private final ReportPhotoRepository reportPhotoRepository;
    private final PetService petService;
    private final TelegramSenderService telegramSenderService;
    private final TelegramBot telegramBot;

    public ReportService(ReportRepository reportRepository, ReportPhotoRepository reportPhotoRepository, PetService petService, TelegramSenderService telegramSenderService, TelegramBot telegramBot) {
        this.reportRepository = reportRepository;
        this.reportPhotoRepository = reportPhotoRepository;
        this.petService = petService;
        this.telegramSenderService = telegramSenderService;
        this.telegramBot = telegramBot;
    }


    /**
     * Метод ищет все отчеты в базе данных. (Table - Report)
     *
     * @return - найденные отчеты
     */
    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Метод ищет отчет по ID в базе данных. (Table - Report)
     *
     * @param id - ID искомого отчета.
     * @return - найденный отчет
     */
    public Report findReportById(long id) {
        return reportRepository.findById(id).orElse(null);
    }

    /**
     * Метод удаляет отчет по ID из базы данных. (Table - Report)
     *
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
     *
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
     *
     * @param message - сообщение полученное от пользователя
     */
    public boolean createReport(Message message) {
        if (message.caption() == null || message.caption().isEmpty()) {
            telegramSenderService.send(message.chat().id(), "Неправильный ввод, введите отчет снова");
            return false;
        }
        LOGGER.info("Was invoked method to create new Report in db by Telegram bot");
        LOGGER.debug(message.caption());
        Matcher matcher = patternReport.matcher(message.caption());
        PhotoSize[] photoSize = message.photo();
        String fileId = Arrays.stream(photoSize).sorted(Comparator.comparing(PhotoSize::fileSize).reversed()).findFirst().orElse(null).fileId();
        GetFile getFile = new GetFile(fileId);
        File file = telegramBot.execute(getFile).file();
        if (matcher.matches()) {
            String name = matcher.group(3);
            String reportStr = matcher.group(7);
            Pet pet = petService.findByName(name);
            if (Objects.isNull(pet)) {
                LOGGER.debug("Pet with name:" + name + "don't exist");
                telegramSenderService.send(message.chat().id(), "Неверное имя, введите отчет снова");
                return false;
            }
            try {
                byte[] data = telegramBot.getFileContent(file);
                ReportPhoto reportPhoto = new ReportPhoto(data);
                Report report = new Report(reportStr, LocalDateTime.now(), pet);
                reportPhoto.addReportToPhoto(report);
                createReportPhoto(reportPhoto);
                createReport(report);
            } catch (IOException e) {
                e.printStackTrace();
                telegramSenderService.send(message.chat().id(), "Неверное добавление фотки, введите отчет снова");
                return false;
            } finally {
                return true;
            }

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
                Report report = new Report(parts[1] + ". " + parts[2] + ". " + parts[3] + ".", LocalDateTime.now(), pet);
                URL url = new URL(parts[4]);
                BufferedInputStream in = new BufferedInputStream(url.openStream());
                ReportPhoto reportPhoto = new ReportPhoto(in.readAllBytes());
                reportPhoto.addReportToPhoto(report);
                createReport(report);
                createReportPhoto(reportPhoto);
            }

        }
    }


    /**
     * Метод обновляет отчет в базе данных.
     *
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
