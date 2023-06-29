package io.snipper.snippet.model;

public class Snippet {
    private String id;
    private String language;
    private String code;

    public Snippet(final String id,
                final String language,
                final String code) {
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

    public String getCode() {
        return this.code;
    }
}
