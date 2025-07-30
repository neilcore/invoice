package core.hubby.backend.business.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import core.hubby.backend.tax.entities.TaxType;
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

/**
 *  the "LineItems" collection holds all the granular detail
 *  about what is being bought or sold on that particular invoice.
 *  When you create an invoice in Xero, you add multiple "line items"
 *  to build up the total amount.
 */
@Entity
@Table(name = "line_items", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LineItems implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	// Shouldn't be include at any JSON response -- FOR NOW
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id", nullable = false)
	@NotNull(message = "Invoice cannot be null")
	private Invoice invoice;
	
	// A clear explanation of the product or service provided.
	@Column(name = "lineitem_description", nullable = false)
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
	
	// This is automatically calculated
	// total value for that specific line item -- LineAmount = Quantity * UnitAmount
	@Column(name = "line_amount", nullable = false)
	@NotNull(message = "Line amount cannot be blank")
	private Double lineAmount;
	
	@Column(name = "tax_type", nullable = false)
	@NotBlank(message = "taxType attribute cannot be blank.")
	private String taxType;
	
	// auto-calculated
	@Column(name = "tax_amount", nullable = false, precision = 7, scale = 2)
	@NotNull(message = "Tax amount cannot be null")
	private BigDecimal taxAmount;
	
	@Column(name = "discount_rate")
	private Integer discountRate;
}
