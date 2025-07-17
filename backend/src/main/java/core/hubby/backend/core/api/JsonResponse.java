package core.hubby.backend.core.api;

import org.springframework.http.HttpStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
sealed class JsonResponse permits ApiError, ApiResponse {
	
	@NotBlank(message = "message attribute cannot be blank.")
	private String message;
	
	@NotNull(message = "status attribute cannot be null.")
	private HttpStatus status;
	
	public JsonResponse(String msg, HttpStatus status) {
		this.message = msg;
		this.status = status;
	}
}
