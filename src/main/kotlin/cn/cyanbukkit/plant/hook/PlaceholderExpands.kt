package cn.cyanbukkit.plant.hook

import cn.cyanbukkit.plant.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

object PlaceholderExpands : PlaceholderExpansion() {


    override fun getIdentifier(): String {
        return "psb"
    }

    override fun getAuthor(): String {
        return "NostMC"
    }

    override fun getVersion(): String {
        return cyanPlugin.description.version
    }

    override fun onPlaceholderRequest(player: Player, params: String): String {
        val `var` = params.split("_")
        if (`var`.size == 1) {
            // 识别出`var`[0]中字符串中的数字
            val number = Regex("\\d+").find(`var`[0])?.value?.toInt()
            if (number != null) {
                val data = getData()
                if (number > data.size) return "无人问津"
                return "${data[number - 1].name} ${data[number - 1].breakData}"
            }
        } else if (`var`.size == 2) {
            val number = Regex("\\d+").find(`var`[0])?.value?.toInt()
            if (number != null) {
                val data = getData()
                if (number > data.size) return "https://www.lanternmc.cn/files/nouser.png"
                return data[number - 1].url
            }
        }
        return "无人问津"
    }

    fun getData(): List<Data> {
        val gameStatus = if (cyanPlugin.dataConfig.contains("GameStatus")) cyanPlugin.dataConfig.getConfigurationSection("GameStatus") else return emptyList()
        val colm = gameStatus.getKeys(false).map { // it username
            Data(it, gameStatus.getInt(it), cyanPlugin.dataConfig.getString("User.${it}", "https://www.lanternmc.cn/files/nouser.png"))
        }
        //  对表里的 breakData 进行排列 最大的在前面
        return colm.sortedByDescending { it.breakData }
    }

    data class Data(
        val name: String,
        val breakData: Int,
        val url: String
    )

}