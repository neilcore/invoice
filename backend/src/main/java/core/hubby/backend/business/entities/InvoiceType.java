package core.hubby.backend.business.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice_type", schema = "app_sc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceType {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "name", nullable = false, unique = true)
	@NotBlank(message = "Name cannot be blank")
	private String name;
	
	@Column(name = "description")
	private String description;
}
