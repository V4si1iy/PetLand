package pet.project.PetLand.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Shelter { // Таблица: Приют
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // уникальный id
    private String name; // название приюта питомцев
    private String address; // адрес
    private String locationMap; // ссылка на схему проезда
    private String description; // описание приюта
    private String rules; // правила приюта
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "volunteer_shelter",
            joinColumns = @JoinColumn(name = "shelter_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "volunteer_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<Volunteer> volunteers;

    public Shelter(long id, String name, String address, String locationMap, String description, String rules, Set<Volunteer> volunteers) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.locationMap = locationMap;
        this.description = description;
        this.rules = rules;
        this.volunteers = volunteers;
    }

    public Shelter() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(String locationMap) {
        this.locationMap = locationMap;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Set<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(Set<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelter shelter = (Shelter) o;

        if (id != shelter.id) return false;
        if (name != null ? !name.equals(shelter.name) : shelter.name != null) return false;
        if (address != null ? !address.equals(shelter.address) : shelter.address != null) return false;
        return locationMap != null ? locationMap.equals(shelter.locationMap) : shelter.locationMap == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (locationMap != null ? locationMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}