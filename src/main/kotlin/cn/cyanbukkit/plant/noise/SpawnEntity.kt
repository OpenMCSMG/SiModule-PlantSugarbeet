package cn.cyanbukkit.plant.noise

import cn.cyanbukkit.plant.PlantSugarBeet
import cn.cyanbukkit.plant.PlantSugarBeet.region
import cn.cyanbukkit.plant.PlantSugarBeet.spawnLocation
import cn.cyanbukkit.plant.PlantSugarBeet.walkDirection
import cn.cyanbukkit.plant.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Chicken
import org.bukkit.entity.Cow
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Llama
import org.bukkit.entity.MagmaCube
import org.bukkit.entity.Villager
import org.bukkit.entity.Wither
import org.bukkit.scheduler.BukkitRunnable

object SpawnEntity {

    val npcList = mutableListOf<LivingEntity>()
    /**
     * 只处理移动部分 tp出奇迹
     */

    fun spawnCow(spawnLocation: Location) : Cow {
        val cow = spawnLocation.world.spawnEntity(spawnLocation, EntityType.COW) as Cow
        cow.isCollidable = false // 不可碰撞
        cow.isSilent = true // 无声
        cow.customName = "牛"
        return cow
    }

    fun spawnLlama(location: Location): Llama { // 根据 起始点看end的方向弄个新的位置
        val llama = location.world.spawnEntity(location, EntityType.LLAMA) as Llama
        llama.isCollidable = false // 不可碰撞
        llama.isSilent = true // 无声
        llama.customName = "羊驼"
        return llama
    }

    fun Block.breakBlock(){
        if (!region.planting().contains(this)) return
        object : BukkitRunnable() {
            override fun run() {
                this@breakBlock.world.playEffect(this@breakBlock.location, org.bukkit.Effect.STEP_SOUND, this@breakBlock.type)
                this@breakBlock.type = Material.AIR
            }
        }.runTaskLater(cyanPlugin, 10)
    }

    fun spawnMagmaSlime(location: Location, size: Int) : MagmaCube {
        // 生成岩浆史莱姆
        val magmaCube = location.world.spawnEntity(location, EntityType.MAGMA_CUBE) as MagmaCube
        magmaCube.isCollidable = false // 不可碰撞
        magmaCube.isSilent = true // 无声
        magmaCube.size = size * 2
        magmaCube.customName = "岩浆史莱姆"
        return magmaCube
    }

    // 生成羊驼
    /**
     * 示例
     */
    fun example() {
        val random = PlantSugarBeet.region.line(walkDirection).random()
        val cow = spawnCow(random.start)
        val walk = Walk(cow, { entity ->
            val loc = entity.location
            // 实体的碰撞体积多大根据大小调整范围以及四周的设置
        }, random.start, random.end)
        npcList.add(cow)
    }

    fun spawnParrot(start: Location): LivingEntity {
        val magmaCube = start.world.spawnEntity(start, EntityType.CHICKEN) as Chicken
        magmaCube.isCollidable = false // 不可碰撞
        magmaCube.isSilent = true // 无声
        magmaCube.customName = "bird"
        return magmaCube
    }

    fun spawnVillager(start: Location): Villager {
        val magmaCube = start.world.spawnEntity(start, EntityType.VILLAGER) as Villager
        magmaCube.isCollidable = false // 不可碰撞
        magmaCube.isSilent = true // 无声
        magmaCube.customName = "villager"
        magmaCube.profession = Villager.Profession.FARMER
        return magmaCube
    }

    fun spawnDeer(start: Location): LivingEntity {
        val magmaCube = start.world.spawnEntity(start, EntityType.COW) as Cow
        magmaCube.isCollidable = false // 不可碰撞
        magmaCube.isSilent = true // 无声
        magmaCube.customName = "鹿"
        return magmaCube
    }

    fun spawnWither(start: Location): LivingEntity {
        val magmaCube = start.world.spawnEntity(start, EntityType.WITHER) as Wither
        magmaCube.isCollidable = false // 不可碰撞
        magmaCube.isSilent = true // 无声
        magmaCube.customName = "with"
        return magmaCube
    }

    fun spawnDragon(start: Location): LivingEntity {
        val magmaCube = start.world.spawnEntity(start, EntityType.ENDER_DRAGON) as EnderDragon
        magmaCube.isCollidable = false // 不可碰撞
        magmaCube.isSilent = true // 无声
        magmaCube.customName = "dragon"
        return magmaCube
    }

}