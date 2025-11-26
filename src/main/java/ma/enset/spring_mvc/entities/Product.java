package ma.enset.spring_mvc.entities;

import jakarta.persistence.Entity;//cette classe sera une table dans la base de données.
import jakarta.persistence.GeneratedValue; //la valeur de l’ID sera générée automatiquement.
import jakarta.persistence.Id;//marque l’attribut comme clé primaire de la table.
import jakarta.validation.constraints.Min;//valider que les valeurs numériques sont supérieures ou égales à un minimum.
import jakarta.validation.constraints.NotEmpty;//valide qu’un champ String n’est pas vide (ni null, ni "").
import jakarta.validation.constraints.Size;//impose des longueurs minimales et maximales pour les chaînes de caractères.
import lombok.*;
// Lombok
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class Product {
    // Déclaration de la classe Product.
    @Id @GeneratedValue
    private Long id;
    @NotEmpty // Validation : "name" ne peut pas être vide.
    @Size(min=3, max=50)
    private String name;
    @Min(0)
    private double price;
    @Min(1)
    private double quantity;
}
