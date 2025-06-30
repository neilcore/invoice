package core.hubby.backend.business.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "line_items", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LineItems implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	// Shouldn't be include at any JSON response -- FOR NOW
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "invoice_id", referencedColumnName = "id", nullable = false)
	@NotNull(message = "Invoice cannot be null")
	private Invoice invoice;
	
	// A clear explanation of the product or service provided.
	@Column(name = "description", nullable = false)
	@NotBlank(message = "Description cannot be blank")
	private String description;
	
	// Unique identifier assigned by the application itself to each individual line item
	@Column(name = "line_item_id", nullable = false, unique = true)
	@NotBlank(message = "Line item ID cannot be blank")
	private String lineItemId;
	
	// How many units of that product or service were provided.
	@Column(name = "quantity", nullable = false)
	@NotNull(message = "Quantity cannot be blank")
	private Double quantity;
	
	// The cost of one unit of that product or service.
	@Column(name = "unit_amount", nullable = false)
	@NotNull(message = "Unit amount cannot be blank")
	private Double unitAmount;
	
	@Column(name = "account_code", nullable = false)
	@NotBlank(message = "Account code cannot be blank")
	private String accountCode;
	
	@Column(name = "line_amount_type", nullable = false)
	private String lineAmountType;
	
	// This is automatically calculated
	// total value for that specific line item -- LineAmount = Quantity * UnitAmount
	@Column(name = "line_amount", nullable = false)
	@NotNull(message = "Line amount cannot be blank")
	private Double lineAmount;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tax_type", referencedColumnName = "id")
	private TaxType taxType;
	
	@Column(name = "tax_amount", nullable = false)
	@NotNull(message = "Tax amount cannot be null")
	private Double taxAmount;
}
