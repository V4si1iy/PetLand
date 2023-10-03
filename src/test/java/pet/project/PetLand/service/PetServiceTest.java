package pet.project.PetLand.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pet.project.PetLand.model.Pet;
import pet.project.PetLand.repository.PetRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {
    @Mock
    PetRepository petRepository;

    @InjectMocks
    PetService petService;

    static final Long PET_ID_ONE = 1L;
    static final Long PET_ID_TWO = 2L;
    static final String PET_NAME_ONE = "Марсик";
    static final String PET_NAME_TWO = "Карлуша";
    static final String PET_NAME_ONE_UPDATE = "Вайда";
    static final Integer PET_AGE_ONE = 3;
    static final Integer PET_AGE_TWO = 5;
    static final Integer PET_AGE_ONE_UPDATE = 2;
    static final Integer NUMBER_OF_INVOCATIONS = 1;

    static final Pet PET_OBJ_ONE = new Pet(PET_ID_ONE, PET_AGE_ONE, PET_NAME_ONE);
    static final Pet PET_OBJ_TWO = new Pet(PET_ID_TWO, PET_AGE_TWO, PET_NAME_TWO);
    static final Pet PET_OBJ_ONE_UPDATE = new Pet(PET_ID_ONE, PET_AGE_ONE_UPDATE, PET_NAME_ONE_UPDATE);

    static final List<Pet> LIST_OF_TWO_PETS = List.of(PET_OBJ_ONE, PET_OBJ_TWO);

    @Test
    void createPetTest() {
        when(petRepository.save(PET_OBJ_ONE)).thenReturn(PET_OBJ_ONE);
        Pet actual, expected;
        expected = PET_OBJ_ONE;
        actual = petService.create(PET_OBJ_ONE);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getAge(), actual.getAge());
    }

    @Test
    void readPetTest() {
        when(petRepository.findById(PET_ID_ONE)).thenReturn(Optional.ofNullable(PET_OBJ_ONE));
        Pet actual, expected;
        expected = PET_OBJ_ONE;
        actual = petService.read(PET_ID_ONE);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getAge(), actual.getAge());
    }
    @Test
    void readAllPetTest() {
        when(petRepository.findAll()).thenReturn(LIST_OF_TWO_PETS);
        Collection<Pet> actual, expected;
        expected = LIST_OF_TWO_PETS;
        actual = petService.read();
        assertEquals(expected, actual);
    }

    @Test
    void updatePetTest() {
        when(petRepository.save(PET_OBJ_ONE)).thenReturn(PET_OBJ_ONE_UPDATE);
        when(petRepository.existsById(PET_ID_ONE)).thenReturn(true);
        Pet actual, expected;
        actual = petService.update(PET_ID_ONE, PET_OBJ_ONE);
        expected = PET_OBJ_ONE_UPDATE;
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getAge(), actual.getAge());
    }
    @Test
    void deletePetTest() {
        when(petRepository.findById(PET_ID_ONE)).thenReturn(Optional.ofNullable(PET_OBJ_ONE));
        petService.delete(PET_ID_ONE);
        verify(petRepository, times(NUMBER_OF_INVOCATIONS)).deleteById(PET_ID_ONE);
    }
}
