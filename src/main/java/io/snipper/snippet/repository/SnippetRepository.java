package io.snipper.snippet.repository;

import io.snipper.snippet.model.Snippet;
import io.snipper.snippet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    Optional<Snippet> findById(Long id);
    List<Snippet> findAllByOwnerId(Long ownerId);
    List<Snippet> findAll();
}
