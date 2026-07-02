package dto;

import model.User;

public class AuthResponse {

    private String accessToken;
    private User user;

    public AuthResponse(
            String accessToken,
            User user
    ) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public User getUser() {
        return user;
    }
}