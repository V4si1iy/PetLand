package pet.project.PetLand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.project.PetLand.service.ShelterService;
import pet.project.PetLand.model.Shelter;

import java.util.Collection;

@RestController
@RequestMapping("shelter")
public class ShelterController {
    private final ShelterService shelterService;
    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

@Operation(
        tags = "Shelter store",
        summary = "Найти все приюты в базе",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Найти все приюты",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                array = @ArraySchema(schema = @Schema(implementation = Shelter.class))
                        )
                )
        }
                )
    @GetMapping
    public ResponseEntity<Collection<Shelter>> findAll() {
        Collection<Shelter> shelters = shelterService.findAll();
        return ResponseEntity.ok(shelters);
    }

    @Operation(
            tags = "Shelter store",
            summary = "Найти приют по ID в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найти приют",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Приютов по этому id не найдено"
                    )
            }
    )

    @GetMapping("{id}")
    public ResponseEntity<Shelter> findById(@PathVariable Long id) {
        Shelter shelter = shelterService.findById(id);
        return ResponseEntity.ok(shelter);
    }

    @Operation(
            tags = "Shelter store",
            summary = "Добавить новый приют в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавить приют",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Поле id игнорируется, id автоматически увеличивается на 1",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Shelter.class)
                    )
            )
    )

    @PostMapping
    public ResponseEntity<Shelter> create(@RequestBody Shelter shelter) {
        Shelter createdShelter = shelterService.create(shelter);
        return ResponseEntity.ok(createdShelter);
    }

    @Operation(
            tags = "Shelter store",
            summary = "Обновить приют ID в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Обновить приют",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Приюта по этому id не найдено"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "если введен не существующий id, будет возвращена ошибка 404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Shelter.class)
                    )
            )
    )

    @PutMapping
    public ResponseEntity<Shelter> update(@RequestBody Shelter shelter) {
        Shelter updatedShelter = shelterService.update(shelter);
        return ResponseEntity.ok(updatedShelter);
    }

    @Operation(
            tags = "Shelter store",
            summary = "Удалить приют по ID из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удалить приют",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Приюта по этому id не найдено"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "если введен не существующий id, будет возвращена ошибка 404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Shelter.class)
                    )
            )
    )

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        shelterService.delete(id);
    }

}
