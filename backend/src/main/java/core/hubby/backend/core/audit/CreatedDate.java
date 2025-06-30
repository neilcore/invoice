package core.hubby.backend.core.audit;

import java.time.Instant;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class CreatedDate {
	@org.springframework.data.annotation.CreatedDate
	private Instant createdDate;
}
