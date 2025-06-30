package core.hubby.backend.auth.dto.param;

public record LoginRequest(String email, String password) {
    public LoginRequest {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }
}
