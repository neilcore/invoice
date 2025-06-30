package core.hubby.backend.business.entities.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalLinks {	
	@NotBlank(message = "LinkType cannot be blank")
	@Column(name = "link_type")
	private String linkType;
	@NotNull(message = "URL cannot be blank")
	private String url;
}
