package cn.cyanbukkit.plant.cyanlib.launcher;

import cn.cyanbukkit.plant.PlantSugarBeet;
import cn.cyanbukkit.plant.command.MainTestCommand;
import cn.cyanbukkit.plant.command.SetUpCommand;
import cn.cyanbukkit.plant.cyanlib.loader.KotlinBootstrap;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 嵌套框架
 */

public class CyanPluginLauncher extends JavaPlugin {

    public static CyanPluginLauncher cyanPlugin;
    public File arena;
    public YamlConfiguration arenaConfig;
    public YamlConfiguration dataConfig;
    private File data;
    public CyanPluginLauncher() {
        cyanPlugin = this;
        KotlinBootstrap.init();
        // xyz.xenondevs:particle:1.8.4
        KotlinBootstrap.loadDepend("xyz.xenondevs", "particle", "1.8.4");
    }

    public void saveData() {
        try {
            dataConfig.save(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void registerCommand(Command command) {
        Class<?> pluginManagerClass = cyanPlugin.getServer().getPluginManager().getClass();
        try {
            Field field = pluginManagerClass.getDeclaredField("commandMap");
            field.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) field.get(cyanPlugin.getServer().getPluginManager());
            commandMap.register(cyanPlugin.getName(), command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad() {
    }


    @Override
    public void onEnable() {
        loadConfig();
        PlantSugarBeet.INSTANCE.load();
        registerCommand(MainTestCommand.INSTANCE);
        registerCommand(SetUpCommand.INSTANCE);
    }


    private void loadConfig() {
        try {
            // 创建 arena.yml 文件
            Path arenaPath = Paths.get("plugins/SiModuleGame/addon", getDescription().getName(), "arena.yml");
            Files.createDirectories(arenaPath.getParent());
            if (Files.notExists(arenaPath)) {
                InputStream is = getResource("arena.yml");
                if (is != null) {
                    Files.copy(is, arenaPath);
                }
            }
            arenaConfig = YamlConfiguration.loadConfiguration(arenaPath.toFile());
            // 创建 cache.yml 文件
            Path dataPath = Paths.get("plugins/SiModuleGame/addon", getDescription().getName(), "cache.yml");
            Files.createDirectories(dataPath.getParent());
            if (Files.notExists(dataPath)) {
                Files.createFile(dataPath);
                getLogger().info("创建一个新的文件: " + dataPath.getFileName());
            } else {
                getLogger().info("加载: " + dataPath.toAbsolutePath());
            }
            dataConfig = YamlConfiguration.loadConfiguration(dataPath.toFile());

            // 加载数据
            dataConfig.getKeys(false).forEach(key -> {
                getLogger().info("Load data: " + key + " -> " + dataConfig.get(key));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}