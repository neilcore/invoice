package core.hubby.backend.business.entities;
import java.time.LocalDate;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrganizationNameUpdate {
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "former_name", columnDefinition = "jsonb")
	private Map<String, String> formerName;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "current_name", columnDefinition = "jsonb")
	private Map<String, String> currentName;
	
	@Column(name = "updated_date")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate updatedDate;
	
	@Column(name = "is_updatable", nullable = false)
	@NotNull(message = "isUpdatable cannot be null.")
	@Builder.Default
	private boolean isUpdatable = true;
	
	@Column(name = "note", nullable = false)
	@Builder.Default
	private String note = "you can change your name once in 20 days";

}
