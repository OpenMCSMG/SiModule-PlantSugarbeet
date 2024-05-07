package cn.cyanbukkit.plant.effect

import cn.cyanbukkit.plant.PlantSugarBeet
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.Firework
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect


object TNTEffect : Listener {

    fun Location.playEffect() {
        // 在这个位置绽放粒子颜色随机
        val color = listOf(
            java.awt.Color.RED,
            java.awt.Color.ORANGE,
            java.awt.Color.YELLOW,
            java.awt.Color.GREEN,
            java.awt.Color.BLUE,
            java.awt.Color.MAGENTA,
            java.awt.Color.CYAN,
            java.awt.Color.PINK,
            java.awt.Color.GRAY,
            java.awt.Color.LIGHT_GRAY,
            java.awt.Color.DARK_GRAY,
            java.awt.Color.BLACK,
        ).random()
        ParticleBuilder(ParticleEffect.REDSTONE, this)
            .setColor(color)
            .setAmount(100)
            .setOffset(0.5F, 0.5F, 0.5F)
            .display()
        this.world.spawn(this, Firework::class.java).apply {
            // 设置火焰球的颜色
            val fm = fireworkMeta.apply {
                addEffect(FireworkEffect.builder().withColor(Color.fromRGB(
                    (0..255).random(),
                    (0..255).random(),
                    (0..255).random()
                )).withFlicker().with(FireworkEffect.Type.BALL).build())
            }
            fireworkMeta = fm
            //    设置立刻爆炸
            this.detonate()
            fireworkMeta.power = 0
        }
    }


    fun fartSpawnTNT(player: Location, amount: Int) {
        // 在玩家位置生成TNT
        for (i in 0 until amount) {
            // 在玩家位置生成TNT
            val tnt = player.world.spawn(player, TNTPrimed::class.java)
            // velocity 设置方向随机 力度随机
            tnt.velocity = player.direction.multiply(Math.random() * 2)
            // 设置TNT爆炸时间 3秒爆炸
            tnt.fuseTicks = 60
        }
    }

    fun randomBoolean() = Math.random() > 0.5

    fun Location.fallTNT() {
        // 在玩家位置生成TNT
        val tnt = this.world.spawn(this, TNTPrimed::class.java)
        // 设置TNT爆炸时间 3秒爆炸
        tnt.fuseTicks = 60
    }

    @EventHandler
    fun onTNTExplode(event: org.bukkit.event.entity.EntityExplodeEvent) {
        // 爆炸时remove 不在region的范围的方块
        // 爆炸范围为3以外的方块不删
        event.blockList().removeIf {
            !PlantSugarBeet.region.planting().contains(it) ||
            it.location.distance(event.location) > 1
        }
        event.location.playEffect()
    }


}