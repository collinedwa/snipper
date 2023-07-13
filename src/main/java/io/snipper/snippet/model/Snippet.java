package io.snipper.snippet.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Set;

@Table(name = "snippets")
public class Snippet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String language;
    private Object code;

    public Snippet(final String id,
                final String language,
                final Object code) {
        this.id = id;
        this.language = language;
        this.code = code;
    }

    public void setId(final String id) {
        this.id = id;
    }
    public String getId() {
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

    public Object getCode() {
        return this.code;
    }
}
