package io.snipper.snippet.controller;

import io.snipper.snippet.model.Snippet;
import io.snipper.snippet.model.User;
import io.snipper.snippet.model.UserLogin;
import io.snipper.snippet.service.AuthorizationService;
import io.snipper.snippet.service.EncryptionService;
import io.snipper.snippet.service.SnippetService;
import io.snipper.snippet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SnippetController {
    private final Map<Long, User> userMap;
    private final Map<Long, Snippet> snippetMap;
    private final AuthorizationService authorizationService;
    private final EncryptionService encryptionService;
    private final SnippetService snippetService;
    final UserService userService;

    @Autowired
    public SnippetController(final Map<Long, User> userMap,
                             final Map<Long, Snippet> snippetMap,
                             final AuthorizationService authorizationService,
                             final EncryptionService encryptionService,
                             final SnippetService snippetService,
                             final UserService userService) {
        this.userMap = userMap;
        this.snippetMap = snippetMap;
        this.authorizationService = authorizationService;
        this.encryptionService = encryptionService;
        this.snippetService = snippetService;
        this.userService = userService;
    }

    @GetMapping(path = "/snippets", produces = "application/json")
    public ResponseEntity<Collection<Snippet>> getSnippets(@RequestParam(required = false) final String language) {
        if (language != null) {
            return ResponseEntity.ok(snippetService.getSnippets()
                    .stream()
                    .filter(snippet -> language.toLowerCase().equals(snippet.getLanguage().toLowerCase()))
                    .toList()
            );
        }
        return ResponseEntity.ok(snippetService.getSnippets());
    }

    @GetMapping(path = "/snippets/{id}", produces = "application/json")
    public Object getSingleSnippet(@PathVariable(name="id") final Long id,
                                   @RequestHeader(name="Authorization") final String jwtToken) throws Exception {
        final Long userId = authorizationService.retrieveUserId(jwtToken);
        final Snippet snippet = snippetService.getSnippetById(id);
        final User user = userService.getUserById(userId);
        final boolean isAuthorized = (authorizationService.validateToken(jwtToken)
                && authorizationService.validateOwnership(snippet, user));

        if (snippet != null) {
            final String code = snippet.getCode();
            snippet.setCode(encryptionService.decrypt(code, encryptionService.secretKey));

            return isAuthorized ? ResponseEntity.ok(snippet) : ResponseEntity.status(401);
        } else {
            return ResponseEntity.badRequest();
        }
    }

    @PostMapping(path = "/snippets", consumes = "application/json", produces = "application/json")
    public Object postSnippet(@Valid @RequestBody final Snippet snippet,
                              @RequestHeader(name="Authorization") final String jwtToken) throws Exception {

        if (authorizationService.validateToken(jwtToken)) {
            final String code = snippet.getCode();
            final String secureCode = encryptionService.encrypt(code, encryptionService.secretKey);
            final Long userId = authorizationService.retrieveUserId(jwtToken);
            snippet.setCode(secureCode);
            snippet.setOwnerId(userId);
            snippetService.postSnippet(snippet);

            return ResponseEntity.ok(snippet);
        } else {
            return ResponseEntity.status(401);
        }
    }

    @DeleteMapping(path = "/snippets/{id}", produces = "application/json")
    public Object deleteSingleSnippet(@PathVariable(name="id") final Long id,
                                      @RequestHeader(name="Authorization") final String jwtToken) {
        final Long userId = authorizationService.retrieveUserId(jwtToken);
        final Snippet snippet = snippetService.getSnippetById(id);
        final User user = userService.getUserById(userId);
        final boolean isAuthorized = authorizationService.validateOwnership(snippet, user);

        if (snippet != null) {
            if (isAuthorized) snippetService.deleteSnippet(snippet);
            return isAuthorized ? ResponseEntity.ok("deleted snippet") : ResponseEntity.status(401);
        } else {
            return ResponseEntity.badRequest();
        }
    }
}
