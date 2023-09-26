package pet.project.PetLand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.project.PetLand.model.Report;
import pet.project.PetLand.model.ReportPhoto;
import pet.project.PetLand.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @Operation(
            tags = "Report store",
            summary = "Найти все отчеты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найти отчеты",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = List.class)
                            )
                    ),

            }
    )
    @GetMapping
    public ResponseEntity<List<Report>> findAllReports() {
        return ResponseEntity.ok(reportService.findAllReports());
    }
    @Operation(
            tags = "Report store",
            summary = "Найти отчет по ID в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })
    @GetMapping("/{id}")
    public ResponseEntity<Report> findReportById(@PathVariable Long id) {
        Report report = reportService.findReportById(id);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }
    @Operation(
            tags = "Report store",
            summary = "Удалить отчет по ID из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Report> deleteReport(@PathVariable Long id) {
        Report report = reportService.deleteReport(id);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }
    @Operation(
            tags = "Report store",
            summary = "Обновить отчет по ID в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Обновить отчет",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })
    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(@RequestBody Report report) {
        Report found = reportService.updateReport(report);
        if (found == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(found);
    }
    @Operation(
            tags = "Report store",
            summary = "Добавить отчет в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавить отчет",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        Report create = reportService.createReport(report);
        return ResponseEntity.ok(create);
    }
    @Operation(
            tags = "Report store",
            summary = "Preview фото по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найти отчеты",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = List.class)
                            )
                    ),

            }
    )
    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> previewPhotoById(@PathVariable Long id) {
        ReportPhoto reportPhoto = reportService.getPhotoById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(reportPhoto.getMediaType()));
        headers.setContentLength(reportPhoto.getPhoto().length);
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(reportPhoto.getPhoto());
    }
    @Operation(
            tags = "Report store",
            summary = "Поиск всех фотографий по id отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найти отчеты",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = List.class)
                            )
                    ),

            }
    )
    @GetMapping("{id}/photos")
    public ResponseEntity<List<ReportPhoto>> getPhotosByReportId(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getAllPhotoByReportId(id));
    }

}
