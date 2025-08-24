package dev.onebiteaidan.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Config {

    private final Plugin plugin;
    private final File folder;                  // Usually plugin.getDataFolder() or a subfolder
    private final String fileName;              // must include ".yml"
    private final boolean createIfMissing;
    private final boolean hasResourceDefault;   // True if a default configuration with the same name exists in /resources

    private File file;
    private YamlConfiguration config;

    /**
     * @param plugin                your JavaPlugin
     * @param folder                where to place the file (e.g. plugin.getDataFolder())
     * @param baseNameNoExt         config base name; ".yml" will be appended
     * @param createIfMissing       create file if missing
     * @param hasResourceDefault    if true, copy defaults from plugin resources on first run
     */
    public Config(Plugin plugin,
                  File folder,
                  String baseNameNoExt,
                  boolean createIfMissing,
                  boolean hasResourceDefault) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.folder = Objects.requireNonNull(folder, "folder");
        this.fileName = (baseNameNoExt.endsWith(".yml") ? baseNameNoExt : baseNameNoExt + ".yml");
        this.createIfMissing = createIfMissing;
        this.hasResourceDefault = hasResourceDefault;
    }

    public synchronized void init() {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        file = new File(folder, fileName);

        if (!file.exists() && createIfMissing) {
            if (hasResourceDefault && plugin.getResource(fileName) != null) {
                // Copies from /resources while preserving if file exists
                plugin.saveResource(fileName, false);
            } else {
                try {
                    // Create an empty file
                    file.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().severe("Failed to create " + file.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }

        // Load user config
        config = YamlConfiguration.loadConfiguration(file);

        // If we have a bundled default, set it as default so copyDefaults works
        if (hasResourceDefault) {
            try (InputStream in = plugin.getResource(fileName)) {
                if (in != null) {
                    try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                        YamlConfiguration defaults = YamlConfiguration.loadConfiguration(reader);
                        config.setDefaults(defaults);
                        // If you want missing keys to be written out to disk:
                        config.options().copyDefaults(true);
                        save(); // write defaults that weren't in the file
                    }
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Unable to load defaults for " + fileName + ": " + e.getMessage());
            }
        }
    }

    /**
     * Reload from disk (keeps default if present).
     */
    public synchronized void reload() {
        if (file == null) init(); // lazy init
        YamlConfiguration fresh = YamlConfiguration.loadConfiguration(file);
        if (config != null && config.getDefaults() != null) {
            fresh.setDefaults(config.getDefaults());
            fresh.options().copyDefaults(config.options().copyDefaults());
        }
        config = fresh;
    }

    /**
     * Save to disk
     */
    public synchronized void save() {
        if (file == null || config == null) return;
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save " + file.getAbsolutePath() + ": " + e.getMessage());
        }
    }

    public synchronized FileConfiguration getConfig() {
        if (config == null) init();
        return config;
    }

    public synchronized File getFile() {
        if (file == null) init();
        return file;
    }

    // Convenience getters (So you don't have to call <ConfigObject>.getConfig.getString(path))
    public String getString(String path)    { return getConfig().getString(path); }
    public int getInt(String path)          { return getConfig().getInt(path); }
    public boolean getBoolean(String path)  { return getConfig().getBoolean(path); }
    public double getDouble(String path)    { return getConfig().getDouble(path); }

    // Convenience getters with defaults
    public String getString(String path, String def)      { return getConfig().getString(path, def); }
    public int getInt(String path, int def)               { return getConfig().getInt(path, def); }
    public boolean getBoolean(String path, boolean def)   { return getConfig().getBoolean(path, def); }
    public double getDouble(String path, double def)      { return getConfig().getDouble(path, def); }
}