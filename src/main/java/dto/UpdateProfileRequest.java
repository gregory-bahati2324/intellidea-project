package dto;

public class UpdateProfileRequest {

    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String avatarUrl;

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}