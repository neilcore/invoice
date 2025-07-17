package core.hubby.backend.core.api;

import java.util.List;

import org.springframework.http.HttpStatus;

import jakarta.validation.constraints.NotEmpty;

import java.util.Arrays;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public final class ApiError extends JsonResponse {
	
	@NotEmpty(message = "errors attribute cannot be empty.")
    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super(message, status);
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
    	super(message, status);
        this.errors = Arrays.asList(error);
    }
}
