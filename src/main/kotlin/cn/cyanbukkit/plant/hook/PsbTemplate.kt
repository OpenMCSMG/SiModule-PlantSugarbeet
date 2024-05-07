package cn.cyanbukkit.plant.hook

import cn.cyanbukkit.plant.PlantSugarBeet
import cn.cyanbukkit.plant.PlantSugarBeet.maxTemplate
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

object PsbTemplate : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "psbtemplate"
    }

    override fun getAuthor(): String {
        return "cyanbukkit"
    }

    override fun getVersion(): String {
        return "1.0"
    }

    override fun onPlaceholderRequest(player: Player, params: String): String? {
        return when (params) {
            "max" -> maxTemplate.toString()
            "now" -> PlantSugarBeet.templete.toString()
            else -> "0"
        }
    }
}