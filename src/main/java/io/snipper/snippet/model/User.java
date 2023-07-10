package io.snipper.snippet.model;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class User {
    private String id;
    private String email;
    private String password;
    private boolean loggedIn;

    public User(final String id,
                final String email,
                final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.loggedIn = false;
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

    public boolean checkPassword(final String password) {
        return BCrypt.checkpw(password, this.password);
    }

    public String hashPassword() {
        return BCrypt.hashpw(this.password, BCrypt.gensalt());
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }
}
