package io.snipper.snippet.controller;

import io.snipper.snippet.model.Snippet;
import io.snipper.snippet.model.User;
import io.snipper.snippet.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SnippetController {
    private final Map<String, User> userMap;
    private final Map<String, Snippet> snippetMap;
    private final Cipher cipher;
    private final KeyGenerator keyGenerator;
    private final SecretKey secretKey;


    @Autowired
    public SnippetController(final Map<String, User> userMap,
                             final Map<String, Snippet> snippetMap) throws Exception {
        this.userMap = userMap;
        this.snippetMap = snippetMap;
        this.cipher = Cipher.getInstance("AES");
        this.keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        this.secretKey = keyGenerator.generateKey();
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
            final String secureCode = decrypt(code, this.secretKey);
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
            final String secureCode =  encrypt(code, this.secretKey);
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
    public Object getSingleUser(@PathVariable(name="id") final String id,
                                              @Valid @RequestBody final UserLogin user) {
        final List<User> users = userMap.values()
                .stream()
                .filter(account -> account.getEmail().equals(user.getEmail()) && account.getId().equals(id))
                .collect(Collectors.toList());

        if (!users.isEmpty() ) {
            final User matchingUser = users.get(0);
            if (matchingUser.checkPassword(user.getPassword())) {
                return ResponseEntity.ok(matchingUser);
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
                matchingUser.setLoggedIn(true);
                userMap.put(matchingUser.getId(), matchingUser);
                return ResponseEntity.ok("logged in");
            }
        }
        return ResponseEntity.badRequest();
    }

    public String encrypt(String plainText, SecretKey secretKey)
            throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    public String decrypt(String encryptedText, SecretKey secretKey)
            throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }


}
