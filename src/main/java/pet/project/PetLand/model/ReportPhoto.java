package pet.project.PetLand.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ReportPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mediaType;
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "id_report")
    private Report report;
}
