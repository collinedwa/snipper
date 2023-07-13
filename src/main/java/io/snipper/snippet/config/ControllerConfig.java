package io.snipper.snippet.config;

import com.google.gson.*;
import io.snipper.snippet.model.Snippet;
import io.snipper.snippet.model.User;
import io.snipper.snippet.repository.SnippetRepository;
import io.snipper.snippet.service.EncryptionService;
import io.snipper.snippet.service.SnippetService;
import io.snipper.snippet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.HashMap;

@Configuration
public class ControllerConfig {
    final UserService userService;
    final SnippetService snippetService;
    final EncryptionService encryptionService;

    @Autowired
    public ControllerConfig(final UserService userService,
                            final SnippetService snippetService,
                            final EncryptionService encryptionService) {
        this.userService = userService;
        this.snippetService = snippetService;
        this.encryptionService = encryptionService;
    }

    @Bean
    public HashMap<Long, User> userMap() {
        final Gson gson = new Gson();
        final HashMap<Long, User> map = new HashMap<>();
        final JsonParser jsonParser = new JsonParser();
        try {
            final JsonArray arr = (JsonArray) jsonParser
                    .parse(new FileReader("src/main/resources/seed/userSeedData.json"));

            for (JsonElement node : arr) {
                final User user = gson.fromJson(node, User.class);
                final String hashedPass = user.hashPassword();
                user.setPassword(hashedPass);
                userService.createUser(user);
                map.put(user.getId(), user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }

    @Bean
    public HashMap<Long, Snippet> snippetMap() {
        final Gson gson = new Gson();
        final HashMap<Long, Snippet> map = new HashMap<>();
        final JsonParser jsonParser = new JsonParser();
        try {
            final JsonArray arr = (JsonArray) jsonParser
                    .parse(new FileReader("src/main/resources/seed/snippetSeedData.json"));

            for (JsonElement node : arr) {
                final Snippet snippet = gson.fromJson(node, Snippet.class);
                final String code = snippet.getCode();
                snippet.setCode(encryptionService.encrypt(code, encryptionService.secretKey));
                snippetService.postSnippet(snippet);
                map.put(snippet.getId(), snippet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }
}
