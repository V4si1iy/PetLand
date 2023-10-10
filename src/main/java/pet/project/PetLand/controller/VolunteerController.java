package pet.project.PetLand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.model.Pet;
import pet.project.PetLand.model.Report;
import pet.project.PetLand.model.Volunteer;
import pet.project.PetLand.service.CustomerService;
import pet.project.PetLand.service.PetService;
import pet.project.PetLand.service.VolunteerService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {
    private final VolunteerService volunteerService;
    private final PetService petService;
    private final CustomerService customerService;

    public VolunteerController(VolunteerService volunteerService, PetService petService, CustomerService customerService) {
        this.volunteerService = volunteerService;
        this.customerService = customerService;
        this.petService = petService;
    }
    @Operation(
            tags = "Volunteer store",
            summary = "Найти всех волонтеров в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найти волонтеров",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = List.class)
                            )
                    )

            }
    )
    @GetMapping
    public ResponseEntity<List<Volunteer>> findAllVolunteers(){
        return ResponseEntity.ok(volunteerService.findAllVolunteers());
    }

    @Operation(
            tags = "Volunteer store",
            summary = "Найти волонтера по ID в базе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтеры",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })
    @GetMapping("/{id}")
    public ResponseEntity<Volunteer> findVolunteerById(@PathVariable long id) {
        Volunteer volunteer = volunteerService.findVolunteerById(id);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteer);
    }

    @Operation(
            tags = "Volunteer store",
            summary = "Добавить волонтера в базу ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтеры",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })
    @PostMapping
    public ResponseEntity<Volunteer> createVolunteer(@RequestBody Volunteer volunteer){
        Volunteer createVolunteer = volunteerService.createVolunteer(volunteer);
        return ResponseEntity.ok(createVolunteer);
    }
    @Operation(
            tags = "Volunteer store",
            summary = "Привязать усыновителя к животному ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтеры",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })

    @PutMapping("{pet-id}/customer/{customer-id}")
    public ResponseEntity<Pet> setCustomerForPet(@PathVariable(name = "pet-id") long petId, @PathVariable(name = "customer-id") long customerId) {
        Customer customer = customerService.findById(customerId);
        Pet pet = petService.read(petId);
        pet.setCustomer(customer);
        LocalDateTime decisionDate = LocalDateTime.now().plusDays(30);
        pet.setDecisionDate(decisionDate);
        petService.update(pet.getId(), pet);

        return ResponseEntity.ok(pet);
    }

    @Operation(
            tags = "Volunteer store",
            summary = "Редактировать волонтеров",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтеры",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })
    @PutMapping
    public ResponseEntity<Volunteer> updateVolunteer(@RequestBody Volunteer volunteer){
        Volunteer update = volunteerService.updateVolunteer(volunteer);
        if (update == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(update);
    }

    @Operation(
            tags = "Volunteer store",
            summary = "Удалить волонтера по ID из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтеры",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Volunteer> deleteVolunteer(@PathVariable long id) {
        Volunteer volunteer = volunteerService.deleteVolunteer(id);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteer);
    }

    @Operation(
            tags = "Volunteer store",
            summary = "Удалить питомца у усыновителя по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтеры",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report[].class)
                            )
                    )
            })
    @DeleteMapping("{pet_id}/customer/{customer-id}")
    public ResponseEntity<Pet> deletePetFromCustomer(@PathVariable(name = "pet_id") long petId, @PathVariable(name = "customer-id") long customerId) {
        Customer customer = customerService.findById(customerId);
        Pet pet = petService.delete(petId);
        if (pet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(pet);
    }
}
