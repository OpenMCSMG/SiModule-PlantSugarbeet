package cn.cyanbukkit.plant.listener

import cn.cyanbukkit.plant.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.nostmc.pixgame.api.LiveMessageEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object BuiltListen : Listener {

    @EventHandler
    fun onLive(e: LiveMessageEvent) {
        cyanPlugin.dataConfig.set("Users.${e.user.name}", e.user.headUrl)
        cyanPlugin.saveData()
    }


}