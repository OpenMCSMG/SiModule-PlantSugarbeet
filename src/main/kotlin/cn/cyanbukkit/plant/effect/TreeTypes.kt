package cn.cyanbukkit.plant.effect

import org.bukkit.TreeType
import java.util.*

object TreeTypes {
    enum class ETreeType {
        Oak, Large_Oak, Birch, Spruce, Large_Spruce,
        Jungle, Large_Jungle, Acacia, Dark_Oak,
        Red_Mushroom, Brown_Mushroom, Crimson_Fungus, Warped_Fungus,
        Flowering_Azalea, Azalea
    }

    private val TreeTypeByType = HashMap<ETreeType, ArrayList<String>>()

    fun InitializeTreeTypes() {
        var data = arrayListOf("TREE")
        TreeTypeByType[ETreeType.Oak] = data

        data = arrayListOf("BIG_TREE")
        TreeTypeByType[ETreeType.Large_Oak] = data

        data = arrayListOf("REDWOOD")
        TreeTypeByType[ETreeType.Spruce] = data

        data = arrayListOf("MEGA_REDWOOD")
        TreeTypeByType[ETreeType.Large_Spruce] = data

        data = arrayListOf("SMALL_JUNGLE")
        TreeTypeByType[ETreeType.Jungle] = data

        data = arrayListOf("JUNGLE")
        TreeTypeByType[ETreeType.Large_Jungle] = data

        data = arrayListOf("ACACIA")
        TreeTypeByType[ETreeType.Acacia] = data

        data = arrayListOf("DARK_OAK")
        TreeTypeByType[ETreeType.Dark_Oak] = data

        data = arrayListOf("BIRCH")
        TreeTypeByType[ETreeType.Birch] = data

        data = arrayListOf("RED_MUSHROOM")
        TreeTypeByType[ETreeType.Red_Mushroom] = data

        data = arrayListOf("BROWN_MUSHROOM")
        TreeTypeByType[ETreeType.Brown_Mushroom] = data

        data = arrayListOf("CRIMSON_FUNGUS")
        TreeTypeByType[ETreeType.Crimson_Fungus] = data

        data = arrayListOf("WARPED_FUNGUS")
        TreeTypeByType[ETreeType.Warped_Fungus] = data

        data = arrayListOf("AZALEA")
        TreeTypeByType[ETreeType.Flowering_Azalea] = data

        data = arrayListOf("AZALEA")
        TreeTypeByType[ETreeType.Azalea] = data
    }

    fun GetType(type: ETreeType): TreeType {
        return TreeType.valueOf(TreeTypeByType[type]!![0])
    }
}