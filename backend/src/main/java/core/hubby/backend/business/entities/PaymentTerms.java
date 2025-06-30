package core.hubby.backend.business.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_terms", schema = "app_sc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTerms implements java.io.Serializable {
	@Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false, unique = true)
	@NotBlank(message = "Name cannot be blank")
	private String name;
	
	@Column(nullable = false)
	@NotBlank(message = "Description cannot be blank")
	private String description;
}
