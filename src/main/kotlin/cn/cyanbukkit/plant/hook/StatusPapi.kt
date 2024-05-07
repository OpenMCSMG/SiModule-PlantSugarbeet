package cn.cyanbukkit.plant.hook

import cn.cyanbukkit.plant.PlantSugarBeet.region
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

object StatusPapi : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "psbstatus"
    }

    override fun getAuthor(): String {
        return "NostMC"
    }

    override fun getVersion(): String {
        return "1.0"
    }


    override fun onPlaceholderRequest(player: Player, params: String): String {
        return when (params) {
            "now" -> {
                region.getPlantIsSuccess().size.toString()
            }

            "max" -> {
                region.getsize().toString()
            }

            else -> {
                "?"
            }
        }
    }


}