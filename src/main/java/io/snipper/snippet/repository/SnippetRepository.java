package io.snipper.snippet.repository;

import io.snipper.snippet.model.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    Optional<Snippet> findByLanguage(String language);
    Boolean existsByUsername(String language);
}
