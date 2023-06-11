package pl.plantoplate.repository.remote.models;

import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("role")
    private String role;
    @SerializedName("password")
    private String password;

    public UserInfo() {
        // Default constructor required for deserialization
    }

    /**
     * Constructs a new UserInfo object.
     * @param username user's name
     */
    public UserInfo(String username) {
        this.username = username;
    }

    /**
     * Constructs a new UserInfo object.
     * @param username user's name
     * @param email user's email address
     * @param role user's role
     */
    public UserInfo(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
