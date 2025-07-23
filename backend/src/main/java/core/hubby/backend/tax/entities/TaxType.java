package core.hubby.backend.tax.entities;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import core.hubby.backend.tax.entities.embedded.TaxTypes;
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

/**
 * OUTPUT: This is typically used for sales tax (Accounts Receivable).
 * It means the tax is added to the item's price when you sell something.
 * INPUT: This is typically used for purchase tax (Accounts Payable).
 * It means the tax is part of the cost when you buy something.
 * NONE: No tax applies to this line item.
 * BASEEXCLUDED:  This is often used for items that are tax-exempt or where the
 * tax is handled outside of Xero's standard calculation.
 */
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
	@Column(name = "type_collections", columnDefinition = "jsonb", nullable = false)
	@NotNull(message = "typeCollections attribute cannot be null.")
	private Set<TaxTypes> typeCollections;
}
