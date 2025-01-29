package io.github.bigcookie233.meirin;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import io.github.bigcookie233.meirin.entity.QueryResult;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class PlayerListener {
    private HttpClient client;
    private Gson gson;
    private WhitelistConfig config;

    @Inject
    public PlayerListener(WhitelistConfig config) {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
        this.config = config;
    }

    @Subscribe
    public void onPlayerJoin(LoginEvent event) {
        QueryResult result = this.queryPlayer(event.getPlayer());
        if (result == null || result.getMessageType() == null) {
            event.setResult(ResultedEvent.ComponentResult.denied(MiniMessage.miniMessage().deserialize(this.config.errorMsg)));
        } else if (!result.isAllowed()) {
            Component message;
            switch (result.getMessageType()) {
                case MINI_MESSAGE -> message = MiniMessage.miniMessage().deserialize(result.getMessage());
                case JSON_MESSAGE -> message = GsonComponentSerializer.gson().deserialize(result.getMessage());
                default -> message = MiniMessage.miniMessage().deserialize(this.config.errorMsg);
            }
            event.setResult(ResultedEvent.ComponentResult.denied(message));
        }
    }

    private QueryResult queryPlayer(Player player) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(this.config.endpoint))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("username=%s&secret=%s",
                            URLEncoder.encode(player.getUsername(), StandardCharsets.UTF_8),
                            URLEncoder.encode(this.config.secretKey, StandardCharsets.UTF_8))))
                    .build();

            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            QueryResult result = this.gson.fromJson(responseBody, QueryResult.class);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
