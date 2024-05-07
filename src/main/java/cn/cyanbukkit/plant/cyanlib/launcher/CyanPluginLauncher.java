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

/**
 * 嵌套框架
 */

public class CyanPluginLauncher extends JavaPlugin {

    public static CyanPluginLauncher cyanPlugin;

    public CyanPluginLauncher() {
        cyanPlugin = this;
        KotlinBootstrap.init();
        // xyz.xenondevs:particle:1.8.4
        KotlinBootstrap.loadDepend("xyz.xenondevs","particle","1.8.4");
    }

    public File arena;
    public YamlConfiguration arenaConfig;
    private File data;
    public YamlConfiguration dataConfig;

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
        try {
            loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PlantSugarBeet.INSTANCE.load();
        registerCommand(MainTestCommand.INSTANCE);
        registerCommand(SetUpCommand.INSTANCE);
    }


    public void loadData() throws IOException {
        data = new File("plugins/ModuleGame-Bukkit/addon/"+ getDescription().getName() +"/cache.yml");
        getLogger().info("加载: " + data.getAbsolutePath());
        if (!data.exists()) {
            if (data.createNewFile()) {
                getLogger().info("创建一个新的文件: " + data.getName());
            } else {
                getLogger().info("错误加载: " + data.getName());
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(data);
        dataConfig.getKeys(false).forEach(key -> {
            getLogger().info("Load data: " + key + " -> " + dataConfig.get(key));
        });
    }

    private void loadConfig() {
        arena = new File("plugins/ModuleGame-Bukkit/addon/"+ getDescription().getName() +"/arena.yml");
        if (!arena.exists()) {
            try {
                if(arena.getParentFile().mkdirs()) {
                    InputStream is = getResource("arena.yml");
                    if (is != null) {
                        Files.copy(is, arena.toPath());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        arenaConfig = YamlConfiguration.loadConfiguration(arena);
    }


}