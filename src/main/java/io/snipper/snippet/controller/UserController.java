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
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final AuthorizationService authorizationService;
    private final EncryptionService encryptionService;
    private final SnippetService snippetService;
    final UserService userService;

    @Autowired
    public UserController(final AuthorizationService authorizationService,
                          final EncryptionService encryptionService,
                          final SnippetService snippetService,
                          final UserService userService) {
        this.authorizationService = authorizationService;
        this.encryptionService = encryptionService;
        this.snippetService = snippetService;
        this.userService = userService;
    }

    @GetMapping("/users")
    public Object getUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping(path = "/users/{id}", produces = "application/json")
    public Object getSingleUser(@PathVariable(name="id") final Long id,
                                @RequestHeader(name="Authorization") final String jwtToken) {
        final User matchingUser = userService.getUserById(id);

        if (matchingUser != null) {
            final boolean isAuthorized = authorizationService.validateToken(jwtToken, id.toString());
            return isAuthorized ? ResponseEntity.ok(matchingUser) : ResponseEntity.status(401);
        } else {
            return ResponseEntity.badRequest();
        }
    }

    @GetMapping(path = "/users/{id}/snippets", produces = "application/json")
    public Object getUserSnippets(@PathVariable(name="id") final Long id,
                                  @RequestHeader(name="Authorization") final String jwtToken) throws Exception {
        final User matchingUser = userService.getUserById(id);

        if (matchingUser != null) {
            final boolean isAuthorized = authorizationService.validateToken(jwtToken, id.toString());
            if (!isAuthorized) return ResponseEntity.status(401);
            final List<Snippet> snippets = snippetService.getSnippetsByOwnerId(id);
            for (final Snippet snippet : snippets) {
                final String code = snippet.getCode();
                snippet.setCode(encryptionService.decrypt(code, encryptionService.secretKey));
            }
            return ResponseEntity.ok(snippets);
        } else {
            return ResponseEntity.badRequest();
        }
    }

    @PostMapping(path = "/users", consumes = "application/json", produces = "application/json")
    public Object postUser(@Valid @RequestBody final User user) {
        final User matchingUser = userService.getUserByEmail(user.getEmail());

        if (matchingUser == null) {
            user.setPassword(user.hashPassword());
            userService.createUser(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest();
        }
    }

    @PostMapping(path = "/users/login", consumes = "application/json", produces = "application/json")
    public Object loginUser(@Valid @RequestBody final UserLogin user) {
        final User matchingUser = userService.getUserByEmail(user.getEmail());

        if (matchingUser != null && matchingUser.checkPassword(user.getPassword())) {
            this.authorizationService.generateToken(matchingUser);
            return ResponseEntity.ok("logged in");
        } else {
            return ResponseEntity.badRequest();
        }
    }
}
