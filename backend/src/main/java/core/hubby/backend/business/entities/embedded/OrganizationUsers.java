package core.hubby.backend.business.entities.embedded;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import core.hubby.backend.business.entities.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUsers {	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User userId;
	
	@NotBlank(message = "User role cannot be blank")
	@Column(name = "user_role", nullable = false)
	private String userRole;
	
	@Column(name = "user_joined", nullable = false)
	@Builder.Default
	private LocalDate userJoined = LocalDate.now();
	
	@Column(name = "organization_status", nullable = false)
	@NotBlank(message = "organizationStatus cannpt be blank.")
	private String organizationStatus;
}
