package io.snipper.snippet.controller.config;

import com.google.gson.*;
import io.snipper.snippet.model.Snippet;
import io.snipper.snippet.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.HashMap;

@Component
public class ControllerConfig {

    @Bean
    public HashMap<String, User> userMap() {
        final Gson gson = new Gson();
        final HashMap<String, User> map = new HashMap<>();
        final JsonParser jsonParser = new JsonParser();
        try {
            final JsonArray arr = (JsonArray) jsonParser
                    .parse(new FileReader("src/main/resources/seed/userSeedData.json"));

            for (JsonElement node : arr) {
                final User user = gson.fromJson(node, User.class);
                map.put(user.getId(), user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }

    @Bean
    public HashMap<String, Snippet> snippetMap() {
        final Gson gson = new Gson();
        final HashMap<String, Snippet> map = new HashMap<>();
        final JsonParser jsonParser = new JsonParser();
        try {
            final JsonArray arr = (JsonArray) jsonParser
                    .parse(new FileReader("src/main/resources/seed/snippetSeedData.json"));

            for (JsonElement node : arr) {
                final Snippet snippet = gson.fromJson(node, Snippet.class);
                map.put(snippet.getId(), snippet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }
}
