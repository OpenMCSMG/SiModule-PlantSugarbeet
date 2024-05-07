package cn.cyanbukkit.plant.noise

/**
 * 怀念 我的世界中国版失落世界  种地种一半弹个验证码 答对了还给速度
 * 我要在互游里还原他！
 * 致敬2020年的失落世界
 * @step 1 先随机一些字母
 * @step 2 根据字母随机平铺在龙核的界面上并且随机位置
 * @step 3 当点击界面时存储并记录点击的组合
 * @step 4 点击确认时判断是否正确
 */
object VerificationCode {


    // step 1
    fun randomLetter(length: Int): String {
        val str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val sb = StringBuilder()
        for (i in 0 until length) {
            val number = (Math.random() * 52).toInt()
            sb.append(str[number])
        }
        return sb.toString()
    }
    // 龙核得设置一个图的边界 晚上醒了再说
    // step 2
    fun randomPosition(length: Int): List<ScreenPosition> {
        val list = mutableListOf<ScreenPosition>()

        return list
    }





}

data class ScreenPosition(val x: Int, val y: Int)