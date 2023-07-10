package io.snipper.snippet.model;

public class UserLogin {
    private String email;
    private String password;

    public UserLogin(final String email,
                     final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }
}
