package pet.project.PetLand.service;

import org.springframework.stereotype.Service;
import pet.project.PetLand.model.Volunteer;
import pet.project.PetLand.repository.VolunteerRepository;

import java.util.List;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public List<Volunteer> findAllVolunteers(){
        return volunteerRepository.findAll();
    }

    public Volunteer findVolunteerById (long id){
        return volunteerRepository.findById(id).orElse(null);
    }

    public Volunteer createVolunteer(Volunteer volunteer){
        return volunteerRepository.save(volunteer);
    }

    public Volunteer updateVolunteer(Volunteer volunteer) {
        if (findVolunteerById(volunteer.getId()) != null) {
            return volunteerRepository.save(volunteer);
        }
        return null;
    }

    public Volunteer deleteVolunteer(long id){
        Volunteer volunteer = findVolunteerById(id);
        if (volunteer != null) {
            volunteerRepository.delete(volunteer);
        }
        return volunteer;
    }
}
