package pet.project.PetLand.service;

import org.springframework.stereotype.Service;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.model.Pet;
import pet.project.PetLand.model.Volunteer;
import pet.project.PetLand.repository.VolunteerRepository;

import java.util.List;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    /**
     * Метод ищет всех волонтеров в базе данных. (Table - Volunteer)
     * @return - найденные волонтеры
     */
    public List<Volunteer> findAllVolunteers(){
        return volunteerRepository.findAll();
    }

    /**
     * Метод ищев волонтера по id в базе данных. (Table - Volunteer)
     * @param id -  ID волонтера, которого надо найти.
     * @return - найденный волонтер.
     */
    public Volunteer findVolunteerById (long id){
        return volunteerRepository.findById(id).orElse(null);
    }

    /**
     * Метод добавляет волонтера в базу данных. (Table - Volunteer)
     * @param volunteer - волонтер, которого надо добавить в базу данных.
     * @return - добавленный волонтер.
     */
    public Volunteer createVolunteer(Volunteer volunteer){
        return volunteerRepository.save(volunteer);
    }

    /**
     * Метод обновляет информацию о волонтере.
     * @param volunteer - ID волонтра, информацию о котором надо обновить.
     * @return - обновленный волонтер.
     */
    public Volunteer updateVolunteer(Volunteer volunteer) {
        if (findVolunteerById(volunteer.getId()) != null) {
            return volunteerRepository.save(volunteer);
        }
        return null;
    }

    /**
     * Метод удаляет волонтера по id из базы данных .(Table - Volunteer)
     * @param id - ID волонтера, которого хотим удалить.
     * @return - удаленный волонтер
     */
    public Volunteer deleteVolunteer(long id){
        Volunteer volunteer = findVolunteerById(id);
        if (volunteer != null) {
            volunteerRepository.delete(volunteer);
        }
        return volunteer;
    }
}
