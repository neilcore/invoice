package core.hubby.backend.accounts.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_category", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountCategory {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "account_category_id", nullable = false, unique = true)
	private UUID accountCategoryId;
	
	@Column(name = "category_name", nullable = false, unique = true)
	@NotBlank(message = "name attribute cannot be blank.")
	private String name;
	
	public AccountCategory(@NotNull String name) {
		this.name = name;
	}
}
