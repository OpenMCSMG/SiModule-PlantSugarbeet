package cn.cyanbukkit.plant.command

import cn.cyanbukkit.plant.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object BindListener : Listener {

    var pos1 : Block? = null
    var pos2 : Block? = null

    fun save() {
        if (pos1 != null && pos2 != null) {
            // save to file
            cyanPlugin.arenaConfig.set("PlantRegion.pos1", pos1!!.location)
            cyanPlugin.arenaConfig.set("PlantRegion.pos2", pos2!!.location)
            cyanPlugin.arenaConfig.save(cyanPlugin.arena)
            Bukkit.broadcastMessage("已保存区域")
        } else {
            Bukkit.broadcastMessage("请先选择区域")
        }
    }

    var bind = false

    @EventHandler
    fun onInaction(e: PlayerInteractEvent) {
        if (bind) {
            val player = e.player
            val block = e.clickedBlock
            if (block != null) {
                if (pos1 == null) {
                    pos1 = block
                    player.sendMessage("已选择第一个点")
                } else if (pos2 == null) {
                    pos2 = block
                    player.sendMessage("已选择第二个点")
                }
            }
        }
    }

}