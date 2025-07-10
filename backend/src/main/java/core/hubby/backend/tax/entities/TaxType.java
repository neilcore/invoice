package core.hubby.backend.tax.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	// Usually will hold a country name
	@Column(name = "label", nullable = false, unique = true)
	@NotNull(message = "label cannot be null.")
	private String label;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "tax_type", columnDefinition = "jsonb", nullable = false)
	@NotNull(message = "taxType cannot be null.")
	private Map<String, String> taxType;
}
