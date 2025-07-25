package core.hubby.backend.business.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import core.hubby.backend.business.enums.Roles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_account", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserAccount implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id", nullable = false, unique = true)
	private UUID userId;
	
	@Column(nullable = false, unique = true, name = "email")
	@Email(message = "Email should be valid")
	private String email;
	
	@Column(name = "first_name", nullable = false)
	@NotBlank(message = "First name is mandatory")
	private String firstName;
	
	@Column(name = "last_name", nullable = false)
	@NotBlank(message = "Last name is mandatory")
	private String lastName;
	
	@Column(name = "account_password", nullable = false)
	@NotNull(message = "accountPassword cannot be null.")
	private String accountPassword;
	
	@Column(name = "contact_number", columnDefinition = "jsonb")
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, String> contactNumber;
	
    @Column(name = "roles", nullable = false)
    @NotNull(message = "Roles cannot be null")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Roles roles = Roles.NONE;
    
    // A user by default doesn't own any organization
    @Column(name = "is_owner", nullable = false)
    @Builder.Default
    private boolean isOwner = false;
    
    // A user by default isn't part of any organization
    @Column(name = "part_of_organization", nullable = false)
    @Builder.Default
    private boolean partOfOrganization = false;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userAccount")
    private UserAccountSettings accountSettings;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(roles.name()));
	}

	@Override
	public String getUsername() {
		return this.email;
	}
	
    @Override
    public String getPassword() {
        return this.accountPassword;
    }

}
