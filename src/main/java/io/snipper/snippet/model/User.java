package io.snipper.snippet.model;

public class User {
    private String id;
    private String email;
    private String password;

    public User(final String id,
                final String email,
                final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public void setId(final String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
