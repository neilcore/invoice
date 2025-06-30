package core.hubby.backend.core.dto;

public record ExternalLink(String linkType, String url) {
	public ExternalLink {
		if (linkType.isBlank() || linkType.isEmpty()) {
			throw new IllegalArgumentException("linkType component is required.");
		}
		if (url.isBlank() || url.isEmpty()) {
			throw new IllegalArgumentException("url component is required.");
		}
	}
}
