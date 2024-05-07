package cn.cyanbukkit.plant.command

import cn.cyanbukkit.plant.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SetUpCommand : Command("setup") {
    private const val HELP = """
         /setup setjoinspawn  以玩家设置玩家加入服务器的出生点
         /setup bindmode  切换绑定模式
         /setup save  保存绑定的区域
         /setup sprinklerhead 设置洒水器的头部物品
    """

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(HELP)
            return true
        }
        when (args[0]) {
            "setjoinspawn" -> {
                // 设置玩家加入服务器的出生点
                if (sender !is Player) {
                    sender.sendMessage(HELP)
                    return true
                }
                val loc = sender.location.block
                val data = "${loc.world.name},${loc.x + 0.5},${loc.y + 0.5},${loc.z + 0.5},${sender.location.yaw},${sender.location.pitch}"
                cyanPlugin.arenaConfig.set("Spawn", data)
                cyanPlugin.arenaConfig.save(cyanPlugin.arena)
                sender.sendMessage("§a设置${data}成功")
            }

            "bindmode" -> {
                BindListener.bind = !BindListener.bind
                sender.sendMessage("§a已切换绑定模式当前状态为§36${BindListener.bind}")
            }

            "save" -> {
                BindListener.save()
            }

            "sprinklerhead" -> {
                if (sender !is Player) {
                    sender.sendMessage("§c只有玩家才能执行此命令")
                    return true
                }
                val item = sender.inventory.itemInMainHand
                if (item.type == Material.AIR) {
                    sender.sendMessage("§c请手持物品")
                    return true
                }
                cyanPlugin.arenaConfig.set("SprinklerHead", item)
                cyanPlugin.arenaConfig.save(cyanPlugin.arena)
                sender.sendMessage("§a设置洒水器头部物品成功")
            }

            else -> {
                sender.sendMessage(HELP)
            }
        }
        return true
    }


}