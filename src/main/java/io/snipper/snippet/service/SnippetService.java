package io.snipper.snippet.service;

import io.snipper.snippet.model.Snippet;
import io.snipper.snippet.model.User;
import io.snipper.snippet.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnippetService {
    final SnippetRepository snippetRepository;

    @Autowired
    public SnippetService(final SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    public Snippet getSnippetById(final Long id) {
        try {
            final Snippet response = snippetRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("snippet not found"));
            return response;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Snippet> getSnippets() {
        final List<Snippet> response = snippetRepository.findAll();
        return response;
    }

    public List<Snippet> getSnippetsByOwnerId(final Long ownerId) {
        final List<Snippet> response = snippetRepository.findAllByOwnerId(ownerId);
        return response;
    }

    public void postSnippet(final Snippet snippet) {
        snippetRepository.save(snippet);
    }

    public void deleteSnippet(final Snippet snippet) {
        snippetRepository.delete(snippet);
    }
}
