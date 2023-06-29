package io.snipper.snippet.controller;

import io.snipper.snippet.model.Snippet;
import io.snipper.snippet.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SnippetController {
    private Map<String, User> userMap;
    private Map<String, Snippet> snippetMap;

    @Autowired
    public SnippetController(final Map<String, User> userMap,
                             final Map<String, Snippet> snippetMap) {
        this.userMap = userMap;
        this.snippetMap = snippetMap;
    }

    @GetMapping(path = "/snippets", produces = "application/json")
    public ResponseEntity<Collection<Snippet>> getSnippets(@RequestParam(required = false) final String language) {
        if (language != null) {
            return ResponseEntity.ok(snippetMap.values()
                    .stream()
                    .filter(snippet -> language.toLowerCase().equals(snippet.getLanguage().toLowerCase()))
                    .toList()
            );
        }
        return ResponseEntity.ok(snippetMap.values());
    }

    @GetMapping(path = "/snippets/{id}", produces = "application/json")
    public ResponseEntity<Snippet> getSingleSnippet(@PathVariable(name="id") final String id) {
        return ResponseEntity.ok(snippetMap.get(id));
    }

    @PostMapping(path = "/snippets", consumes = "application/json", produces = "application/json")
    public Object postSnippet(@Valid @RequestBody final Snippet snippet) {
        if (snippetMap.get(snippet.getId()) == null) {
            snippetMap.put(snippet.getId(), snippet);
            return ResponseEntity.ok(snippet);
        } else {
            return ResponseEntity.badRequest();
        }
    }

    @DeleteMapping(path = "/snippets/{id}", produces = "application/json")
    public ResponseEntity<String> deleteSingleSnippet(@PathVariable(name="id") final String id) {
        if (snippetMap.get(id) != null) {
            snippetMap.remove(id);
            return ResponseEntity.ok("deleted snippet");
        } else {
            return ResponseEntity.ok("snippet does not exist");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getUsers() {
        return ResponseEntity.ok(userMap.values());
    }

    @GetMapping(path = "/users/{id}", produces = "application/json")
    public ResponseEntity<User> getSingleUser(@PathVariable(name="id") final String id) {
        return ResponseEntity.ok(userMap.get(id));
    }

    @PostMapping(path = "/users", consumes = "application/json", produces = "application/json")
    public Object postUser(@Valid @RequestBody final User user) {
        if (userMap.get(user.getId()) == null) {
            userMap.put(user.getId(), user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest();
        }
    }


}
