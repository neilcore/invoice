package core.hubby.backend.accounts.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_types", schema = "app_sc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountType {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(unique = true, nullable = false)
	@NotNull(message = "name cannot be null.")
	private String name;
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
	@NotNull(message = "category attribute cannot be null")
	private AccountCategory category;
	
	public AccountType(@NotNull String name, String description, @NotNull AccountCategory category) {
		this.name = name;
		this.description = description;
		this.category = category;
	}
}
