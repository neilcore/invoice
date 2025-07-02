package core.hubby.backend.business.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale.IsoCountryCode;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organization_name_update", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrganizationNameUpdate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "organization", nullable = false, referencedColumnName = "id")
	private Organization organization;
	
	@Column(name = "organization_created_date", nullable = false)
	@NotNull(message = "organizationCreatedDate cannot be null.")
	@Builder.Default
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate organizationCreatedDate = LocalDate.now();
	
	@Column(name = "updated_date")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate updatedDate;
	
	@Column(name = "is_updatable", nullable = false)
	@NotBlank(message = "organizationNameUpdatable")
	@Builder.Default
	private boolean isUpdatable = true;
	
	@Column(name = "note", nullable = false)
	@Builder.Default
	private String note = "you can change your name once in 20 days";

}
