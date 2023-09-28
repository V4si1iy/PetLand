package pet.project.PetLand.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pet.project.PetLand.model.Breed;
import pet.project.PetLand.repository.BreedRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BreedServiceTest {
    @InjectMocks
    private BreedService breedService;

    @Mock
    private BreedRepository breedRepository;

    private List<Breed> expectedBreedList;
    private Breed expectedBreed;

    @BeforeEach
    private void getInitialTestBreeds() {
        expectedBreedList = List.of(
                new Breed(1, "Мопс", "Рекомендации для щенка", "Рекомендации для взрослой собаки", 3),
                new Breed(2, "Овчарка", "Рекомендации для щенка", "Рекомендации для взрослой собаки", 5),
                new Breed(3, "Хаски", "Рекомендации для щенка", "Рекомендации для взрослой собаки", 10)
        );
        expectedBreed = expectedBreedList.get(0);
    }
    @Test
    void findAllBreeds() {
        when(breedRepository.findAll()).thenReturn(expectedBreedList);
        List<Breed> actual = breedService.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expectedBreedList);
    }
    @Test
    void findBreedByIdPositive() {
        Long testId = expectedBreed.getId();
        when(breedRepository.findById(testId)).thenReturn(Optional.of(expectedBreed));
        Breed actual = breedService.findById(testId);
        assertThat(actual).isEqualTo(expectedBreed);
    }
    @Test
    void createBreed() {
        when(breedRepository.save(expectedBreed)).thenReturn(expectedBreed);
        Breed actual = breedService.create(expectedBreed);
        assertThat(actual).isEqualTo(expectedBreed);
    }
    @Test
    void updateBreed() {
        Long testId = expectedBreed.getId();
        when(breedRepository.findById(testId)).thenReturn(Optional.ofNullable(expectedBreed));
        when(breedRepository.save(expectedBreed)).thenReturn(expectedBreed);
        Breed actual = breedService.update(expectedBreed);
        assertThat(actual).isEqualTo(expectedBreed);
    }
    @Test
    void deleteBreed() {
        Long testId = expectedBreed.getId();
        when(breedRepository.findById(testId)).thenReturn(Optional.ofNullable(expectedBreed));
        Breed actual = breedService.delete(testId);
        assertThat(actual).isEqualTo(expectedBreed);
    }
}
