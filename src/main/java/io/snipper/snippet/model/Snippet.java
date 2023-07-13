package io.snipper.snippet.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="snippets")
public class Snippet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String language;
    @Column(nullable = false)
    private String code;
    @Column
    private Long ownerId;

    public Snippet(final Long id,
                final String language,
                final String code) {
        this.id = id;
        this.language = language;
        this.code = code;
    }
    public Snippet(){
    }

    public void setId(final Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setOwnerId(final Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }
}
