package cn.cyanbukkit.plant

import cn.cyanbukkit.plant.command.BindListener
import cn.cyanbukkit.plant.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.plant.effect.Region
import cn.cyanbukkit.plant.effect.SprinklerHead
import cn.cyanbukkit.plant.effect.TNTEffect
import cn.cyanbukkit.plant.hook.PlaceholderExpands
import cn.cyanbukkit.plant.hook.PsbTemplate
import cn.cyanbukkit.plant.hook.StatusPapi
import cn.cyanbukkit.plant.listener.BuiltListen
import cn.cyanbukkit.plant.listener.PlayerHandle
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

/**
 * 相当于主类所有加载数据的活
 */
object PlantSugarBeet {

    var spawnLocation: String = ""
    var landBlockY = 0
    var maxTemplate = 0
    var walkDirection: String = ""
    lateinit var region: Region
    lateinit var sprinklerHead : ItemStack

    fun regionIsInitialized() = this::region.isInitialized
    fun sprinklerHeadIsInitialized() = this::sprinklerHead.isInitialized

    fun load() {
        cyanPlugin.arenaConfig.options().copyDefaults(true)
        cyanPlugin.arenaConfig.addDefault("landY", 63)
        cyanPlugin.arenaConfig.addDefault("KeepTime", 15)
        cyanPlugin.arenaConfig.addDefault("MaxTemplate", 5)
        cyanPlugin.arenaConfig.addDefault("Direction", "x+")
        cyanPlugin.arenaConfig.addDefault("Spawn", "minecraft,0,200,0,0,0")
        cyanPlugin.arenaConfig.addDefault("BreakFixedHandle", listOf("463|5|1.5")) // 礼物 执行多少遍
        cyanPlugin.arenaConfig.options().copyDefaults(true)
        cyanPlugin.arenaConfig.options().copyHeader(true)
        cyanPlugin.arenaConfig.options().header("这是一个种甜菜的插件\n" +
                "landY: 甜菜种植的高度\n" +
                "KeepTime: 保持时间\n" +
                "MaxTemplate: 最大完成数量数\n" +
                "Direction: 破坏/帮忙种植的行走放行\n" +
                "BreakFixedHandle: 固定破坏触发仅礼物专用不然破坏不计数 (格式 礼物id|执行次数|速度)")
        cyanPlugin.arenaConfig.save(cyanPlugin.arena)
        cyanPlugin.logger.info("注意：这里玩法破坏是在addon中设置而不是本体Js ！")

        spawnLocation = cyanPlugin.arenaConfig.getString("Spawn")
        landBlockY = cyanPlugin.arenaConfig.getInt("landY")
        maxTemplate = cyanPlugin.arenaConfig.getInt("MaxTemplate")
        walkDirection = cyanPlugin.arenaConfig.getString("Direction") ?: "x+"

        val block1 = (cyanPlugin.arenaConfig.get("PlantRegion.pos1") as Location)
        val block2 = (cyanPlugin.arenaConfig.get("PlantRegion.pos2") as Location)
        region = Region(block1, block2)
        if (cyanPlugin.arenaConfig.contains("SprinklerHead")) {
            sprinklerHead = cyanPlugin.arenaConfig.getItemStack("SprinklerHead")
        }

        cyanPlugin.server.pluginManager.registerEvents(PlayerHandle, cyanPlugin)
        cyanPlugin.server.pluginManager.registerEvents(TNTEffect, cyanPlugin)
        cyanPlugin.server.pluginManager.registerEvents(BindListener, cyanPlugin)
        cyanPlugin.server.pluginManager.registerEvents(SprinklerHead, cyanPlugin)
        cyanPlugin.server.pluginManager.registerEvents(BuiltListen, cyanPlugin)
        PlaceholderExpands.register()
        StatusPapi.register()
        PsbTemplate.register()
        status()
    }

    fun String.toLocation(): Location? {
        try {
            val split = this.split(",")
            return Location(cyanPlugin.server.getWorld(split[0]),
                split[1].toDouble(), split[2].toDouble(), split[3].toDouble(), split[4].toFloat(), split[5].toFloat())
        } catch (e: Exception) {
            return null
        }
    }


    fun default(p: Player) {
        val location = spawnLocation.toLocation()
        if (location != null) {
            p.teleport(location)
        }
        p.gameMode = GameMode.SURVIVAL
        p.inventory.clear()
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).baseValue = 1.0
        p.allowFlight = true
        region.default()
        if (regionIsInitialized()) {
            p.inventory.addItem(sprinklerHead)
            p.inventory.addItem(ItemStack(Material.BEETROOT_SEEDS, 64))
            p.inventory.addItem(ItemStack(Material.INK_SACK,64).apply {
                durability = 15
            })
            p.inventory.addItem(ItemStack(Material.DIAMOND_SWORD).apply {
                val i =itemMeta.apply {
                    isUnbreakable = true
                }
                itemMeta = i
            })
        }
    }



    var templete = 0
    val KEEP = cyanPlugin.arenaConfig.getInt("KeepTime")
    var keepTime = KEEP
    var checking = false

    private fun status() {
        Bukkit.getConsoleSender().sendMessage("§a[种甜菜] 插件已经加载统计")
        object : BukkitRunnable() {
            override fun run() {
                if(Bukkit.getOnlinePlayers().isEmpty()) return
                if (region.getPlantIsSuccess().size >= region.getsize()) {
                    if (checking) {
                        keepTime--
                        Bukkit.getOnlinePlayers().forEach {
                            it.sendTitle("加油", "§a还剩${keepTime}秒", 10, 20, 10)
                        }
                        if (keepTime <= 0) {
                            checking = false
                            keepTime = KEEP
                            templete++
                            Bukkit.getOnlinePlayers().forEach {
                                it.sendTitle("完成一个回合", "§a已经成功完成${templete}回合", 10, 20, 10)
                            }
                            Bukkit.getScheduler().runTaskLater(cyanPlugin, {
                                nextTemplate()
                            }, 0)
                        }
                    } else {
                        checking = true
                        Bukkit.getOnlinePlayers().forEach {
                            it.sendTitle("", "§a加油离成功就差${KEEP}秒了", 10, 20, 10)
                        }
                    }
                } else {
                    if (checking) {
                        checking = false
                        keepTime = KEEP
                        Bukkit.getOnlinePlayers().forEach {
                            it.sendTitle("§c哎呀", "§f没有坚持住~", 10, 20, 10)
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(cyanPlugin, 0L, 20L)
    }

    private fun nextTemplate() {
        region.default()
    }

}