package pet.project.PetLand.service;

import com.pengrad.telegrambot.TelegramBot;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import pet.project.PetLand.model.Volunteer;
import pet.project.PetLand.repository.VolunteerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VolunteerServiceTest {
    @Mock
    private VolunteerRepository volunteerRepository;

    @MockBean
    private TelegramBot bot;

    @InjectMocks
    private VolunteerService volunteerService;

    private List<Volunteer> volunteersForTest() {

        Volunteer vol1 = createTestVolunteer(1L, 1234L, "Петров", "Петр", "Петрович", "89097653344", "3-я улица Строителей, 5");
        Volunteer vol2 = createTestVolunteer(2L, 4567L, "Иванов", "Иван", "Иванович", "89653421232", "Проектируемый проезд, 6");

        List<Volunteer> volunteers = new ArrayList<>();
        volunteers.add(vol1);
        volunteers.add(vol2);

        return volunteers;
    }
    private Volunteer createTestVolunteer(Long id, Long chatId, String surname, String name, String secondName, String phone, String address) {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(id);
        volunteer.setChatId(chatId);
        volunteer.setSurname(surname);
        volunteer.setName(name);
        volunteer.setSecondName(secondName);
        volunteer.setPhone(phone);
        volunteer.setAddress(address);
        return (volunteer);
    }
    private Volunteer testVolunteer() {
        Volunteer v1 = new Volunteer();
        v1.setId(1L);
        v1.setChatId(1234L);
        v1.setSurname("Петров");
        v1.setName("Петр");
        v1.setSecondName("Петрович");
        v1.setPhone("89097653344");
        v1.setAddress("3-я улица Строителей, 5");
        return v1;

    }

    private Volunteer testVolunteerWrong() {
        Volunteer v2 = new Volunteer();
        v2.setId(3L);
        v2.setChatId(6789L);
        v2.setSurname("Смирнова");
        v2.setName("Елена");
        v2.setSecondName("Викторовна");
        v2.setPhone("89675456745");
        v2.setAddress("Ленинский проспект, 63");
        return v2;
    }

    private Volunteer testVolunteerUpdate() {
        Volunteer v3 = new Volunteer();
        v3.setId(3L);
        v3.setChatId(5612L);
        v3.setSurname("Козлов");
        v3.setName("Олег");
        v3.setSecondName("Игоревич");
        v3.setPhone("89613245678");
        v3.setAddress("Коломенский проезд, 1");
        return v3;
    }
    @Test
    void findAllVolunteers(){
        List<Volunteer> volunteers = volunteersForTest();
        when(volunteerRepository.findAll()).thenReturn(volunteers);
        List<Volunteer> actual = volunteerService.findAllVolunteers();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(volunteers);
    }
    @Test
    void  findVolunteerById(){
        when(volunteerRepository.findById(1L)).thenReturn(Optional.of(testVolunteer()));
        when(volunteerRepository.findById(3L)).thenReturn(Optional.of(testVolunteerWrong()));
        assertThat(volunteerService.findVolunteerById(1L)).isEqualTo(testVolunteer());
        assertThat(volunteerService.findVolunteerById(3L)).isNotEqualTo(testVolunteer());
    }
    @Test
    void  createVolunteer(){
        when(volunteerRepository.
                save(testVolunteer())).
                thenReturn(testVolunteer());
        assertThat(volunteerService.
                createVolunteer(testVolunteer())).
                isEqualTo(testVolunteer());
    }
    @Test
    public void updateVolunteer() {
        Volunteer expected = createTestVolunteer(1L, 1234L, "Петров", "Петр", "Петрович", "89097653344", "Нагатинская, 1");
        when(volunteerRepository.findById(expected.getId())).thenReturn(Optional.ofNullable(expected));
        when(volunteerRepository.save(expected)).thenReturn(expected);
        Volunteer actual = volunteerService.updateVolunteer(expected);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void deleteVolunteer() {
        when(volunteerRepository.findById(2L)).thenReturn(Optional.empty());
        Volunteer expected = null;
        Volunteer actual = volunteerService.deleteVolunteer(2L);
        Assertions.assertThat(actual).isEqualTo(expected);
        Volunteer volunteer = createTestVolunteer(3L, 1234L, "Петров", "Петр", "Петрович", "89097653344", "Нагатинская, 1");
        when(volunteerRepository.findById(3L)).thenReturn(Optional.of(volunteer));

        expected = volunteer;
        actual = volunteerService.deleteVolunteer(3L);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
