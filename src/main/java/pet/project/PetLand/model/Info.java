package pet.project.PetLand.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class Info { // Таблица: Информация (Info) - документы, правила
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String area; // область правил (key): транспортировка, обустройства дома щенка, взрослого, советы кинолога, проверенные кинологи, причина отказа

    private String instructions; // правила (инструкции)

    public String getArea() {
        return area;
    }

    public long getId() {
        return id;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Info info = (Info) o;
        return Objects.equals(area, info.area) && Objects.equals(instructions, info.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(area, instructions);
    }
}
