package pet.project.PetLand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.project.PetLand.model.Pet;
import pet.project.PetLand.service.PetService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("pet")
public class PetController {
    private final PetService petService;
    public PetController(PetService petService) {
        this.petService = petService;
    }
    @Operation(
            tags = "Pet store",
            summary = "Найти всех животных в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найти животных",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),

            }
    )
    @GetMapping()
    public ResponseEntity<List<Pet>> readAll() {
        List<Pet> result = new ArrayList<>(petService.read());
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @Operation(
            tags = "Pet store",
            summary = "Найти животное по ID в базе ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найти животное по ID",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Pet> readPetById(@PathVariable long id) {
        Pet result = petService.read(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @Operation(
            tags = "Pet store",
            summary = "Добавить животное в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавить животное",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
            }
    )
    @PostMapping()
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        Pet result = petService.create(pet);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @Operation(
            tags = "Pet store",
            summary = "Обновить информацию о животном по ID в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Обновить информацию о животном",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable long id, @RequestBody Pet pet) {
        Pet result = petService.update(id, pet);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @Operation(
            tags = "Pet store",
            summary = "Удалить животное из базы по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удалить животное",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Pet> deletePet(@PathVariable long id) {
        Pet result = petService.delete(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
