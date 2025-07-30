package core.hubby.backend.business.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import core.hubby.backend.business.repositories.InvoiceRepository;
import core.hubby.backend.contacts.entities.Contact;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Invoice implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "invoice_id", nullable = false, unique = true)
	private UUID invoiceId;
	
	@Column(name = "invoice_type", nullable = false)
	@NotNull(message = "Invoice type cannot be null")
	private String type;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contact", referencedColumnName = "contact_id", nullable = false)
	private Contact contact;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "invoice")
	private Set<LineItems> lineItems;
	
	@Column(name = "line_amount_type")
	private String lineAmountTypes;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@PastOrPresent(message = "Invalid value for date field.")
	@Column(name = "invoice_date")
	private LocalDate date;
	
	@Column(name = "due_date", nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@FutureOrPresent(message = "Due date must be today or in the future")
	@NotNull(message = "Due date cannot be null")
	private LocalDate dueDate;
	
	@Builder.Default
	@NotBlank(message = "Status cannot be blank")
	@Column(name = "invoice_status", nullable = false)
	private String status = "DRAFT";
	
	@Column(name = "reference")
	@NotBlank(message = "Reference cannot be blank")
	private String reference;
	
	@Column(name = "sub_total", nullable = false)
	@NotNull(message = "subTotal cannot be null.")
	private Double subTotal;
	
	@Column(name = "grand_total", nullable = false)
	@NotNull(message = "grandTotal cannot be null.")
	private BigDecimal grandTotal;
	
	@Column(name = "total_tax")
	private BigDecimal totalTax; // value-added tax
	
	public void setLineAmountTypes(String lineAmountTypes) {
		if (lineAmountTypes.isEmpty() || lineAmountTypes.isBlank()) {
			this.lineAmountTypes = null;
		}else if (!Set.of("EXCLUSIVE", "INCLUSIVE", "NO_TAX").contains(lineAmountTypes)) {
			throw new NoSuchElementException("Invalid line amount type.");
		}else {
			this.lineAmountTypes = lineAmountTypes;
		}
	}
	
	
	public void setType(String type) {
		if (!Set.of(InvoiceRepository.INVOICE_TYPE_CUSTOMER_INVOICE, InvoiceRepository.INVOICE_TYPE_SUPPLIER_INVOICE).contains(type.toLowerCase())) {
			throw new NoSuchElementException("Invalid invoice type value.");
		} else {
			this.type = type.toUpperCase();
		}
	}
}
