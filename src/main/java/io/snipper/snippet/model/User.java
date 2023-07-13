package io.snipper.snippet.model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="email", nullable = false, unique = true)
    @Email
    private String email;
    @Column(name="password", nullable = false, unique = true)
    private String password;

    public User(final Long id,
                final String email,
                final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
    public User(){
    }

    public void setId(final Long id) {
        this.id = id;
    }
    public Long getId() {
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
}
