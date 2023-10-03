package pet.project.PetLand.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Breed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String breed; // порода питомца (key field)
    private String recommendationsChild; // рекомендации по уходу за щенком
    private String recommendationsAdult; // рекомендации по уходу за взрослой собакой
    private int childAge; // ≥ возраст взрослой собаки (признак взрослой собаки, если больше этого значения)

    public Breed() {

    }

    public String getBreed() {
        return breed;
    }

    public String getRecommendationsChild() {
        return recommendationsChild;
    }

    public String getRecommendationsAdult() {
        return recommendationsAdult;
    }

    public int getChildAge() {
        return childAge;
    }

    public Breed(long id, String breed, String recommendationsChild, String recommendationsAdult, int childAge) {
        this.id = id;
        this.breed = breed;
        this.recommendationsChild = recommendationsChild;
        this.recommendationsAdult = recommendationsAdult;
        this.childAge = childAge;
    }

    public long getId() {
        return id;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setRecommendationsChild(String recommendationsChild) {
        this.recommendationsChild = recommendationsChild;
    }

    public void setRecommendationsAdult(String recommendationsAdult) {
        this.recommendationsAdult = recommendationsAdult;
    }

    public void setChildAge(int childAge) {
        this.childAge = childAge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Breed that = (Breed) o;
        return childAge == that.childAge && Objects.equals(breed, that.breed) && Objects.equals(recommendationsChild, that.recommendationsChild) && Objects.equals(recommendationsAdult, that.recommendationsAdult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(breed, recommendationsChild, recommendationsAdult, childAge);
    }
}