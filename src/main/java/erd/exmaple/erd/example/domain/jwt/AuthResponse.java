package erd.exmaple.erd.example.domain.jwt;


public class AuthResponse {
    private final String jwt;
    private final String message;

    public AuthResponse(String jwt,String message) {
        this.jwt = jwt;
        this.message = message;
    }

    public String getJwt() {
        return jwt;
    }
    public String getMessage(String message) {
        return message;
    }
}