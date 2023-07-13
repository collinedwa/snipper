package io.snipper.snippet.model;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Set;

@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
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

    public boolean checkPassword(final String password) {
        return BCrypt.checkpw(password, this.password);
    }

    public String hashPassword() {
        return BCrypt.hashpw(this.password, BCrypt.gensalt());
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_snippets",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "snippet_id", referencedColumnName = "id"))
    private Set<Snippet> snippets;
}
