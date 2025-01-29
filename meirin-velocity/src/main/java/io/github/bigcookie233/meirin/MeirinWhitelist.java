package io.github.bigcookie233.meirin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(
        id = "meirin",
        name = "Meirin Whitelist",
        version = "1.0-SNAPSHOT",
        authors = {"Bigcookie233"}
)
public class MeirinWhitelist {
    private final ProxyServer server;
    private final PlayerListener listener;

    @Inject
    public MeirinWhitelist(ProxyServer server, PlayerListener listener) {
        this.server = server;
        this.listener = listener;
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        server.getEventManager().register(this, this.listener);
    }
}
