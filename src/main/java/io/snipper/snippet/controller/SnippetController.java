package io.snipper.snippet.controller;

import io.snipper.snippet.model.Snippet;
import io.snipper.snippet.model.User;
import io.snipper.snippet.model.UserLogin;
import io.snipper.snippet.service.AuthorizationService;
import io.snipper.snippet.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SnippetController {
    private final Map<String, User> userMap;
    private final Map<String, Snippet> snippetMap;
    private final AuthorizationService authorizationService;
    private final EncryptionService encryptionService;


    @Autowired
    public SnippetController(final Map<String, User> userMap,
                             final Map<String, Snippet> snippetMap,
                             final AuthorizationService authorizationService,
                             final EncryptionService encryptionService) {
        this.userMap = userMap;
        this.snippetMap = snippetMap;
        this.authorizationService = authorizationService;
        this.encryptionService = encryptionService;
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
    public ResponseEntity<Snippet> getSingleSnippet(@PathVariable(name="id") final String id) throws Exception {
        if (snippetMap.containsKey(id)) {
            final Snippet snippet = snippetMap.get(id);
            final String code = (String) snippet.getCode();
            final String secureCode = encryptionService.decrypt(code, encryptionService.secretKey);
            snippet.setCode(secureCode);
            return ResponseEntity.ok(snippet);
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @PostMapping(path = "/snippets", consumes = "application/json", produces = "application/json")
    public Object postSnippet(@Valid @RequestBody final Snippet snippet) throws Exception {
        if (snippetMap.get(snippet.getId()) == null) {
            final String code = (String) snippet.getCode();
            final String secureCode =  encryptionService.encrypt(code, encryptionService.secretKey);
            snippet.setCode(secureCode);
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
    public Object getSingleUser(@PathVariable(name="id") final String id) {
        final List<User> users = userMap.values()
                .stream()
                .filter(account -> account.getId().equals(id))
                .collect(Collectors.toList());

        if (!users.isEmpty() ) {
            final User matchingUser = users.get(0);
            final boolean isAuthorized = authorizationService.validateToken(matchingUser);
            if (isAuthorized) {
                return ResponseEntity.ok(matchingUser);
            } else {
                return ResponseEntity.status(401);
            }
        }
        return ResponseEntity.badRequest();
    }

    @PostMapping(path = "/users", consumes = "application/json", produces = "application/json")
    public Object postUser(@Valid @RequestBody final User user) {
        final List<User> users = userMap.values()
                .stream()
                .filter(account -> account.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            user.setPassword(user.hashPassword());
            userMap.put(user.getId(), user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest();
        }
    }

    @PostMapping(path = "/users/login", consumes = "application/json", produces = "application/json")
    public Object loginUser(@Valid @RequestBody final UserLogin user) {
        final List<User> users = userMap.values()
                .stream()
                .filter(account -> account.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList());

        if (!users.isEmpty()) {
            final User matchingUser = users.get(0);
            if (matchingUser.checkPassword(user.getPassword())) {
                this.authorizationService.generateToken(matchingUser);
                return ResponseEntity.ok("logged in");
            }
        }
        return ResponseEntity.badRequest();
    }
}
