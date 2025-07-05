package core.hubby.backend.accounts.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	@NotNull(message = "name cannot be null.")
	private String name;
	private String description;
}
