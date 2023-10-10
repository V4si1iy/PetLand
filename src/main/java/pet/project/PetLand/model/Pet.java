package pet.project.PetLand.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // уникальный id
    String animal; // вид животного
    int age; // возраст (месяцев)
    String name; // имя питомца
    @ManyToOne
    @JoinColumn(name = "id_customer")
    private Customer customer;

    private LocalDateTime decisionDate; // дата принятия решения по усыновлению
    @ManyToOne
    @JoinColumn(name="id_shelter")
    private Shelter shelter; // ссылка на приют питомца

    public Pet(Long id, Customer customer) {
        this.id = id;
        this.customer = customer;
    }

    public Pet() {

    }
    public Pet(Long id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    // ----------------------------------------------
    public LocalDateTime getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(LocalDateTime decisionDate) {
        this.decisionDate = decisionDate;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public long getId() {
        return id;
    }


    public String getAnimal() {
        return animal;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return age == pet.age && Objects.equals(animal, pet.animal) && Objects.equals(name, pet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animal, age, name);
    }
}
