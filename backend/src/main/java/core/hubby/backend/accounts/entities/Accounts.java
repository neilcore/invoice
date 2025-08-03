package core.hubby.backend.accounts.entities;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import core.hubby.backend.accounts.repositories.AccountRepository;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Accounts {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "account_id", nullable = false, unique = true)
	private UUID accountId;
	
	@Column(nullable = false, unique = true)
	@NotBlank(message = "code attribute cannot be blank.")
	private String code;
	
	@Column(name = "account_name", nullable = false, unique = true)
	@NotBlank(message = "accountName attribute cannote be blank.")
	private String accountName;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_type", nullable = false, referencedColumnName = "id")
	@NotNull(message = "accountType attribute cannot be null.")
	private AccountType accountType;
	
	@Column(nullable = false)
	@NotBlank(message = "status attribute cannot be blank.")
	private String status;
	
	private String description;
	
	@Column(name = "tax_type", nullable = false)
	@NotBlank(message = "taxType attribute cannot be blank.")
	private String taxType;
	
	@Column(name = "class_type", nullable = false)
	@NotBlank(message = "classType attribute cannot be blank.")
	private String classType;
	
	@Column(name = "enable_payments_account")
	private boolean enablePaymentToAccount;
	
	@Column(name = "updated_date")
	private LocalDate updatedDate;
	
	@Column(name = "add_to_watch_list")
	private boolean addToWatchList;
	
	public void setStatus(String stat) {
		if (!Set.of(AccountRepository.ACCOUNT_STATUS_ACTIVE, AccountRepository.ACCOUNT_STATUS_ARCHIVED).contains(stat.toUpperCase())) {
			throw new NoSuchElementException("Invalid status.");
		}
		this.status = stat;
	}
}
