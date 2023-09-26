package pet.project.PetLand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.project.PetLand.model.Info;
import pet.project.PetLand.service.InfoService;

import java.util.List;

@RestController
@RequestMapping("info")
public class InfoController {
    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }
    @Operation(
            tags = "Info store",
            summary = "Получить все записи",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    ),
            }
    )
    @GetMapping
    public ResponseEntity <List<Info>> getAllInfoRecords() {
        return ResponseEntity.ok(infoService.getAll());
    }

    @Operation(
            tags = "Info store",
            summary = "Получить записи по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    ),
            }
    )
    @GetMapping("{id}")
    public ResponseEntity <Info> getInfoById(@PathVariable long id) {
        Info info = infoService.findInfoById(id);
        if (info == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(info);
        }
    }

    @Operation(
            tags = "Info store",
            summary = "Добавить запись",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    ),
            }
    )
    @PostMapping
    public ResponseEntity <Info> createInfo(@RequestBody Info info) {
        return ResponseEntity.ok(infoService.createInfo(info));
    }

    @Operation(
            tags = "Info store",
            summary = "Редактировать запись",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    ),
            }
    )
    @PutMapping
    public ResponseEntity <Info> updateInfo(@RequestBody Info newInfo) {
        Info info = infoService.updateInfo(newInfo);
        if (info == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(info);
        }
    }

    @Operation(
            tags = "Info store",
            summary = "Удалить запись по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Info.class)
                            )
                    ),
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity <Info> removeInfo(@PathVariable Long id) {
        Info info = infoService.deleteInfoById(id);
        if (info == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(info);
    }
}
