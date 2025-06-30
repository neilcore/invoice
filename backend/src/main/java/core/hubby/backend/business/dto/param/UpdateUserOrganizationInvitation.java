package core.hubby.backend.business.dto.param;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserOrganizationInvitation (
		@NotNull(message = "invitationFrom component cannot be null.")
		UUID invitationFrom,
		@NotNull(message = "invitationTo component cannot be null")
		UUID invitationTo,
		@NotNull(message = "updatedBy component cannot be null.")
		UUID updatedBy,
		@NotBlank(message = "invitationStatus component cannot be blank.")
		String invitationStatus
) {

}
