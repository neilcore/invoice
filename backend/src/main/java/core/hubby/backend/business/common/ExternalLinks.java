package core.hubby.backend.business.common;
import java.io.Serializable;
import java.util.Set;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalLinks implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Set<String> linkTypes = Set.of(
			"FACEBOOK",
			"INSTAGRAM",
			"WEBSITE",
			"TIKTOK"
	);
	
	@NotNull(message = "linkType attribute cannot be null.")
	private String linkType;
	@NotNull(message = "url attribute cannot be null.")
	private String url;
	
	public void setLinkType(String linkType) {
		if (!linkTypes.contains(linkType.toUpperCase())) {
			throw new IllegalArgumentException("Invalid key found: '" + linkType + "'. Allowed keys are: " + linkTypes);
		}else {
			this.linkType = linkType.toUpperCase();
		}
	}
}
