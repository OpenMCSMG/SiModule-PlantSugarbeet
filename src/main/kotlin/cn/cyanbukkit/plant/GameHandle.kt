package cn.cyanbukkit.plant

import cn.cyanbukkit.plant.PlantSugarBeet.region
import cn.cyanbukkit.plant.PlantSugarBeet.templete
import cn.cyanbukkit.plant.PlantSugarBeet.walkDirection
import cn.cyanbukkit.plant.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.plant.effect.Line
import cn.cyanbukkit.plant.effect.SprinklerHead.getEdge
import cn.cyanbukkit.plant.effect.SprinklerHead.plantCrop
import cn.cyanbukkit.plant.effect.TNTEffect
import cn.cyanbukkit.plant.effect.TNTEffect.fallTNT
import cn.cyanbukkit.plant.effect.TNTEffect.randomBoolean
import cn.cyanbukkit.plant.noise.SpawnEntity
import cn.cyanbukkit.plant.noise.SpawnEntity.breakBlock
import cn.cyanbukkit.plant.noise.Walk
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect

class GameHandle {

    fun sheepFix(amount: Int) {
        getOnlinePlayers().forEach {
            it.sendTitle("§c羊驼削帮简单修", "§9嗯", 10, 40, 10)
        }
        PlantSugarBeet.templete += amount
        for (i in 0 until amount) {
            object : BukkitRunnable() {
                override fun run() {
                    for (lin in region.line(walkDirection)) {
                        val sheep = SpawnEntity.spawnLlama(lin.start)
                        if (Math.random() > 0.5) {
                            sheep.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, false, false))
                        }
                        Walk(sheep, { entity ->
                            val loc = entity.location
                            if (region.planting().contains(loc.block)) {
                                plantCrop(loc.block)
                            }
                        }, lin.start, lin.end).walkUseTP(1.0)
                    }
                }
            }.runTaskLater(cyanPlugin, (i * 20).toLong())
        }
    }

    // 生成岩浆史莱姆
    fun magmaSlimeBreak(amount: Int, userName: String, edgeSize: Int = 3) {
        getOnlinePlayers().forEach {
            it.sendTitle("§c一块岩浆史莱姆", "§9", 10, 40, 10)
        }
        for (i in 0 until amount) {
            object : BukkitRunnable() {
                override fun run() {
                    val lin = region.line(walkDirection).random()
                    val has = cyanPlugin.dataConfig.getInt("GameStatus.${userName}", 0)
                    cyanPlugin.dataConfig.set(
                        "GameStatus.${userName}",
                        has + (edgeSize * 2 * lin.getLineHasBlockSize())
                    )
                    cyanPlugin.saveData()
                    val slime = SpawnEntity.spawnMagmaSlime(lin.start, edgeSize)
                    Walk(slime, { entity ->
                        val middle = entity.location.block
                        middle.getEdge(edgeSize - 1).forEach {
                            it.breakBlock()
                        }
                    }, lin.start, lin.end).walkUseTP(1.0)

                }
            }.runTaskLater(cyanPlugin, (i * 20).toLong())
        }
    }

    // 贪吃鸟
    fun greedyBird(amount: Int, userName: String) {
        getOnlinePlayers().forEach {
            it.sendTitle("§c贪吃鸟", "§9", 10, 40, 10)
        }
        for (i in 0 until amount) {
            object : BukkitRunnable() {
                override fun run() {
                    val lin = region.line(walkDirection).random()
                    val has = cyanPlugin.dataConfig.getInt("GameStatus.${userName}", 0)
                    cyanPlugin.dataConfig.set("GameStatus.${userName}", has + (3 * lin.getLineHasBlockSize()))
                    cyanPlugin.saveData()
                    val slime = SpawnEntity.spawnParrot(lin.start)
                    Walk(slime, { entity ->
                        val middle = entity.location
                        ParticleBuilder(ParticleEffect.VILLAGER_ANGRY, middle.add(0.5, 0.5, 0.5))
                            .setSpeed(0.1f)
                            .setAmount(8)
                            .display()
                        middle.block.getEdge(1).forEach {
                            it.breakBlock()
                        }
                    }, lin.start, lin.end).walkUseTP(1.0)
                }
            }.runTaskLater(cyanPlugin, (i * 20).toLong())
        }
    }

    // TNT劈叉
    fun tntFart(amount: Int) {
        getOnlinePlayers().forEach {
            it.sendTitle("§cTNT劈叉", "§9", 10, 40, 10)
        }
        for (i in 0 until amount) {
            object : BukkitRunnable() {
                override fun run() {
                    getOnlinePlayers().forEach {
                        TNTEffect.fartSpawnTNT(it.location, 3)
                    }
                }
            }.runTaskLater(cyanPlugin, (i * 20).toLong())
        }
    }

    // 村民帮放种子
    fun villagerHelp(amount: Int) {
        getOnlinePlayers().forEach {
            it.sendTitle("§c淳朴村民", "§9嗯", 10, 40, 10)
        }
        PlantSugarBeet.templete += amount
        for (i in 0 until amount) {
            object : BukkitRunnable() {
                val isPlantedBlock = mutableListOf<Block>()
                override fun run() {
                    val lin = region.line(walkDirection).random()
                    val sheep = SpawnEntity.spawnVillager(lin.start)
                    Walk(sheep, { entity ->
                        val loc = entity.location
                        if (region.planting().contains(loc.block) && !isPlantedBlock.contains(loc.block)) {
                            plantCrop(loc.block)
                            isPlantedBlock.add(loc.block)
                        }
                        ParticleBuilder(ParticleEffect.VILLAGER_HAPPY, entity.location.add(0.0, 1.5, 0.0))
                            .setOffsetY(5.5f)
                            .display()
                    }, lin.start, lin.end).walkUseTP(1.0)
                }
            }.runTaskLater(cyanPlugin, (i * 20).toLong())
        }
    }

    // 鹿
    fun deer(amount: Int, userName: String) {
        getOnlinePlayers().forEach {
            it.sendTitle("§c鹿", "§9嗯", 10, 40, 10)
        }
        for (i in 0 until amount) {
            object : BukkitRunnable() {
                override fun run() {
                    val lin = region.line(walkDirection).random()
                    val has = cyanPlugin.dataConfig.getInt("GameStatus.${userName}", 0)
                    cyanPlugin.dataConfig.set("GameStatus.${userName}", has + (5 * lin.getLineHasBlockSize()))
                    cyanPlugin.saveData()
                    val slime = SpawnEntity.spawnDeer(lin.start)
                    Walk(slime, { entity ->
                        val middle = entity.location.block
                        middle.getEdge(2).forEach {
                            it.breakBlock()
                        }
                    }, lin.start, lin.end).walkUseTP(1.0)
                }
            }.runTaskLater(cyanPlugin, (i * 20).toLong())
        }
    }

    // 牛
    fun cowGoGoGo(amount: Int, userName: String) {
        getOnlinePlayers().forEach {
            it.sendTitle("§c牛牛出击", "§9", 10, 40, 10)
        }
        for (i in 0 until amount * 2) {
            object : BukkitRunnable() {
                override fun run() {
                    val lin = region.line(walkDirection).random()
                    val has = cyanPlugin.dataConfig.getInt("GameStatus.${userName}", 0)
                    cyanPlugin.dataConfig.set("GameStatus.${userName}", has + (2 * lin.getLineHasBlockSize()))
                    cyanPlugin.saveData()
                    val slime = SpawnEntity.spawnCow(lin.start)
                    Walk(slime, { entity ->
                        val middle = entity.location
                        middle.block.breakBlock()
                        ParticleBuilder(ParticleEffect.VILLAGER_ANGRY, middle.add(0.0, 1.0, 0.0))
                            .setOffset(0.5f, 0.5f, 0.5f)
                            .display()
                    }, lin.start, lin.end).walkUseTP(1.0)
                }
            }.runTaskLater(cyanPlugin, (i * 20).toLong())
        }
    }

    // 凋零风暴
    fun witherStorm(amount: Int) {
        getOnlinePlayers().forEach {
            it.sendTitle("§c凋零风暴", "§9", 10, 40, 10)
        }
        templete -= amount
        for (i in 0 until amount) {
            object : BukkitRunnable() {
                override fun run() {
                    val lin = region.line(walkDirection)
                    // 把这个list lin | - - - | - - - | - - - |.....    只挑出|的部分 - 删除 就是每 3 个一组
                    val list = lin.filterIndexed { index, _ -> index % 3 == 0 }
                    list.forEach {
                        val slime = SpawnEntity.spawnWither(it.start)
                        it.start.world.time = 18000
                        Walk(slime, { entity ->
                            val middle = entity.location
                            middle.block.breakBlock()
                            ParticleBuilder(ParticleEffect.VILLAGER_ANGRY, middle.add(0.0, 2.0, 0.0))
                                .setOffset(3.5f, 3.5f, 3.5f)
                                .display()
                            // TNT随机 上空15格
                            if (randomBoolean()) {
                                region.random().add(0.0, 15.0, 0.0).fallTNT()
                            }
                            if (randomBoolean()) {
                                region.random().world.strikeLightningEffect(region.random())
                            }
                        }, it.start, it.end, { w ->
                            w.time = 6000
                        }).walkUseTP(1.0)
                    }
                }
            }.runTaskLater(cyanPlugin, (i * 20).toLong())
        }
    }

    // 末影龙
    fun dragon(amount: Int) {
        getOnlinePlayers().forEach {
            it.sendTitle("§c末影龙大清洗", "§9", 10, 40, 10)
        }
        templete -= amount
        for (i in 0 until amount) {
            object : BukkitRunnable() {
                override fun run() {
                    val lin = region.line(walkDirection)
                    val isPlantedBlock = mutableListOf<Block>()
                    // 在lin随机三条Line  并放在新的list
                    val list = mutableListOf<Line>()
                    for (j in 0 until lin.size-1) {
                        val li = lin.random()
                        if (list.size == 3) break
                        if (list.contains(li)) continue
                        list.add(li)
                    }
                    list.forEach {
                        val slime = SpawnEntity.spawnDragon(it.start)
                        it.start.world.time = 18000
                        Walk(slime, { entity ->
                            val middle = entity.location
                            middle.block.breakBlock()
                            ParticleBuilder(ParticleEffect.VILLAGER_ANGRY, middle.add(0.0, 2.0, 0.0))
                                .setOffset(3.5f, 3.5f, 3.5f)
                                .display()
                            // TNT随机 上空15格
                            if (randomBoolean() && !isPlantedBlock.contains(middle.block)) {
                                region.random().add(0.0, 15.0, 0.0).fallTNT()
                            }
                            if (randomBoolean() && !isPlantedBlock.contains(middle.block)) {
                                region.random().world.strikeLightningEffect(region.random())
                            }
                            if (!isPlantedBlock.contains(middle.block)) {
                                isPlantedBlock.add(middle.block)
                            }
                        }, it.start, it.end, { w ->
                            w.time = 6000
                        }).walkUseTP(1.0)
                    }
                }
            }.runTaskLater(cyanPlugin, (i * 20).toLong())
        }
    }


}

annotation class Mode(val value: String)