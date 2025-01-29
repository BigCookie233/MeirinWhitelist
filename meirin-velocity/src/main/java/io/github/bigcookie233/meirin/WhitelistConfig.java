package io.github.bigcookie233.meirin;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class WhitelistConfig {
    private final Path dataFolder;
    private final Path configPath;
    public final String secretKey;
    public final String endpoint;
    public final String errorMsg;

    @Inject
    public WhitelistConfig(@DataDirectory Path dataDirectory) {
        this.dataFolder = dataDirectory;
        this.configPath = dataDirectory.resolve("config.toml");

        try {
            saveDefaultConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Toml toml = loadConfig();

        this.secretKey = toml.getString("api.secret-key");
        this.endpoint = toml.getString("api.endpoint");
        this.errorMsg = toml.getString("messages.error-message");
    }

    private void saveDefaultConfig() throws IOException {
        if (Files.notExists(dataFolder)) Files.createDirectory(dataFolder);
        if (Files.exists(configPath)) return;

        try (InputStream in = WhitelistConfig.class.getResourceAsStream("/config.toml")) {
            assert in != null;
            Files.copy(in, configPath);
        }
    }

    private File configFile() {
        return configPath.toFile();
    }

    private Toml loadConfig() {
        return new Toml().read(configFile());
    }
}
