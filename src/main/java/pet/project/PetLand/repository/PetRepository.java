package pet.project.PetLand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.model.Pet;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    /**
     * Список питомцев по типу(породе) животного.
     */
    List<Pet> findPetsByBreed(String breed);

    /**
     * Поиск по бд питомцев по усыновителю.
     */
    List<Pet> findPetsByCustomer(Customer customer);

    List<Pet> findPetsByCustomerOrderById(Customer customer);

    List<Pet> findPetsByCustomerNotNull();

}
