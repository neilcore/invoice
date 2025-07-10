package core.hubby.backend.contacts.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ContactPaymentTerms")
@Table(name = "payment_terms", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentTerms implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "payment_term_id", nullable = false, unique = true)
	private UUID id;
	
	@Column(name = "label", nullable = false, unique = true)
	@NotNull(message = "label cannot be null.")
	private String label;
	
	private String description;
}
