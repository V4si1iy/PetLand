package pet.project.PetLand.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.model.Pet;
import pet.project.PetLand.repository.PetRepository;

import java.util.List;

@Service
public class PetService implements PetServiceImpl {

    private final PetRepository petRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(PetService.class);

    public PetService(PetRepository petRepository) {
        LOGGER.debug("Service wire with Repository");
        this.petRepository = petRepository;
    }
    @Override
    public Pet create(Pet pet) {
        LOGGER.info("Method create was invoked");
        Example<Pet> e = Example.of(pet);
        boolean exists = petRepository.exists(e);
        return petRepository.save(pet);

    }
    public Pet findByName(String name)
    {
        return petRepository.findByName(name);
    }

    /**
     * Метод ищет питомца по id в базе данных
     * @param id - id искомого питоомца
     * @return найденный питомец
     * @see PetRepository#findById(Object)
     */
    @Override
    public Pet read(Long id) {
        LOGGER.info("Method read was invoked");
        return petRepository.findById(id).orElse(null);
    }

    /**
     * Метод ищет всех питомцев в базе данных
     * @return найденные питомцы
     * @see PetRepository#findAll()
     */
    @Override
    public List<Pet> read() {
        LOGGER.info("Method read all was invoked");
        return petRepository.findAll();
    }

    public Pet update(Long id, Pet pet) {
        if (petRepository.existsById(id)) {
            pet.setId(id);
            return petRepository.save(pet);
        }
        return null;
    }

    public Pet delete(Long id) {
        Pet currentPet = petRepository.findById(id).orElse(null);
        if (currentPet == null) {
            return null;
        } else {
            petRepository.deleteById(id);
            return currentPet;
        }
    }

    public List<Pet> findPetsByCustomer(Customer customer) {
        return petRepository.findPetsByCustomer(customer);
    }

    public List<Pet> findPetsByCustomerOrderById(Customer customer) {
        return petRepository.findPetsByCustomerOrderById(customer);
    }

    /**
     * Список питомцев, для которых назначен усыновитель
     *
     * @return список питомцев
     */
    @Override
    public List<Pet> findPetsWithCustomer() {
        return petRepository.findPetsByCustomerNotNull();
    }
}
