package cn.cyanbukkit.plant.listener

import cn.cyanbukkit.plant.PlantSugarBeet
import cn.cyanbukkit.plant.PlantSugarBeet.default
import cn.cyanbukkit.plant.PlantSugarBeet.region
import cn.cyanbukkit.plant.PlantSugarBeet.regionIsInitialized
import cn.cyanbukkit.plant.PlantSugarBeet.sprinklerHeadIsInitialized
import cn.cyanbukkit.plant.PlantSugarBeet.toLocation
import cn.cyanbukkit.plant.effect.SprinklerHead
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.MagmaCube
import org.bukkit.entity.Player
import org.bukkit.entity.Slime
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot


object PlayerHandle : Listener{

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        default(player)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        default(player)
    }

    @EventHandler
    // 解决entity死亡分裂比如史莱姆
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        if (entity is Slime) {
            // 取消史莱姆分裂
            entity.size = 0
            event.droppedExp = 0
            event.drops.clear()
        }
    }

    @EventHandler
    fun onIntactOe(e: PlayerInteractEntityEvent) {
        // 左键他就给实体一个伤害
       if (e.rightClicked is LivingEntity) {
           val entity = e.rightClicked as LivingEntity
           entity.damage(1.0)
       }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onSprinklerHead(e: PlayerInteractEvent) {
        val player = e.player
        if (player.gameMode.name.contains("CREATIVE", true)) return
        if (!e.hasItem()) return
        if (sprinklerHeadIsInitialized() && e.item.isSimilar(PlantSugarBeet.sprinklerHead)) {
            e.isCancelled = true
            SprinklerHead.use(player)
            return
        }
        if ( e.player.inventory.itemInMainHand.type == Material.INK_SACK) {
            e.item.amount = 64
        }
    }

    @EventHandler
    fun placeBlock(e: BlockBreakEvent) {
       // 种子放下
        if (e.player.gameMode.name.contains("CREATIVE", true)) return
        if (regionIsInitialized() && region.planting().contains(e.block)) {
            return
        }
        e.isCancelled = true
    }


    @EventHandler
    fun placeBlock(e: BlockPlaceEvent) {
       // 种子放下
        if (e.player.gameMode.name.contains("CREATIVE", true)) return
        if (regionIsInitialized() && region.planting().contains(e.block)) {
            val item = e.block.drops
            if ( e.player.inventory.itemInMainHand.type == Material.INK_SACK) {
                return
            }
            item.forEach {
                e.player.inventory.itemInMainHand = it
            }
            return
        }
        e.isCancelled = true
    }

    @EventHandler
    fun foodChange(e: FoodLevelChangeEvent) {
        e.isCancelled = true
    }
    @EventHandler
    fun damage(e: EntityDamageEvent) {
        if (e.entity is Player) {
            //有击退不会掉血
            e.isCancelled = true
            e.damage = 0.0
            e.entity.velocity = e.entity.location.direction.multiply(-1)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun hydrate(e: BlockFadeEvent) {
        if (e.block.type == Material.SOIL) {
            e.block.data = 7.toByte()
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun hydrate2(e: BlockPhysicsEvent) {
        if (e.block.type == Material.SOIL) {
            e.block.data = 7.toByte()
            e.isCancelled = true
        }
    }


}