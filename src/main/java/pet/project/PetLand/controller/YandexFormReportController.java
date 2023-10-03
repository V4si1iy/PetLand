package pet.project.PetLand.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pet.project.PetLand.service.ReportService;

import java.io.IOException;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/yandexForm/report")
@AllArgsConstructor
public class YandexFormReportController {
    ReportService reportService;

    @PostMapping()
    public void getForm(@RequestBody String str) throws IOException {
        reportService.createReportYandexForm(str);
    }
}
