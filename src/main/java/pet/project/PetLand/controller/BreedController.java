package pet.project.PetLand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.project.PetLand.model.Breed;
import pet.project.PetLand.service.BreedService;

import java.util.List;

@RestController
@RequestMapping("breed")
public class BreedController {
    private final BreedService breedService;

    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }
    @Operation(
            tags = "Breed store",
            summary = "Поиск всех пород животных в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найти породы животных",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Breed.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<Breed>> readAll() {
        List<Breed> breeds = breedService.findAll();
        return ResponseEntity.ok(breeds);
    }

    @Operation(
            tags = "Breed store",
            summary = "Поиск породы животного по ID в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найти породы животных",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Breed.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пород животных по этому id не найдено"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Breed> readById(@Parameter(description = "id искомой породы животного") @PathVariable Long id) {
        Breed breed = breedService.findById(id);
        if (breed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(breed);
    }

    @Operation(
            tags = "Breed store",
            summary = "Добавить новую породу животного в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавить породу животного",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Breed.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "поле id игнорируется, id автоматически увеличивается на 1",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Breed.class)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<Breed> create(@RequestBody Breed breed) {
        Breed createdBreed = breedService.create(breed);
        return ResponseEntity.ok(createdBreed);
    }

    @Operation(
            tags = "Breed store",
            summary = "Обновить породу животного по ID в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Обновить породу животного",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Breed.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пород животных по этому id не найдено"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "если введен не существующий id, будет возвращена ошибка 404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Breed.class)
                    )
            )
    )
    @PutMapping
    public ResponseEntity<Breed> update(@RequestBody Breed breed) {
        Breed updatedBreed = breedService.update(breed);
        if (updatedBreed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBreed);
    }

    @Operation(
            tags = "Breed store",
            summary = "Удалить породу животного по ID в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удалить породу животного",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Breed.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пород животных по этому id не найдено"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "если введен не существующий id, будет возвращена ошибка 404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Breed.class))
                    )
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Breed> delete(@Parameter(description = "id удаляемой породы животного") @PathVariable Long id) {
        Breed deletedBreed = breedService.delete(id);
        if (deletedBreed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deletedBreed);
    }
}
