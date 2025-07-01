package core.hubby.backend.business.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

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
	@Column(name = "id")
	private UUID id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "invoice_type", referencedColumnName = "id", nullable = false)
	@NotNull(message = "Invoice type cannot be null")
	private InvoiceType invoiceType;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contact", referencedColumnName = "id", nullable = false)
	private Contact contact;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "invoice")
	private List<LineItems> lineItems;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@PastOrPresent(message = "Date cannot be in the future")
	@NotNull(message = "Date cannot be null")
	private LocalDate date;
	
	@Column(name = "due_date", nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@FutureOrPresent(message = "Due date must be today or in the future")
	@NotNull(message = "Due date cannot be null")
	private LocalDate dueDate;
	
	@Builder.Default
	@NotBlank(message = "Status cannot be blank")
	private String status = "DRAFT";
	
	@Column(name = "reference", nullable = true)
	@NotBlank(message = "Reference cannot be blank")
	private String reference;
}
