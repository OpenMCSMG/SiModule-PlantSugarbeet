package cn.cyanbukkit.plant.command

import cn.cyanbukkit.plant.GameHandle
import cn.cyanbukkit.plant.Mode
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

object MainTestCommand : Command("test") {
    override fun execute(sender: CommandSender, la: String, args: Array<String>): Boolean {
        if (args.isEmpty()) { // 快速测试比如想单独测试一个东西是
//            val p = PlantSugarBeet.region.line(walkDirection).random()
//            SpawnEn.walk(p.start,p.end,2.0, "test")
            return true
        }

        if (sender !is org.bukkit.entity.Player) {
            sender.sendMessage("§c你不是玩家")
            return true
        }
        val p = sender
        val hand = GameHandle()
        val methods = hand.javaClass.declaredMethods
        for (method in methods) {
            val mode = method.getAnnotation(Mode::class.java)
            if (mode != null) {
                if (mode.value == args[0]) {
                    val info = args.sliceArray(1 until args.size)
                    val parameterTypes = method.parameterTypes
                    val convertedArgs = Array<Any>(info.size) { i ->
                        when (parameterTypes[i + 1]) { // i + 1 because the first parameter is Player
                            Double::class.java -> info[i].toDouble()
                            Int::class.java -> info[i].toInt()
                            String::class.java -> info[i]
                            else -> throw IllegalArgumentException("Unsupported parameter type")
                        }
                    }
                    method.invoke(hand, p, *convertedArgs)
                    return true
                }
            }
        }
        return true
    }
}