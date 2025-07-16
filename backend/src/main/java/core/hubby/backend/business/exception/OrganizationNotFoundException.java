package core.hubby.backend.business.exception;

import java.util.UUID;

public class OrganizationNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OrganizationNotFoundException(UUID id) {
		super("Organization entity with ID " + id + " not found.");
	}

}
