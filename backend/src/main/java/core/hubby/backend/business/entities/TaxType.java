package core.hubby.backend.business.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tax_type", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TaxType implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "type", nullable = false, unique = true)
	@NotBlank(message = "Tax type cannot be blank")
	private String type;
	
	@Column(name = "rate", nullable = false)
	@Builder.Default
	@NotNull(message = "Rate cannot be null")
	private Double rate = 0.00;
	
	@Column(name = "name", nullable = false)
	@NotBlank(message = "Name cannot be blank")
	private String name;
	
	@Column(name = "system_defined")
	private String systemDefined;
}
